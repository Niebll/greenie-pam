package com.example.greeniepam.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.greeniepam.feature.detail_post.ui.DetailPostScreen
import com.example.greeniepam.ui.DetailScreen
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
                },
                onNavigateToDetailPost = { postId ->
                    navController.navigate(Screen.DetailPost(postId))
                }
            )
        }
        composable<Screen.Detail> { backStackEntry ->
            val detail: Screen.Detail = backStackEntry.toRoute()
            DetailScreen(
                itemId = detail.id,
                onBack = { navController.popBackStack() }
            )
        }
        composable<Screen.DetailPost> { backStackEntry ->
            val detailPost: Screen.DetailPost = backStackEntry.toRoute()
            DetailPostScreen(
                itemId = detailPost.postId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
