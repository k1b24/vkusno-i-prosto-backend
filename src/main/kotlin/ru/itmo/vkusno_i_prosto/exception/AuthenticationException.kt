package ru.itmo.vkusno_i_prosto.exception

class AuthenticationException(message: String) : ResponseStatusException(403, message)