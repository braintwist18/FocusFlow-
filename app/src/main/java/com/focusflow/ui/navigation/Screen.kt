package com.focusflow.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Home : Screen()

    @Serializable
    data object Task : Screen()

    @Serializable
    data object Forest : Screen()

    fun route(): String = when (this) {
        Home -> "home"
        Task -> "task"
        Forest -> "forest"
    }
}
