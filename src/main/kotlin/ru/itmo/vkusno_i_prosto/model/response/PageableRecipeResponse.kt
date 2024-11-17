package ru.itmo.vkusno_i_prosto.model.response

data class PageableRecipeResponse(
    val recipes: List<RecipeResponse>,
    val offset: Long,
    val limit: Long,
    val total: Long,
)
