package com.vp.assignmentvp_navigation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vp.assignmentvp_navigation.data.CampusRepository
import com.vp.assignmentvp_navigation.model.Club
import com.vp.assignmentvp_navigation.model.Project
import com.vp.assignmentvp_navigation.model.UserProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class CampusUiState(
    val userProfile: UserProfile = UserProfile(),
    val clubs: List<Club> = emptyList(),
    val projects: List<Project> = emptyList()
) {
    val joinedClubs: List<Club> get() = clubs.filter { it.isJoined }
    val joinedClubsCount: Int get() = joinedClubs.size
    val totalProjectsCount: Int get() = projects.size

    fun getClubById(clubId: String): Club? = clubs.firstOrNull { it.id == clubId }
    fun getProjectById(projectId: String): Project? = projects.firstOrNull { it.id == projectId }
    fun getProjectsForClub(clubId: String): List<Project> = projects.filter { it.clubId == clubId }
}

class CampusViewModel(
    private val repository: CampusRepository = CampusRepository
) : ViewModel() {

    val uiState: StateFlow<CampusUiState> = combine(
        repository.profile,
        repository.clubs,
        repository.projects
    ) { profile, clubs, projects ->
        CampusUiState(
            userProfile = profile,
            clubs = clubs,
            projects = projects
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CampusUiState()
    )

    fun toggleJoinClub(clubId: String) {
        repository.toggleJoinClub(clubId)
    }

    fun addProject(
        clubId: String,
        title: String,
        description: String,
        techStacks: List<String>
    ) {
        repository.addProject(
            clubId = clubId,
            title = title.trim(),
            description = description.trim(),
            techStacks = techStacks
        )
    }

    fun deleteProject(projectId: String) {
        repository.deleteProject(projectId)
    }

    fun updateProjectStatus(projectId: String, isCompleted: Boolean) {
        repository.updateProjectStatus(projectId, isCompleted)
    }
}
