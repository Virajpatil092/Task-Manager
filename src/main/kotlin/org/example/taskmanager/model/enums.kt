package org.example.taskmanager.model

enum class ProjectStatus {
    OPEN,
    ARCHIVED
}

enum class TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE,
    BLOCKED
}

enum class Role {
    USER,
    OWNER,
    ADMIN
}