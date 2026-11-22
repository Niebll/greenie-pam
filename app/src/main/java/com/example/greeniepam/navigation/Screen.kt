package com.example.greeniepam.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Auth : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data class Detail(val id: String) : Screen

    @Serializable
    data class DetailPost(val postId: String) : Screen

    @Serializable
    data object EditProfile : Screen

    @Serializable
    data object Favorites : Screen

    @Serializable
    data class PustakaDetail(val plantId: Long) : Screen
}
