package org.example.taskmanager.service

import kotlinx.coroutines.flow.Flow
import org.example.taskmanager.model.Role
import org.example.taskmanager.model.User
import org.example.taskmanager.repo.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
) {

    suspend fun createUser(username: String, email: String, password: String, displayName: String?, role: Role = Role.USER): User {
        val user = User(username = username, displayName = displayName, email = email, password = password, role = role)
        return userRepository.save(user)
    }

    suspend fun getUserById(id: UUID): User? =
        userRepository.findById(id)

    suspend fun getUserByUsername(username: String): User? =
        userRepository.findByUsername(username)

    suspend fun getUserByEmail(email: String): User? =
        userRepository.findByEmail(email)

    fun getUsersByRole(role: Role): Flow<User> =
        userRepository.findAllByRole(role)

    fun getAllUsers(): Flow<User> =
        userRepository.findAll()

    suspend fun deleteUser(id: UUID) {
        userRepository.deleteById(id)
    }
}