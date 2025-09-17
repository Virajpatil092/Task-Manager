package org.example.taskmanager.service

import kotlinx.coroutines.flow.Flow
import org.example.taskmanager.model.Project
import org.example.taskmanager.model.ProjectStatus
import org.example.taskmanager.repo.ProjectRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProjectService(
    private val projectRepository: ProjectRepository
) {

    suspend fun createProject(name: String, description: String?, ownerId: UUID, status: ProjectStatus): Project {
        val project = Project(name = name, description = description, ownerId = ownerId, status = status)
        return projectRepository.save(project)
    }

    suspend fun getProjectById(id: UUID): Project? =
        projectRepository.findById(id)

    fun getProjectsByStatus(status: ProjectStatus): Flow<Project> =
        projectRepository.findAllByStatus(status)

    fun getProjectsByOwner(ownerId: UUID): Flow<Project> =
        projectRepository.findAllByOwnerId(ownerId)

    fun getAllProjects(): Flow<Project> =
        projectRepository.findAll()

    suspend fun updateProjectStatus(id: UUID, newStatus: ProjectStatus): Project? {
        val project = projectRepository.findById(id) ?: return null
        val updated = project.copy(status = newStatus)
        return projectRepository.save(updated)
    }

    suspend fun deleteProject(id: UUID) {
        projectRepository.deleteById(id)
    }
}