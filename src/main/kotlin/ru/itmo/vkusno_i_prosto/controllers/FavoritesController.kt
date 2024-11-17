package ru.itmo.vkusno_i_prosto.controllers

import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import ru.itmo.vkusno_i_prosto.exception.ResponseStatusException
import ru.itmo.vkusno_i_prosto.model.response.PageableRecipeResponse
import ru.itmo.vkusno_i_prosto.repository.RecipesRepository

@RestController
@RequestMapping("/v1/favorites")
class FavoritesController(
    private val recipesRepository: RecipesRepository,
) {

    @PostMapping("/{recipe-id}")
    @ResponseStatus(HttpStatus.CREATED)
    fun addFavorite(
        @PathVariable("recipe-id") recipeId: String,
        authentication: Authentication,
    ) {
        val recipe = recipesRepository.findById(recipeId)
        if (recipe.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND.value(), "Recipe not found")
        }
        val recipeEntity = recipe.get()
        recipeEntity.favorites.add(authentication.name)
        recipesRepository.save(recipeEntity)
    }

    @GetMapping
    fun getFavorites(
        @RequestParam("offset") offset: Long?,
        @RequestParam("limit") limit: Long?,
        authentication: Authentication,
    ): PageableRecipeResponse {
        val trueOffset = offset ?: 0
        val trueLimit = limit ?: Long.MAX_VALUE
        return PageableRecipeResponse(
            recipes = recipesRepository.findByFavoritesContains(authentication.name, trueOffset, trueLimit).map { it.toRecipeResponse(authentication.name) },
            offset = trueOffset,
            limit = trueLimit,
            total = recipesRepository.countByFavoritesContains(authentication.name),
        )
    }

    @GetMapping("/{recipe-id}")
    fun isFavorite(@PathVariable("recipe-id") recipeId: String, authentication: Authentication): Boolean {
        return recipesRepository.findById(recipeId).get().favorites.contains(authentication.name)
    }
}