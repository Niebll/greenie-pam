package com.example.greeniepam.feature.home.ui.feed.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greeniepam.core.theme.GreenieTheme

/**
 * Header layar Home.
 *
 * Menampilkan avatar + "Halo, Nama" + subtitle + ikon notifikasi & pencarian.
 */
@Composable
fun HomeHeader(
    userName: String,
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Avatar dengan inisial dan border emas
        val initial = userName.firstOrNull()?.uppercase().orEmpty()

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF1E4631))
                .border(1.5.dp, Color(0xFFC7A75C), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = initial,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }

        Spacer(Modifier.width(12.dp))

        // Salam + subtitle
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Halo, $userName",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "Sudah siram tanamanmu hari ini?",
                fontSize = 13.sp,
                color = Color(0xFFA5B8AC),
            )
        }

        // Ikon notifikasi (Tombol Lingkaran Transparan)
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0x1FFFFFFF))
                .clickable(onClick = onNotificationClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifikasi",
                tint = Color.White,
                modifier = Modifier.size(20.dp),
            )
        }

        Spacer(Modifier.width(10.dp))

        // Ikon pencarian (Tombol Lingkaran Transparan)
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0x1FFFFFFF))
                .clickable(onClick = onSearchClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Cari",
                tint = Color.White,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

/* ---------- preview ---------- */

@Preview(showBackground = true, backgroundColor = 0xFF0C1A12)
@Composable
private fun HomeHeaderPreview() {
    GreenieTheme {
        HomeHeader(userName = "Dinda")
    }
}
