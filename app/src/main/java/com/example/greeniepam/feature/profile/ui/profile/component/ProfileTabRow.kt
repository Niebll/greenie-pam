package com.example.greeniepam.feature.profile.ui.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.feature.profile.ui.profile.ProfileTab

@Composable
fun ProfileTabRow(
    selectedTab: ProfileTab,
    onTabSelected: (ProfileTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = GreenieTheme.spacing.screenHorizontal, vertical = GreenieTheme.spacing.space12),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        ProfileTab.entries.forEach { tab ->
            TabItem(
                label = tab.label,
                isSelected = tab == selectedTab,
                onClick = { onTabSelected(tab) },
            )
        }
    }
}

@Composable
private fun TabItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = if (isSelected) GreenieTheme.typography.title15 else GreenieTheme.typography.title14,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) GreenieTheme.colors.text.accent else GreenieTheme.colors.text.secondary,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.5.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(GreenieTheme.colors.surface.accent),
            )
        } else {
            Spacer(Modifier.height(2.5.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0C1A12)
@Composable
private fun ProfileTabRowPreview() {
    GreenieTheme {
        ProfileTabRow(
            selectedTab = ProfileTab.Tanaman,
            onTabSelected = {},
        )
    }
}
