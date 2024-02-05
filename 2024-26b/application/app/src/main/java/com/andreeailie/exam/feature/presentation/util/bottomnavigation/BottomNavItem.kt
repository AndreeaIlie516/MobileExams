package com.andreeailie.exam.feature.presentation.util.bottomnavigation

import com.andreeailie.exam.R

sealed class BottomNavItem(var title: String, var icon: Int, var screenRoute: String) {
    object Home : BottomNavItem("Home", R.drawable.baseline_fitbit_24, "home_screen")
    object Progress : BottomNavItem("Progress", R.drawable.baseline_show_chart_24, "progress_screen")
    object Top : BottomNavItem("Top", R.drawable.baseline_moving_24, "top_screen")

}