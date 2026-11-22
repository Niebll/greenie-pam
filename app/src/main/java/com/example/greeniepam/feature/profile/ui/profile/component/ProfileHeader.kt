package com.example.greeniepam.feature.profile.ui.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greeniepam.core.theme.GreenieTheme

/**
 * Header profil: Avatar besar + Nama + @username + Tombol "Edit profil" & "Bagikan"
 */
@Composable
fun ProfileHeader(
    userName: String,
    usernameHandle: String,
    modifier: Modifier = Modifier,
    onEditProfileClick: () -> Unit = {},
    onShareProfileClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = GreenieTheme.spacing.screenHorizontal),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Avatar besar dengan background gradien hijau dan ikon daun
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0xFF2C5E40)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Eco,
                contentDescription = null,
                tint = Color(0x66FFFFFF),
                modifier = Modifier.size(40.dp),
            )
        }

        Spacer(Modifier.width(GreenieTheme.spacing.space16))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = userName,
                style = GreenieTheme.typography.heading20,
                color = GreenieTheme.colors.text.primary,
            )
            Text(
                text = usernameHandle,
                style = GreenieTheme.typography.body12,
                color = GreenieTheme.colors.text.secondary,
            )

            Spacer(Modifier.height(GreenieTheme.spacing.space12))

            Row(horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space8)) {
                // Tombol "Edit profil" (Outline putih)
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(1.dp, GreenieTheme.colors.border.strong, CircleShape)
                        .clickable(onClick = onEditProfileClick)
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Edit profil",
                        style = GreenieTheme.typography.label12,
                        color = GreenieTheme.colors.text.primary,
                    )
                }

                // Tombol "Bagikan" (Outline emas)
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(1.dp, GreenieTheme.colors.border.accent, CircleShape)
                        .clickable(onClick = onShareProfileClick)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Bagikan",
                        style = GreenieTheme.typography.label12,
                        color = GreenieTheme.colors.text.accent,
                    )
                }
            }
        }
    }
}

/* ---------- preview ---------- */

@Preview(showBackground = true, backgroundColor = 0xFF0C1A12)
@Composable
private fun ProfileHeaderPreview() {
    GreenieTheme {
        ProfileHeader(userName = "Dinda", usernameHandle = "@dinda.greenie")
    }
}
