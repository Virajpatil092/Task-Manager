package org.example.taskmanager.repo

import kotlinx.coroutines.flow.Flow
import org.example.taskmanager.model.Role
import org.example.taskmanager.model.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID


interface UserRepository : CoroutineCrudRepository<User, UUID> {
    suspend fun findByUsername(username: String): User?
    suspend fun findByEmail(email: String): User?
    fun findAllByRole(role: Role): Flow<User>
}