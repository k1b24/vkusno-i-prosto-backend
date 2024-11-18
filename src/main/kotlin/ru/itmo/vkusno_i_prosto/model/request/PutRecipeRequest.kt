package ru.itmo.vkusno_i_prosto.model.request

import ru.itmo.vkusno_i_prosto.model.recipes.Recipe

data class PutRecipeRequest(
    val name: String,
    val image: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val tags: List<String>,
    val videoLink: String?,
    val showUsername: Boolean,
) {
    fun toRecipe(username: String): Recipe = Recipe(
        name = name,
        image = image,
        ingredients = ingredients,
        steps = steps,
        tags = tags,
        showUsername = showUsername,
        videoLink = videoLink,
        ownerUsername= username,
    )
}
