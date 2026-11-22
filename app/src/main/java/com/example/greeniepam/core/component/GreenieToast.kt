package com.example.greeniepam.core.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greeniepam.core.theme.GreenieTheme
import kotlinx.coroutines.delay

/** Isi toast: judul tebal + keterangan, status sukses/gagal. */
data class GreenieToastData(
    val title: String,
    val message: String,
    val isSuccess: Boolean = true,
)

/**
 * Toast Notifikasi (Figma › komponen "Toast Notifikasi").
 * Pop up di atas layar setelah aksi tombol, hilang otomatis setelah 3 detik.
 * Taruh di dalam Box layar dengan `Modifier.align(Alignment.TopCenter)`.
 */
@Composable
fun GreenieToastHost(
    toast: GreenieToastData?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(toast) {
        if (toast != null) {
            delay(3_000)
            onDismiss()
        }
    }
    AnimatedVisibility(
        visible = toast != null,
        enter = fadeIn() + slideInVertically { -it },
        exit = fadeOut() + slideOutVertically { -it },
        modifier = modifier,
    ) {
        // Simpan isi terakhir supaya animasi keluar tetap menampilkan teks
        toast?.let { GreenieToast(data = it, onDismiss = onDismiss) }
    }
}

@Composable
fun GreenieToast(
    data: GreenieToastData,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(GreenieTheme.radius.radius20)
    val colors = GreenieTheme.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.surface.sheet)
            .border(1.dp, if (data.isSuccess) colors.border.successSoft else colors.border.dangerSoft, shape)
            .padding(horizontal = GreenieTheme.spacing.space14, vertical = GreenieTheme.spacing.space12),
        horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp) // ukuran ikon status dari Figma
                .clip(RoundedCornerShape(GreenieTheme.radius.radius18))
                .background(if (data.isSuccess) colors.surface.successSoft else colors.surface.dangerSoft),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = if (data.isSuccess) Icons.Outlined.Check else Icons.Outlined.Close,
                contentDescription = null,
                tint = if (data.isSuccess) colors.icon.success else colors.icon.danger,
                modifier = Modifier.size(18.dp),
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space2),
        ) {
            Text(text = data.title, style = GreenieTheme.typography.title14, color = colors.text.primary)
            Text(text = data.message, style = GreenieTheme.typography.body12, color = colors.text.secondary)
        }
        Box(
            modifier = Modifier
                .size(44.dp) // target sentuh minimal untuk ikon tutup 16dp
                .clip(CircleShape)
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Tutup",
                tint = colors.icon.default,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0C1A12)
@Composable
private fun GreenieToastPreview() {
    GreenieTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            GreenieToast(
                data = GreenieToastData("Ditambahkan ke favorit", "Monstera tersimpan di tab Favorit profilmu."),
                onDismiss = {},
            )
            GreenieToast(
                data = GreenieToastData("Gagal menyimpan", "Tidak ada koneksi internet.", isSuccess = false),
                onDismiss = {},
            )
        }
    }
}
