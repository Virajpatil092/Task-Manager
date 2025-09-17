package org.example.taskmanager.controller

import org.example.taskmanager.model.Role
import org.example.taskmanager.model.User
import org.example.taskmanager.repo.UserRepository
import org.example.taskmanager.security.JwtUtil
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*

data class SignupRequest(val username: String, val email: String, val password: String, val displayName: String? = null)
data class LoginRequest(val username: String, val password: String)
data class TokenResponse(val token: String)

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun signup(@RequestBody req: SignupRequest): User {
        // basic existence checks
        if (userRepository.findByUsername(req.username) != null) {
            throw IllegalArgumentException("username already exists")
        }
        if (userRepository.findByEmail(req.email) != null) {
            throw IllegalArgumentException("email already exists")
        }
        val user = User(
            username = req.username,
            email = req.email,
            password = passwordEncoder.encode(req.password),
            displayName = req.displayName,
            role = Role.USER
        )
        return userRepository.save(user)
    }

    @PostMapping("/login")
    suspend fun login(@RequestBody req: LoginRequest): TokenResponse {
        val user = userRepository.findByUsername(req.username)
            ?: throw IllegalArgumentException("invalid credentials")
        if (!passwordEncoder.matches(req.password, user.password)) {
            throw IllegalArgumentException("invalid credentials")
        }
        val token = jwtUtil.generateToken(user.username, listOf(user.role))
        return TokenResponse(token)
    }

    @GetMapping("/me")
    suspend fun me(@AuthenticationPrincipal principal: Any?): Map<String, Any?> {
        // When JWT auth is used the SecurityContextRepository sets principal as username (String)
        // We can return username and roles by reading SecurityContext if required.
        return mapOf("principal" to principal)
    }
}
