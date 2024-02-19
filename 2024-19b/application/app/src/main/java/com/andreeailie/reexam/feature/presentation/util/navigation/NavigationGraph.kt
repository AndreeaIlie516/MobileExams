package com.andreeailie.reexam.feature.presentation.util.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.andreeailie.reexam.feature.data.network.NetworkStatusTracker
import com.andreeailie.reexam.feature.presentation.add_edit_entity.AddEditEntityScreen
import com.andreeailie.reexam.feature.presentation.main_section.MainScreen
import com.andreeailie.reexam.feature.presentation.second_section.SecondScreen
import com.andreeailie.reexam.feature.presentation.third_section.ThirdScreen
import com.andreeailie.reexam.feature.presentation.util.Screen
import com.andreeailie.reexam.feature.presentation.util.bottom_navigation.BottomNavItem

@Composable
fun NavigationGraph(
    navController: NavHostController,
    networkStatusTracker: NetworkStatusTracker
) {
    NavHost(navController, startDestination = BottomNavItem.HomeScreen.screenRoute) {
        composable(BottomNavItem.HomeScreen.screenRoute) {
            MainScreen(navController = navController)
        }
        composable(BottomNavItem.SecondScreen.screenRoute) {
            SecondScreen(navController = navController, networkStatusTracker = networkStatusTracker)
        }
//        composable(BottomNavItem.ThirdScreen.screenRoute) {
//            ThirdScreen(networkStatusTracker = networkStatusTracker)
//        }
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