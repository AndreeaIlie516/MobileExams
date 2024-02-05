package com.andreeailie.exam.feature.presentation.util.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.andreeailie.exam.feature.presentation.add_edit_entity.AddEditEntityScreen
import com.andreeailie.exam.feature.presentation.main_section.MainScreen
import com.andreeailie.exam.feature.presentation.progress_section.ProgressScreen
import com.andreeailie.exam.feature.presentation.top_section.TopEventsScreen
import com.andreeailie.exam.feature.presentation.util.bottomnavigation.BottomNavItem

@Composable
fun NavigationGraph(
    navController: NavHostController,
    isConnected: Boolean
) {
    NavHost(navController, startDestination = BottomNavItem.Home.screenRoute) {
        composable(BottomNavItem.Home.screenRoute) {
            MainScreen(navController = navController)
        }
        composable(BottomNavItem.Progress.screenRoute) {
            ProgressScreen(isConnected = isConnected)
        }
        composable(BottomNavItem.Top.screenRoute) {
            TopEventsScreen()
        }
        composable(
            route = NavItem.Add.screenRoute + "?entityId={entityId}",
            arguments = listOf(
                navArgument(
                    name = "entityId"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            AddEditEntityScreen(navController = navController)
        }
    }
}
