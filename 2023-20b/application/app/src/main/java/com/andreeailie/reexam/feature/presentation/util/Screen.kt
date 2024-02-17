package com.andreeailie.reexam.feature.presentation.util

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object SecondScreen : Screen("second_screen")
    object ThirdScreen : Screen("third_screen")
    object AddEditScreen : Screen("add_edit_screen")
}