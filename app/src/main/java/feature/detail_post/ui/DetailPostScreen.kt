package com.example.greeniepam.feature.detail_post.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreHoriz
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.core.theme.greenieCanvas
import com.example.greeniepam.ui.JournalRepository
import kotlin.math.abs

private val DetailCardGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF388E59),
        Color(0xFF163E27),
        Color(0xFF0F2B1B),
    )
)

data class CommentItem(
    val id: Int,
    val authorName: String,
    val text: String,
    val isGoldAvatar: Boolean = false
)

@Composable
fun DetailPostScreen(
    itemId: String = "1",
    onBack: () -> Unit = {}
) {
    var commentText by remember { mutableStateOf("") }

    // Direct read of JournalRepository.journals enables Compose snapshot observation for recomposition on like/comment updates
    val userJournal = JournalRepository.journals.find { entry ->
        entry.id == itemId ||
                (itemId.toLongOrNull() != null && abs(entry.id.hashCode().toLong()) == itemId.toLongOrNull()) ||
                (entry.id == "post-1" && (itemId == "1" || itemId == "post-1"))
    }

    val plantName = userJournal?.plantName ?: "Monstera"
    val height = userJournal?.height.let { if (it.isNullOrBlank()) "32 cm" else it }
    val condition = userJournal?.condition ?: "Daun segar"
    val notes = userJournal?.notes?.ifBlank { "Daun baru akhirnya membuka. Pelan tapi pasti!" } ?: "Daun baru akhirnya membuka. Pelan tapi pasti!"
    val dateStr = userJournal?.date ?: "1 Nov 2026"
    val dateInfo = "Dari Jurnal Pertumbuhan · $dateStr"
    val currentLikes = userJournal?.likesCount ?: 125
    val currentIsLiked = userJournal?.isLiked ?: false

    val commentItems = userJournal?.comments?.mapIndexed { index, comment ->
        CommentItem(index + 1, comment.authorName, comment.text, isGoldAvatar = comment.authorName == "Kamu")
    } ?: listOf(
        CommentItem(1, "Raka", "Cantik banget daunnya!", isGoldAvatar = false),
        CommentItem(2, "Salsa", "Pakai pupuk apa, Kak?", isGoldAvatar = true)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .greenieCanvas()
    ) {
        // ── Top Navigation Bar ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Tombol Kembali (Kiri)
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0x1FFFFFFF))
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp),
                )
            }

            // Judul Halaman (Tengah)
            Text(
                text = "Postingan",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )

            // Tombol Opsi (Kanan)
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0x1FFFFFFF))
                    .clickable { },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.MoreHoriz,
                    contentDescription = "Opsi",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        // ── Konten Utama Berjalan (LazyColumn) ──
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(bottom = 12.dp),
        ) {
            // ── Info Penulis (Avatar + Nama + Tag "Postinganmu") ──
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3E8C5A)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "D",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Dinda",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                        Text(
                            text = "$plantName · $dateStr",
                            fontSize = 12.sp,
                            color = Color(0xFFA5B8AC),
                        )
                    }

                    // Tag "Postinganmu"
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x24C7A75C))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                    ) {
                        Text(
                            text = "Postinganmu",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFE5C16C),
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            // ── Area Foto Tanaman (Gradien Card Hijau) ──
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(DetailCardGradient),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Eco,
                        contentDescription = null,
                        tint = Color(0x33FFFFFF),
                        modifier = Modifier.size(72.dp),
                    )
                }
            }

            // ── Engagement (Like & Komentar Count) ──
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.clickable {
                            userJournal?.let {
                                JournalRepository.toggleLike(it.id)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (currentIsLiked) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Suka",
                            tint = if (currentIsLiked) Color(0xFFE5C16C) else Color.White,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text = "$currentLikes",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = "Komentar",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text = "${commentItems.size}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                    }
                }
            }

            // ── Kartu Deskripsi & Jurnal Info ──
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x1AFFFFFF))
                        .border(1.dp, Color(0x12FFFFFF), RoundedCornerShape(20.dp))
                        .padding(16.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = plantName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x3D000000))
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                        ) {
                            Text(
                                text = "Tinggi $height · $condition",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFFE5C16C),
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = notes,
                        fontSize = 14.sp,
                        color = Color.White,
                        lineHeight = 20.sp,
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = dateInfo,
                        fontSize = 12.sp,
                        color = Color(0xFFA5B8AC),
                    )
                }
            }

            // ── Judul Seksi Komentar ──
            item {
                Text(
                    text = "Komentar (${commentItems.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                )
            }

            // ── Daftar Komentar ──
            items(commentItems) { comment ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    val avatarBg = if (comment.isGoldAvatar) Color(0xFFB58A3C) else Color(0xFF3E8C5A)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(avatarBg),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = comment.authorName.first().toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column {
                        Text(
                            text = comment.authorName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = comment.text,
                            fontSize = 13.sp,
                            color = Color(0xFFD0DCD5),
                        )
                    }
                }
            }
        }

        // ── Input Komentar (Bawah) ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime))
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF12281D))
                    .border(1.dp, Color(0x33FFFFFF), CircleShape)
                    .padding(start = 20.dp, end = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = Color.White,
                    ),
                    cursorBrush = SolidColor(Color(0xFFE5C16C)),
                    decorationBox = { innerTextField ->
                        if (commentText.isEmpty()) {
                            Text(
                                text = "Tulis komentar...",
                                fontSize = 14.sp,
                                color = Color(0xFFA5B8AC),
                            )
                        }
                        innerTextField()
                    },
                )

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE5C16C))
                        .clickable {
                            if (commentText.isNotBlank()) {
                                userJournal?.let {
                                    JournalRepository.addComment(it.id, "Kamu", commentText)
                                }
                                commentText = ""
                            }
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Send,
                        contentDescription = "Kirim",
                        tint = Color(0xFF0F2418),
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}

/* ────────────────────────────────────────────────────────────────────────── */
/*  Preview                                                                  */
/* ────────────────────────────────────────────────────────────────────────── */

@Preview(showBackground = true, backgroundColor = 0xFF0C1A12)
@Composable
private fun DetailPostScreenPreview() {
    GreenieTheme {
        DetailPostScreen()
    }
}
