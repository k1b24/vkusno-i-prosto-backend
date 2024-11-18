package ru.itmo.vkusno_i_prosto.exception

open class ResponseStatusException(val code: Int, override val message: String): Exception(message)

enum class ForbiddenType {
    
}