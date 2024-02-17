package com.andreeailie.reexam.feature.presentation.util.bottom_navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.andreeailie.reexam.R

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.HomeScreen,
        BottomNavItem.SecondScreen,
        BottomNavItem.ThirdScreen
    )

    androidx.compose.material.BottomNavigation(
        modifier = Modifier
            .height(60.dp),
        backgroundColor = colorResource(id = R.color.white),
        contentColor = colorResource(id = R.color.grey)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(35.dp)
                            .padding(bottom = 4.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 16.sp
                    )
                },
                selectedContentColor = colorResource(id = R.color.purple),
                unselectedContentColor = colorResource(id = R.color.grey).copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {

                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}