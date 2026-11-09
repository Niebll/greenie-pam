package com.example.greeniepam.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.greeniepam.core.navbar.GreenieNavbar
import com.example.greeniepam.core.navbar.NavbarItem
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.core.theme.greenieCanvas
import com.example.greeniepam.data.JournalRepository

@Composable
fun MainScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToDetailPost: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(NavbarItem.Beranda) }
    var showBuatJurnal by remember { mutableStateOf(false) }
    val journalEntries = JournalRepository.journals

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
                    onSaveSuccess = { newJournal ->
                        JournalRepository.addJournal(newJournal)
                        showBuatJurnal = false
                        selectedTab = NavbarItem.Koleksi
                    }
                )
            } else {
                when (selectedTab) {
                    NavbarItem.Beranda -> HomeScreen(
                        journalList = journalEntries,
                        onNavigateToDetail = onNavigateToDetail,
                        onNavigateToDetailPost = onNavigateToDetailPost
                    )
                    NavbarItem.Pustaka -> PlaceholderScreen(title = "Halaman Pustaka")
                    NavbarItem.Jadwal -> PlaceholderScreen(title = "Halaman Jadwal")
                    NavbarItem.Koleksi -> KoleksiScreen(
                        journalList = journalEntries,
                        onNavigateToDetail = onNavigateToDetail,
                        onNavigateToDetailPost = onNavigateToDetailPost
                    )
                }
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = GreenieTheme.typography.heading20,
            color = GreenieTheme.colors.text.primary
        )
    }
}
