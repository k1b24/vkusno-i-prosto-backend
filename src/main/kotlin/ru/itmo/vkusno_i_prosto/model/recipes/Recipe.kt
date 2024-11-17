package ru.itmo.vkusno_i_prosto.model.recipes

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("recipes")
data class Recipe(
    @Id
    var id: String? = null,
    val name: String,
    val image: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val tags: List<String>,
    val showUsername: Boolean,
    val ownerUsername: String,
    val videoLink: String?,
    var favoriteBy: List<String> = emptyList(),
)
