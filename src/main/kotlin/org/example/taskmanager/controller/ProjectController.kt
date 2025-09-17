package org.example.taskmanager.controller

import kotlinx.coroutines.flow.Flow
import org.example.taskmanager.model.Project
import org.example.taskmanager.model.ProjectStatus
import org.example.taskmanager.service.ProjectService
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

data class CreateProjectRequest(
    val name: String,
    val description: String?,
    val ownerId: UUID,
    val status: ProjectStatus = ProjectStatus.OPEN
)

@RestController
@RequestMapping("/api/projects")
class ProjectController(
    private val projectService: ProjectService
) {

    @PostMapping
    suspend fun createProject(
        @RequestBody req: CreateProjectRequest
    ): Project =
        projectService.createProject(req.name, req.description, req.ownerId, req.status)

    @GetMapping("/{id}")
    suspend fun getProjectById(@PathVariable id: UUID): Project? =
        projectService.getProjectById(id)

    @GetMapping
    fun getAllProjects(): Flow<Project> =
        projectService.getAllProjects()

    @GetMapping("/status/{status}")
    fun getProjectsByStatus(@PathVariable status: ProjectStatus): Flow<Project> =
        projectService.getProjectsByStatus(status)

    @GetMapping("/owner/{ownerId}")
    fun getProjectsByOwner(@PathVariable ownerId: UUID): Flow<Project> =
        projectService.getProjectsByOwner(ownerId)

    @PatchMapping("/{id}/status")
    suspend fun updateProjectStatus(
        @PathVariable id: UUID,
        @RequestParam status: ProjectStatus
    ): Project? =
        projectService.updateProjectStatus(id, status)

    @DeleteMapping("/{id}")
    suspend fun deleteProject(@PathVariable id: UUID) {
        projectService.deleteProject(id)
    }
}