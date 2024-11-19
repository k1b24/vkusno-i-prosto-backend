package ru.itmo.vkusno_i_prosto.service

import org.springframework.stereotype.Service
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import ru.itmo.vkusno_i_prosto.exception.AuthenticationException
import ru.itmo.vkusno_i_prosto.exception.DatabaseException
import ru.itmo.vkusno_i_prosto.exception.ForbiddenType
import ru.itmo.vkusno_i_prosto.model.request.AuthenticateRequest
import ru.itmo.vkusno_i_prosto.model.request.UserRequest
import ru.itmo.vkusno_i_prosto.model.response.TokenResponse
import ru.itmo.vkusno_i_prosto.model.user.VipUser
import ru.itmo.vkusno_i_prosto.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
) {

    private val passwordEncoder = BCryptPasswordEncoder()

    fun registerUser(request: UserRequest) {
        try {
            if (userRepository.existsByLogin(request.login)) {
                throw AuthenticationException("User with login '${request.login}' already exists.", ForbiddenType.USER_EXISTS)
            }
            if (userRepository.existsByUsername(request.username)) {
                throw AuthenticationException("User with username '${request.username}' already exists.", ForbiddenType.USER_EXISTS)
            }

            val vipUser = VipUser(
                login = request.login,
                password = passwordEncoder.encode(request.password),
                username = request.username,
            )

            userRepository.save(vipUser)
        } catch (ex: DataIntegrityViolationException) {
            throw DatabaseException("Error during user registration: ${ex.message}")
        }
    }

    fun authenticateUser(request: AuthenticateRequest): TokenResponse {
        val user = userRepository.findByLogin(request.login)
        if (user != null && passwordEncoder.matches(request.password, user.password)) {
            return TokenResponse(
                jwtService.generateToken(user.login, user.username),
                jwtService.tokenTtl,
            )
        } else {
            throw AuthenticationException("User with login '${request.login}' not found.", ForbiddenType.BAD_AUTH)
        }
    }

    fun getUserByLogin(login: String): VipUser? = userRepository.findByLogin(login)
}
