package com.andreeailie.reexam.feature.presentation.util.navigation

sealed class NavItem(var title: String, var screenRoute: String) {
    object Add : NavItem("AddEdit", "add_edit_screen")
}