package org.example.taskmanager.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("tasks")
data class Task(
    @Id
    val id: UUID? = null,
    val title: String,
    val description: String? = null,
    val projectId: UUID,
    val assigneeId: UUID? = null,
    val status: TaskStatus = TaskStatus.TODO,
    val createdAt: Instant = Instant.now()
)