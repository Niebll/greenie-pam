package com.example.greeniepam.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.greeniepam.core.session.SessionState
import com.example.greeniepam.core.session.SessionViewModel
import com.example.greeniepam.core.theme.greenieCanvas
import com.example.greeniepam.feature.auth.ui.AuthScreen
import com.example.greeniepam.feature.detail_post.ui.DetailPostScreen
import com.example.greeniepam.feature.profile.ui.edit_profile.EditProfileScreen
import com.example.greeniepam.feature.profile.ui.favorites.FavoritesScreen
import com.example.greeniepam.feature.pustaka.ui.detail.PustakaDetailScreen
import com.example.greeniepam.ui.DetailScreen
import com.example.greeniepam.ui.MainScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    sessionViewModel: SessionViewModel = koinViewModel(),
) {
    val session by sessionViewModel.state.collectAsStateWithLifecycle()

    if (session == SessionState.Loading) {
        // Sesi tersimpan sedang dimuat — tampilkan latar kosong sebentar
        Box(Modifier.fillMaxSize().greenieCanvas())
        return
    }

    val startDestination: Screen = remember { if (session == SessionState.LoggedIn) Screen.Home else Screen.Auth }

    // Pindah otomatis saat user login atau keluar
    LaunchedEffect(session) {
        val onAuthScreen = navController.currentDestination?.hasRoute<Screen.Auth>() == true
        when {
            session == SessionState.LoggedIn && onAuthScreen ->
                navController.navigate(Screen.Home) { popUpTo(navController.graph.id) { inclusive = true } }
            session == SessionState.LoggedOut && !onAuthScreen ->
                navController.navigate(Screen.Auth) { popUpTo(navController.graph.id) { inclusive = true } }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.Auth> {
            AuthScreen()
        }
        composable<Screen.Home> {
            MainScreen(
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.Detail(id))
                },
                onNavigateToDetailPost = { postId ->
                    navController.navigate(Screen.DetailPost(postId))
                },
                onNavigateToEditProfile = {
                    navController.navigate(Screen.EditProfile)
                },
                onNavigateToFavorites = {
                    navController.navigate(Screen.Favorites)
                },
                onNavigateToPustakaDetail = { plantId ->
                    navController.navigate(Screen.PustakaDetail(plantId))
                }
            )
        }
        composable<Screen.Favorites> {
            FavoritesScreen(
                onBack = { navController.popBackStack() },
                onPostClick = { postId -> navController.navigate(Screen.DetailPost(postId.toString())) }
            )
        }
        composable<Screen.PustakaDetail> { backStackEntry ->
            val detail: Screen.PustakaDetail = backStackEntry.toRoute()
            PustakaDetailScreen(
                plantId = detail.plantId,
                onBack = { navController.popBackStack() }
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
        composable<Screen.EditProfile> {
            EditProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
