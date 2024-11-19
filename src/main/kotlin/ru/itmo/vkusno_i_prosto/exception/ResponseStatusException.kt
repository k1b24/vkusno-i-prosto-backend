package ru.itmo.vkusno_i_prosto.exception

open class ResponseStatusException(val code: Int, override val message: String, val forbiddenType: ForbiddenType? = null): Exception(message)

enum class ForbiddenType {
    BAD_AUTH,
    USER_EXISTS,
    NO_PERMISSION,
    TOKEN_EXPIRED,
}
