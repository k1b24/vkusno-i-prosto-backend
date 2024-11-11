package ru.itmo.vkusno_i_prosto.model.request

data class AuthenticateRequest(
    val login: String,
    val password: String,
)
