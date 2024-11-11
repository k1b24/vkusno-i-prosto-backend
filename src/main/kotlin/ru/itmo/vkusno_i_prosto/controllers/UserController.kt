package ru.itmo.vkusno_i_prosto.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.itmo.vkusno_i_prosto.exception.ResponseStatusException
import ru.itmo.vkusno_i_prosto.model.request.AuthenticateRequest
import ru.itmo.vkusno_i_prosto.model.request.UserRequest
import ru.itmo.vkusno_i_prosto.model.response.TokenResponse
import ru.itmo.vkusno_i_prosto.service.UserService

@RestController
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/v1/users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun createUser(@RequestBody userRequest: UserRequest) {
        userService.registerUser(userRequest)
    }

    @PostMapping("/v1/token")
    fun authenticateUser(@RequestBody userRequest: AuthenticateRequest): TokenResponse = userService.authenticateUser(userRequest)

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(ex: ResponseStatusException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.valueOf(ex.code))
    }
}