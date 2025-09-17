package org.example.taskmanager.repo

import kotlinx.coroutines.flow.Flow
import org.example.taskmanager.model.Task
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface TaskRepository : CoroutineCrudRepository<Task, UUID> {
    fun findAllByProjectId(projectId: UUID): Flow<Task>
    fun findAllByAssigneeId(assigneeId: UUID): Flow<Task>
}