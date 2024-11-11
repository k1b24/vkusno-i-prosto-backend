package ru.itmo.vkusno_i_prosto.exception

class DatabaseException(message: String) : ResponseStatusException(500, message)