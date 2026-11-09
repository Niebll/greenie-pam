package com.example.greeniepam.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.greeniepam.feature.detail_post.ui.DetailPostScreen
import com.example.greeniepam.ui.MainScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            MainScreen(
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.Detail(id))
                }
            )
        }
        composable<Screen.Detail> { backStackEntry ->
            val detail: Screen.Detail = backStackEntry.toRoute()
            DetailPostScreen(
                itemId = detail.id,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
