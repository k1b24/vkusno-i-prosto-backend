package ru.itmo.vkusno_i_prosto.exception

class AuthenticationException(message: String, forbiddenType: ForbiddenType) : ResponseStatusException(403, message, forbiddenType)