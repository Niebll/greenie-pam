package com.example.greeniepam.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.greeniepam.core.theme.GreenieTheme

@Composable
fun HomeScreen(
    onNavigateToDetail: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selamat Datang di Greenie Pam!",
            style = GreenieTheme.typography.heading20,
            color = GreenieTheme.colors.text.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        AsyncImage(
            model = "https://picsum.photos/200",
            contentDescription = "Sample Image from Coil",
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onNavigateToDetail("item-123") },
            colors = ButtonDefaults.buttonColors(
                containerColor = GreenieTheme.colors.surface.raised,
                contentColor = GreenieTheme.colors.text.primary
            )
        ) {
            Text(text = "Buka Detail Item", style = GreenieTheme.typography.title15)
        }
    }
}
