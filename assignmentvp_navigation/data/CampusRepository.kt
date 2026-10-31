package com.vp.assignmentvp_navigation.data

import com.vp.assignmentvp_navigation.model.Club
import com.vp.assignmentvp_navigation.model.Project
import com.vp.assignmentvp_navigation.model.ProjectStatus
import com.vp.assignmentvp_navigation.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object CampusRepository {
    private val _profile = MutableStateFlow(UserProfile())
    val profile: StateFlow<UserProfile> = _profile.asStateFlow()

    private val _clubs = MutableStateFlow<List<Club>>(
        listOf(
            Club(
                id = "gdsc",
                name = "GDSC",
                fullName = "Google Developer Student Club",
                memberCount = 142,
                description = "Fokus pada ekosistem Google: Android Compose, Flutter, Firebase, dan Google Cloud Platform.",
                isJoined = true,
                iconType = "code"
            ),
            Club(
                id = "cybersecurity",
                name = "CyberSecurity Guild",
                fullName = "Security & PenTesting",
                memberCount = 88,
                description = "Eksplorasi ethical hacking, CTF challenges, reverse engineering, dan pertahanan jaringan kampus.",
                isJoined = true,
                iconType = "security"
            ),
            Club(
                id = "ai_society",
                name = "AI Society",
                fullName = "Machine Learning & LLM Community",
                memberCount = 115,
                description = "Riset terapan deep learning, computer vision, agen cerdas, dan fine-tuning large language models.",
                isJoined = false,
                iconType = "ai"
            ),
            Club(
                id = "mobile_dev",
                name = "Mobile Dev Community",
                fullName = "Jetpack Compose & KMP",
                memberCount = 96,
                description = "Pengembangan aplikasi native modern Android & iOS dengan arsitektur terkini dan clean code.",
                isJoined = true,
                iconType = "mobile"
            ),
            Club(
                id = "web_dev",
                name = "Web Development Club",
                fullName = "Fullstack Modern Web",
                memberCount = 130,
                description = "Mempelajari pengembangan web modern fullstack dengan React, Vue, Next.js, Ktor, dan cloud hosting.",
                isJoined = false,
                iconType = "web"
            ),
            Club(
                id = "cloud_devops",
                name = "Cloud & DevOps Hub",
                fullName = "Cloud Architecture & Infrastructure",
                memberCount = 75,
                description = "Mendalami infrastruktur cloud, CI/CD pipeline, Docker, Kubernetes, dan automation workflow.",
                isJoined = false,
                iconType = "cloud"
            )
        )
    )
    val clubs: StateFlow<List<Club>> = _clubs.asStateFlow()

    private val _projects = MutableStateFlow<List<Project>>(
        listOf(
            Project(
                id = "proj-gdsc-01",
                clubId = "gdsc",
                clubName = "GDSC",
                title = "Smart Campus Navigation",
                description = "Sistem navigasi peta gedung kampus interaktif berbasis Jetpack Compose dan Google Maps SDK. Memfasilitasi mahasiswa dan dosen menemukan ruang perkuliahan, lab komputer, perpustakaan, serta fasilitas umum kampus secara akurat disertai rute tercepat dan visualisasi lokasi real-time.",
                techStacks = listOf("Kotlin", "Jetpack Compose", "Room", "Google Maps SDK"),
                status = ProjectStatus.IN_PROGRESS
            ),
            Project(
                id = "proj-gdsc-02",
                clubId = "gdsc",
                clubName = "GDSC",
                title = "Student Attendance QR Scanner",
                description = "Pemindai barcode & QR presensi kehadiran workshop berbasis CameraX & Firebase.",
                techStacks = listOf("Jetpack Compose", "Firebase", "CameraX"),
                status = ProjectStatus.COMPLETED
            ),
            Project(
                id = "proj-gdsc-03",
                clubId = "gdsc",
                clubName = "GDSC",
                title = "Developer RSVP Portal",
                description = "Portal pendaftaran webinar dan bootcamp teknologi sinkronisasi Ktor API server.",
                techStacks = listOf("Kotlin", "Ktor", "Compose"),
                status = ProjectStatus.IN_PROGRESS
            ),
            Project(
                id = "proj-sec-01",
                clubId = "cybersecurity",
                clubName = "CyberSecurity Guild",
                title = "Vulnerability Network Scanner",
                description = "Tool audit penetrasi port jaringan lokal kampus dan scanner celah keamanan CVE otomatis.",
                techStacks = listOf("Python", "Linux", "Socket API"),
                status = ProjectStatus.IN_PROGRESS
            ),
            Project(
                id = "proj-mob-01",
                clubId = "mobile_dev",
                clubName = "Mobile Dev Community",
                title = "Campus News & Event App",
                description = "Aplikasi berita kegiatan kampus dengan offline-first architecture dan push notifications.",
                techStacks = listOf("Kotlin", "Jetpack Compose", "Room", "Firebase"),
                status = ProjectStatus.COMPLETED
            )
        )
    )
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    fun toggleJoinClub(clubId: String) {
        _clubs.update { list ->
            list.map { club ->
                if (club.id == clubId) {
                    club.copy(isJoined = !club.isJoined)
                } else club
            }
        }
    }

    fun addProject(
        clubId: String,
        title: String,
        description: String,
        techStacks: List<String>
    ) {
        val club = _clubs.value.firstOrNull { it.id == clubId }
        val clubName = club?.name ?: "Club"
        val countForClub = _projects.value.count { it.clubId == clubId } + 1
        val newId = "proj-${clubId.lowercase().take(4)}-${String.format("%02d", countForClub)}"
        val newProject = Project(
            id = newId,
            clubId = clubId,
            clubName = clubName,
            title = title,
            description = description,
            techStacks = techStacks,
            status = ProjectStatus.IN_PROGRESS
        )
        _projects.update { it + newProject }
    }

    fun deleteProject(projectId: String) {
        _projects.update { list -> list.filterNot { it.id == projectId } }
    }

    fun updateProjectStatus(projectId: String, isCompleted: Boolean) {
        _projects.update { list ->
            list.map { project ->
                if (project.id == projectId) {
                    project.copy(
                        status = if (isCompleted) ProjectStatus.COMPLETED else ProjectStatus.IN_PROGRESS
                    )
                } else project
            }
        }
    }

    fun getClub(clubId: String): Club? = _clubs.value.firstOrNull { it.id == clubId }

    fun getProject(projectId: String): Project? = _projects.value.firstOrNull { it.id == projectId }
}
