package org.example.taskmanager.repo

import kotlinx.coroutines.flow.Flow
import org.example.taskmanager.model.Project
import org.example.taskmanager.model.ProjectStatus
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface ProjectRepository : CoroutineCrudRepository<Project, UUID> {
    fun findAllByStatus(status: ProjectStatus): Flow<Project>
    fun findAllByOwnerId(ownerId: UUID): Flow<Project>
}