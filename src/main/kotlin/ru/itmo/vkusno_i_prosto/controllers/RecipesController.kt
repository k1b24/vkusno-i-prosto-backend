package ru.itmo.vkusno_i_prosto.controllers

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.itmo.vkusno_i_prosto.model.request.PostRecipeRequest

@RestController
@RequestMapping("/v1/recipes")
class RecipesController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createRecipe(
        @RequestBody postRecipeRequest: PostRecipeRequest,
    ) {
        throw NotImplementedError()
    }

    @PutMapping("/{recipe-id}")
    fun updateRecipe() {
        throw NotImplementedError()
    }

    @GetMapping("/{recipe-id}")
    fun getRecipeById() {
        throw NotImplementedError()
    }

    @GetMapping
    fun getRecipes(): String {
        throw NotImplementedError()
    }

    @DeleteMapping("/{recipe-id}")
    fun deleteRecipeById() {
        throw NotImplementedError()
    }
}