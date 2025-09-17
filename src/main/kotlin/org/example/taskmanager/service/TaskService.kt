package org.example.taskmanager.service

import kotlinx.coroutines.flow.Flow
import org.example.taskmanager.model.Task
import org.example.taskmanager.model.TaskStatus
import org.example.taskmanager.repo.TaskRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TaskService(
    private val taskRepository: TaskRepository
) {

    suspend fun createTask(
        title: String,
        description: String?,
        projectId: UUID,
        assigneeId: UUID? = null,
        status: TaskStatus
    ): Task {
        val task = Task(title = title, description = description, projectId = projectId, assigneeId = assigneeId, status = status)
        return taskRepository.save(task)
    }

    suspend fun getTaskById(id: UUID): Task? =
        taskRepository.findById(id)

    fun getTasksByProject(projectId: UUID): Flow<Task> =
        taskRepository.findAllByProjectId(projectId)

    fun getTasksByAssignee(assigneeId: UUID): Flow<Task> =
        taskRepository.findAllByAssigneeId(assigneeId)

    fun getAllTasks(): Flow<Task> =
        taskRepository.findAll()

    suspend fun assignTask(taskId: UUID, assigneeId: UUID): Task? {
        val task = taskRepository.findById(taskId) ?: return null
        val updated = task.copy(assigneeId = assigneeId)
        return taskRepository.save(updated)
    }

    suspend fun updateTaskStatus(taskId: UUID, newStatus: TaskStatus): Task? {
        val task = taskRepository.findById(taskId) ?: return null
        val updated = task.copy(status = newStatus)
        return taskRepository.save(updated)
    }

    suspend fun deleteTask(id: UUID) {
        taskRepository.deleteById(id)
    }
}