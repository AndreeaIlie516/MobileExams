package com.andreeailie.exam.feature.presentation.util

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object ProgressScreen : Screen("progress_screen")
    object TopScreen : Screen("top_screen")
    object AddEditScreen : Screen("add_edit_screen")
}