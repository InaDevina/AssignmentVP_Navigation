package com.vp.assignmentvp_navigation.model

data class Club(
    val id: String,
    val name: String,
    val fullName: String,
    val memberCount: Int,
    val description: String,
    val isJoined: Boolean = false,
    val iconType: String = "code"
)
