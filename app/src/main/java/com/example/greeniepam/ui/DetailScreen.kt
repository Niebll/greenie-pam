package com.example.greeniepam.ui

import androidx.compose.runtime.Composable
import com.example.greeniepam.feature.detail_post.ui.DetailPostScreen

@Composable
fun DetailScreen(
    itemId: String,
    onBack: () -> Unit
) {
    DetailPostScreen(
        itemId = itemId,
        onBack = onBack
    )
}
