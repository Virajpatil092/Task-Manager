package org.example.taskmanager.controller

import kotlinx.coroutines.flow.Flow
import org.example.taskmanager.model.Task
import org.example.taskmanager.model.TaskStatus
import org.example.taskmanager.service.TaskService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

data class CreateTaskRequest(
    val title: String,
    val description: String?,
    val projectId: UUID,
    val assigneeId: UUID?,
    val status: TaskStatus = TaskStatus.TODO
)

@RestController
@RequestMapping("/api/tasks")
class TaskController(
    private val taskService: TaskService
) {

    @PostMapping
    suspend fun createTask(
        @RequestBody req: CreateTaskRequest
    ): Task =
        taskService.createTask(req.title, req.description, req.projectId, req.assigneeId, req.status)

    @GetMapping("/{id}")
    suspend fun getTaskById(@PathVariable id: UUID): Task? =
        taskService.getTaskById(id)

    @GetMapping
    fun getAllTasks(): Flow<Task> =
        taskService.getAllTasks()

    @GetMapping("/project/{projectId}")
    fun getTasksByProject(@PathVariable projectId: UUID): Flow<Task> =
        taskService.getTasksByProject(projectId)

    @GetMapping("/assignee/{assigneeId}")
    fun getTasksByAssignee(@PathVariable assigneeId: UUID): Flow<Task> =
        taskService.getTasksByAssignee(assigneeId)

    @PatchMapping("/{id}/assign")
    suspend fun assignTask(
        @PathVariable id: UUID,
        @RequestParam assigneeId: UUID
    ): Task? =
        taskService.assignTask(id, assigneeId)

    @PatchMapping("/{id}/status")
    suspend fun updateTaskStatus(
        @PathVariable id: UUID,
        @RequestParam status: TaskStatus
    ): Task? =
        taskService.updateTaskStatus(id, status)

    @DeleteMapping("/{id}")
    suspend fun deleteTask(@PathVariable id: UUID) {
        taskService.deleteTask(id)
    }
}