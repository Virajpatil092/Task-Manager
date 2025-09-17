package org.example.taskmanager.config

import kotlinx.coroutines.reactor.mono
import org.example.taskmanager.security.JwtUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val jwtUtil: JwtUtil
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { it.disable() }
            .httpBasic { it.disable() } // we will use JWT
            .formLogin { it.disable() }
            .securityContextRepository(JwtSecurityContextRepository(jwtUtil))
            .authorizeExchange {
                it.pathMatchers("/auth/**").permitAll()
                it.pathMatchers("/public/**").permitAll()
                it.anyExchange().authenticated()
            }
            .build()
    }
}

/**
 * Extracts Bearer token, validates via JwtUtil and creates a SecurityContext.
 */
class JwtSecurityContextRepository(private val jwtUtil: JwtUtil) : ServerSecurityContextRepository {
    override fun save(exchange: ServerWebExchange, context: SecurityContext): Mono<Void> {
        // not needed for stateless JWT
        return Mono.empty()
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        val authHeader = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION) ?: return Mono.empty()
        if (!authHeader.startsWith("Bearer ")) return Mono.empty()
        val token = authHeader.substring(7)
        return mono {
            if (!jwtUtil.validateToken(token)) return@mono null
            val username = jwtUtil.getUsernameFromToken(token)
            val roles = jwtUtil.getRolesFromToken(token).map { SimpleGrantedAuthority("ROLE_$it") }
            val auth = UsernamePasswordAuthenticationToken(username, null, roles)
            SecurityContextImpl(auth) as SecurityContext
        }
    }
}
