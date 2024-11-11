package ru.itmo.vkusno_i_prosto.model.request

data class PostRecipeRequest(
    val name: String,
    val imageUrl: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val tags: List<String>,
    val showAuthorName: Boolean,
    val videoUrl: String?,
)
