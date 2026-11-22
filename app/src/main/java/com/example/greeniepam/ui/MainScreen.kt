package com.example.greeniepam.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.greeniepam.core.navbar.GreenieNavbar
import com.example.greeniepam.core.navbar.NavbarItem
import com.example.greeniepam.core.theme.greenieCanvas
import com.example.greeniepam.feature.home.ui.feed.HomeScreen as HomeFeedScreen
import com.example.greeniepam.feature.journal.ui.create.BuatJurnalScreen
import com.example.greeniepam.feature.profile.ui.profile.ProfileScreen
import com.example.greeniepam.feature.pustaka.ui.list.PustakaScreen
import com.example.greeniepam.feature.schedule.ui.ScheduleScreen
import com.example.greeniepam.feature.schedule.ui.ScheduleViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToDetailPost: (String) -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {},
    onNavigateToPustakaDetail: (Long) -> Unit = {},
) {
    // rememberSaveable: tab tetap sama saat kembali dari halaman detail
    var selectedTab by rememberSaveable { mutableStateOf(NavbarItem.Beranda) }
    var showBuatJurnal by rememberSaveable { mutableStateOf(false) }
    val scheduleViewModel: ScheduleViewModel = koinViewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                GreenieNavbar(
                    selected = if (showBuatJurnal) null else selectedTab,
                    isJournalActive = showBuatJurnal,
                    onItemClick = {
                        showBuatJurnal = false
                        selectedTab = it
                    },
                    onJournalClick = {
                        showBuatJurnal = true
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .greenieCanvas()
                .padding(innerPadding)
        ) {
            if (showBuatJurnal) {
                BuatJurnalScreen(
                    onSaveSuccess = {
                        showBuatJurnal = false
                        // Jurnal yang dibagikan muncul di Beranda dan tab "Dibagikan" di profil
                        selectedTab = NavbarItem.Beranda
                    }
                )
            } else {
                when (selectedTab) {
                    NavbarItem.Beranda -> HomeFeedScreen(
                        onPostClick = { postId ->
                            onNavigateToDetailPost(postId.toString())
                        }
                    )
                    NavbarItem.Pustaka -> PustakaScreen(onPlantClick = onNavigateToPustakaDetail)
                    NavbarItem.Jadwal -> ScheduleScreen(viewModel = scheduleViewModel)
                    NavbarItem.Koleksi -> ProfileScreen(
                        onEditProfileClick = onNavigateToEditProfile,
                        onFavoritesClick = onNavigateToFavorites,
                        onPostClick = { postId -> onNavigateToDetailPost(postId.toString()) },
                    )
                }
            }
        }
    }
}
