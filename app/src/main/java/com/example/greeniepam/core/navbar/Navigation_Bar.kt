package com.example.greeniepam.core.navbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.core.theme.greenieCanvas

/*
 * Navbar — Figma › 🌿 Design System › Komponen › Navbar
 * "Bottom navigation 4 menu + tombol Jurnal di tengah (aksi utama). Variant = menu yang aktif."
 *
 *  ┌──────────────────────────────────────────────────────────┐
 *  │ [Beranda] [Pustaka]  ( ✎ )  [Jadwal] [Koleksi]          │  ← pill, radius 34
 *  └──────────────────────────────────────────────────────────┘
 */

/**
 * 4 menu di navbar. Urutannya = urutan tampil (Jurnal ada di tengah, di antara Pustaka & Jadwal).
 * Ikon pakai Material Icons bawaan (butuh dependency material-icons-core).
 */
enum class NavbarItem(
    val label: String,
    val icon: ImageVector,
) {
    Beranda("Beranda", Icons.Outlined.Home),
    Pustaka("Pustaka", Icons.Outlined.Search),
    Jadwal("Jadwal", Icons.Outlined.DateRange),
    Koleksi("Koleksi", Icons.Outlined.Person),
}

/**
 * @param selected menu yang lagi aktif
 * @param onItemClick dipanggil waktu salah satu dari 4 menu diklik
 * @param onJournalClick dipanggil waktu tombol Jurnal (tengah) diklik
 */
@Composable
fun GreenieNavbar(
    selected: NavbarItem,
    onItemClick: (NavbarItem) -> Unit,
    onJournalClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = GreenieTheme.colors
    val radius = GreenieTheme.radius
    val spacing = GreenieTheme.spacing
    val navbarShape = RoundedCornerShape(radius.radius34)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(navbarShape)
            .background(colors.surface.navbar)
            .border(1.dp, colors.border.emphasis, navbarShape)
            .padding(spacing.space8)
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(spacing.space2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NavbarTab(NavbarItem.Beranda, selected, onItemClick)
        NavbarTab(NavbarItem.Pustaka, selected, onItemClick)
        JournalButton(onClick = onJournalClick)
        NavbarTab(NavbarItem.Jadwal, selected, onItemClick)
        NavbarTab(NavbarItem.Koleksi, selected, onItemClick)
    }
}

@Composable
private fun RowScope.NavbarTab(
    item: NavbarItem,
    selected: NavbarItem,
    onClick: (NavbarItem) -> Unit,
) {
    val colors = GreenieTheme.colors
    val isSelected = item == selected
    val contentColor = if (isSelected) colors.text.accent else colors.text.secondary
    val tabShape = RoundedCornerShape(GreenieTheme.radius.radius24)

    Column(
        modifier = Modifier
            .weight(1f)
            .height(52.dp)
            .clip(tabShape)
            .background(if (isSelected) colors.surface.raised else Color.Transparent)
            .selectable(
                selected = isSelected,
                onClick = { onClick(item) },
                role = Role.Tab,
            ),
        verticalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space2, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null, // label di bawahnya udah cukup buat screen reader
            tint = contentColor,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = item.label,
            style = GreenieTheme.typography.label10,
            color = contentColor,
            maxLines = 1,
        )
    }
}

/** Tombol Jurnal (aksi utama) — lingkaran putih 56dp di tengah navbar */
@Composable
private fun JournalButton(onClick: () -> Unit) {
    val colors = GreenieTheme.colors
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(colors.surface.white)
            .clickable(onClickLabel = "Tulis jurnal", role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = "Tulis jurnal",
            tint = colors.icon.onAccent,
            modifier = Modifier.size(24.dp),
        )
    }
}

// region Preview
@Preview(name = "Navbar — semua state", widthDp = 402, heightDp = 380)
@Composable
private fun GreenieNavbarAllStatesPreview() {
    GreenieTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .greenieCanvas()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            NavbarItem.entries.forEach { item ->
                GreenieNavbar(selected = item, onItemClick = {}, onJournalClick = {})
            }
        }
    }
}

@Preview(name = "Navbar — interaktif", widthDp = 402, heightDp = 120)
@Composable
private fun GreenieNavbarInteractivePreview() {
    var selected by remember { mutableStateOf(NavbarItem.Beranda) }
    GreenieTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .greenieCanvas()
                .padding(20.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            GreenieNavbar(selected = selected, onItemClick = { selected = it }, onJournalClick = {})
        }
    }
}
// endregion