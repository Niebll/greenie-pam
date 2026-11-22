package com.example.greeniepam.feature.profile.ui.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greeniepam.core.theme.GreenieTheme

@Composable
fun ProfileBioCard(
    plantCount: Int,
    sharedCount: Int,
    likeCount: Int,
    bio: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = GreenieTheme.spacing.screenHorizontal)
            .clip(RoundedCornerShape(GreenieTheme.radius.radius20))
            .background(GreenieTheme.colors.surface.card)
            .border(1.dp, GreenieTheme.colors.border.subtle, RoundedCornerShape(GreenieTheme.radius.radius20))
            .padding(GreenieTheme.spacing.space16),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StatItem(count = plantCount.toString(), label = "Tanaman")
            StatItem(count = sharedCount.toString(), label = "Dibagikan")
            StatItem(count = likeCount.toString(), label = "Suka")
        }

        Spacer(Modifier.height(GreenieTheme.spacing.space12))

        HorizontalDivider(
            thickness = 1.dp,
            color = GreenieTheme.colors.border.subtle,
        )

        Spacer(Modifier.height(GreenieTheme.spacing.space12))

        Text(
            text = bio,
            style = GreenieTheme.typography.body12,
            color = GreenieTheme.colors.text.secondary,
            lineHeight = GreenieTheme.typography.body13MediumParagraph.lineHeight,
        )
    }
}

@Composable
private fun StatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            style = GreenieTheme.typography.heading18,
            fontWeight = FontWeight.Bold,
            color = GreenieTheme.colors.text.primary,
        )
        Text(
            text = label,
            style = GreenieTheme.typography.body11,
            color = GreenieTheme.colors.text.secondary,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0C1A12)
@Composable
private fun ProfileBioCardPreview() {
    GreenieTheme {
        ProfileBioCard(
            plantCount = 9,
            sharedCount = 6,
            likeCount = 240,
            bio = "Mahasiswa, pecinta tanaman hias.\nKoleksi kecil di kamar kost.",
        )
    }
}
