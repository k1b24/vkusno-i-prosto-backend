package ru.itmo.vkusno_i_prosto.exception

class UserAlreadyExistsException(message: String) : ResponseStatusException(404, message)
