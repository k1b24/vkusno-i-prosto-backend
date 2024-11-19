package ru.itmo.vkusno_i_prosto.model.response

data class ErrorResponse(
    val message: String,
    val forbiddenType: String? = null,
)
