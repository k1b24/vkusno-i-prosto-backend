package ru.itmo.vkusno_i_prosto.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.*

@Configuration
@EnableWebSecurity(debug = true)
class SecurityConfiguration(
    private val jwtRequestFilter: JwtRequestFilter,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
        .authorizeHttpRequests {
            it.requestMatchers("/v1/users").permitAll()
            it.requestMatchers("/v1/token").permitAll()
            it.requestMatchers("/error").permitAll()
            it.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/v1/recipes/")).permitAll()
            it.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/v1/recipes/*")).permitAll()
            it.anyRequest().authenticated()
        }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowCredentials = true
        configuration.allowedHeaders = listOf("*")
        configuration.allowedOriginPatterns = Collections.singletonList("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH", "HEAD")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}