package org.example.taskmanager.config

import kotlinx.coroutines.runBlocking
import org.example.taskmanager.model.Role
import org.example.taskmanager.model.User
import org.example.taskmanager.repo.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@Configuration
class DataInitializer {

    @Bean
    fun initUsers(userRepository: UserRepository, passwordEncoder: PasswordEncoder) = ApplicationRunner {
        runBlocking {
            val existing = userRepository.findByUsername("user")
            if (existing == null) {
                val user = User(
                    username = "user",
                    email = "testuser@example.com",
                    password = passwordEncoder.encode("password"),
                    displayName = "Test User",
                    role = Role.USER
                )
                userRepository.save(user)
                println("✅ Created default test user: username='user' password='password'")
            } else {
                println("ℹ️ Default test user already exists")
            }
        }
    }
}