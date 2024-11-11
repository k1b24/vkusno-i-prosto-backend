package ru.itmo.vkusno_i_prosto.model.response

data class TokenResponse(
    val token: String,
    val ttl: Long,
)
