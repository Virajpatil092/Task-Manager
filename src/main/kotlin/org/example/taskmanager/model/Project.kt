package org.example.taskmanager.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("projects")
data class Project(
    @Id
    val id: UUID? = null,
    val name: String,
    val description: String? = null,
    val ownerId: UUID,
    val status: ProjectStatus = ProjectStatus.OPEN
)