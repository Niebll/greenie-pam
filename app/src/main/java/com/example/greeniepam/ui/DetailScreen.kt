package com.example.greeniepam.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.greeniepam.core.theme.GreenieTheme

@Composable
fun DetailScreen(
    itemId: String,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Halaman Detail",
            style = GreenieTheme.typography.heading20,
            color = GreenieTheme.colors.text.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "ID Item: $itemId",
            style = GreenieTheme.typography.body14Medium,
            color = GreenieTheme.colors.text.secondary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(
                containerColor = GreenieTheme.colors.surface.raised,
                contentColor = GreenieTheme.colors.text.primary
            )
        ) {
            Text(text = "Kembali", style = GreenieTheme.typography.title15)
        }
    }
}
