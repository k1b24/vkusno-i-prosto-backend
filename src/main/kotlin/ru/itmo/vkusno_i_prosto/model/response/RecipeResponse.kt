package ru.itmo.vkusno_i_prosto.model.response

import ru.itmo.vkusno_i_prosto.model.recipes.Recipe

data class RecipeResponse(
    val id: String,
    val name: String,
    val image: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val tags: List<String>,
    val ownerUsername: String?,
    val showUsername: Boolean,
    val videoLink: String?,
)
