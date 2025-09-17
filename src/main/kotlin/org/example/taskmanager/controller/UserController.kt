package org.example.taskmanager.controller

import kotlinx.coroutines.flow.Flow
import org.example.taskmanager.model.Role
import org.example.taskmanager.model.User
import org.example.taskmanager.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

data class CreateUserRequest(
    val username: String,
    val email: String,
    val password: String,
    val displayName: String? = null,
    val role: Role = Role.USER
)

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    suspend fun createUser(
        @RequestBody req: CreateUserRequest
    ): User =
        userService.createUser(req.username, req.username, req.password, req.displayName, req.role)

    @GetMapping("/{id}")
    suspend fun getUserById(@PathVariable id: UUID): User? =
        userService.getUserById(id)

    @GetMapping
    fun getAllUsers(): Flow<User> =
        userService.getAllUsers()

    @GetMapping("/role/{role}")
    fun getUsersByRole(@PathVariable role: Role): Flow<User> =
        userService.getUsersByRole(role)

    @DeleteMapping("/{id}")
    suspend fun deleteUser(@PathVariable id: UUID) {
        userService.deleteUser(id)
    }
}