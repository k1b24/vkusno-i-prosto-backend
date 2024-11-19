package ru.itmo.vkusno_i_prosto.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.itmo.vkusno_i_prosto.exception.ForbiddenType
import ru.itmo.vkusno_i_prosto.exception.ResponseStatusException
import ru.itmo.vkusno_i_prosto.model.recipes.Recipe
import ru.itmo.vkusno_i_prosto.model.request.PostRecipeRequest
import ru.itmo.vkusno_i_prosto.model.request.PutRecipeRequest
import ru.itmo.vkusno_i_prosto.model.response.ErrorResponse
import ru.itmo.vkusno_i_prosto.model.response.PageableRecipeResponse
import ru.itmo.vkusno_i_prosto.model.response.RecipeResponse
import ru.itmo.vkusno_i_prosto.repository.RecipesRepository
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/v1/recipes")
class RecipesController(
    private val recipesRepository: RecipesRepository,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createRecipe(
        @RequestBody postRecipeRequest: PostRecipeRequest,
        authentication: Authentication,
    ) {
        recipesRepository.save(postRecipeRequest.toRecipe(authentication.name))
    }

    @PutMapping("/{recipe-id}")
    fun updateRecipe(
        @RequestBody putRecipeRequest: PutRecipeRequest,
        authentication: Authentication,
    ) {
        recipesRepository.save(putRecipeRequest.toRecipe(authentication.name))
    }

    @GetMapping("/{recipe-id}")
    fun getRecipeById(
        @PathVariable(name = "recipe-id") recipeId: String,
        authentication: Authentication?,
    ): RecipeResponse {
        val recipe = recipesRepository.findById(recipeId)
        if (recipe.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND.value(), "Recipe not found")
        }
        return recipe.get().toRecipeResponse(authentication?.name)
    }

    @GetMapping
    fun getRecipes(
        @RequestParam("offset") offset: Long?,
        @RequestParam("limit") limit: Long?,
        @RequestParam("ingredients") ingredients: List<String>?,
        @RequestParam("excludeIngredients") excludeIngredients: List<String>?,
        @RequestParam("name") name: String?,
        authentication: Authentication?,
    ): PageableRecipeResponse {
        val trueOffset = offset ?: 0
        val trueLimit = limit ?: Long.MAX_VALUE
        val total = recipesRepository.count()
        val trueExclude = excludeIngredients ?: emptyList()
        val trueName = name ?: ".*"
        val recipes = if (ingredients.isNullOrEmpty()) {
            recipesRepository.findAll(trueExclude, trueName, trueOffset, trueLimit)
        } else {
            recipesRepository.findAll(ingredients, trueExclude, trueName, trueOffset, trueLimit)
        }
        return PageableRecipeResponse(
            recipes = recipes.map { it.toRecipeResponse(authentication?.name) },
            offset = trueOffset,
            limit = trueLimit,
            total = total,
        )
    }

    @GetMapping("/user")
    fun getRecipesByUser(
        @RequestParam("offset") offset: Long?,
        @RequestParam("limit") limit: Long?,
        @RequestParam("ingredients") ingredients: List<String>?,
        @RequestParam("excludeIngredients") excludeIngredients: List<String>?,
        @RequestParam("name") name: String?,
        authentication: Authentication,
    ): PageableRecipeResponse {
        val trueOffset = offset ?: 0
        val trueLimit = limit ?: Long.MAX_VALUE
        val total = recipesRepository.count()
        val trueExclude = excludeIngredients ?: emptyList()
        val trueName = name ?: ".*"
        val recipes = if (ingredients.isNullOrEmpty()) {
            recipesRepository.findAllByUsername(authentication.name, trueExclude, trueName, trueOffset, trueLimit)
        } else {
            recipesRepository.findAllByUsername(ingredients, trueExclude, authentication.name, trueName, trueOffset, trueLimit)
        }
        return PageableRecipeResponse(
            recipes = recipes.map { it.toRecipeResponse(authentication.name) },
            offset = trueOffset,
            limit = trueLimit,
            total = total,
        )
    }

    @DeleteMapping("/{recipe-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRecipeById(
        @PathVariable(name = "recipe-id") recipeId: String,
        authentication: Authentication,
    ) {
        val recipe = recipesRepository.findById(recipeId)
        if (recipe.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND.value(), "Recipe not found")
        }
        if (recipe.get().ownerUsername != authentication.name) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN.value(), "You are not the owner of this recipe", ForbiddenType.NO_PERMISSION)
        }
        recipesRepository.deleteById(recipeId)
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(e: ResponseStatusException): ResponseEntity<ErrorResponse> {
        println("here")
        return ResponseEntity.status(e.code).body(ErrorResponse(e.message, e.forbiddenType?.name))
    }
}

fun Recipe.toRecipeResponse(username: String?) = RecipeResponse(
    id = id!!,
    name = name,
    image = image,
    ingredients = ingredients,
    steps = steps,
    tags = tags,
    ownerUsername = if (showUsername || username == ownerUsername) ownerUsername else null,
    showUsername = showUsername,
    videoLink = videoLink,
)