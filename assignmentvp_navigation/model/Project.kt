package com.vp.assignmentvp_navigation.model

enum class ProjectStatus(val label: String) {
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed")
}

data class Project(
    val id: String,
    val clubId: String,
    val clubName: String,
    val title: String,
    val description: String,
    val techStacks: List<String>,
    val status: ProjectStatus = ProjectStatus.IN_PROGRESS
)
