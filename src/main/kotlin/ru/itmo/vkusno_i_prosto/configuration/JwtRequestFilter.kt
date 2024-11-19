package ru.itmo.vkusno_i_prosto.configuration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.itmo.vkusno_i_prosto.exception.AuthenticationException
import ru.itmo.vkusno_i_prosto.exception.ForbiddenType
import ru.itmo.vkusno_i_prosto.model.response.ErrorResponse
import ru.itmo.vkusno_i_prosto.service.JwtService
import ru.itmo.vkusno_i_prosto.service.UserService

@Component
class JwtRequestFilter(
    private val userService: UserService,
    private val jwtService: JwtService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authorizationHeader = request.getHeader("Authorization")

        var login: String? = null
        var jwt: String? = null

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7)
            login = jwtService.extractLogin(jwt)
        }

        if (login != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userService.getUserByLogin(login)
            if (userDetails == null) {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                return
            }

            if (jwtService.validateToken(jwt!!, userDetails.login)) {
                val authToken = UsernamePasswordAuthenticationToken(userDetails.username, null, emptyList())
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                println(authToken.isAuthenticated)
                SecurityContextHolder.getContext().authentication = authToken
            } else {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.outputStream.write(jacksonObjectMapper().writeValueAsBytes(ErrorResponse("Token expired", ForbiddenType.TOKEN_EXPIRED.name)))
                return
            }
        }
        chain.doFilter(request, response)
    }
}