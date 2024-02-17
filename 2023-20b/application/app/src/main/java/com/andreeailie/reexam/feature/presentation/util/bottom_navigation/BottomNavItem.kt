package com.andreeailie.reexam.feature.presentation.util.bottom_navigation

import com.andreeailie.reexam.R

sealed class BottomNavItem(var title: String, var icon: Int, var screenRoute: String) {
    object HomeScreen : BottomNavItem("Record", R.drawable.baseline_home_24, "home_screen")
    object SecondScreen : BottomNavItem("Manage", R.drawable.baseline_food_bank_24, "second_screen")
    object ThirdScreen : BottomNavItem("Reports", R.drawable.baseline_trending_up_24, "third_screen")

}