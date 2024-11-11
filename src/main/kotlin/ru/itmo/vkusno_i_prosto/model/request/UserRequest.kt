package ru.itmo.vkusno_i_prosto.model.request

data class UserRequest(
    val login: String,
    val password: String,
    val username: String
)