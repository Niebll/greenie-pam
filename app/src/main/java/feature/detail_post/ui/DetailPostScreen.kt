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
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

private val DetailCardGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF388E59),
        Color(0xFF163E27),
        Color(0xFF0F2B1B),
    )
)

data class CommentItem(
    val id: Long,
    val authorName: String,
    val text: String,
    val isGoldAvatar: Boolean = false
)

@Composable
fun DetailPostScreen(
    itemId: String,
    onBack: () -> Unit = {},
    viewModel: DetailPostViewModel = koinViewModel(key = "detail-post-$itemId"),
) {
    LaunchedEffect(itemId) {
        itemId.toLongOrNull()?.let(viewModel::load)
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    DetailPostContent(
        state = state,
        onBack = onBack,
        onLikeClick = viewModel::toggleLike,
        onCommentTextChange = viewModel::onCommentTextChange,
        onSendComment = viewModel::sendComment,
    )
}

@Composable
private fun DetailPostContent(
    state: DetailPostUiState,
    onBack: () -> Unit,
    onLikeClick: () -> Unit,
    onCommentTextChange: (String) -> Unit,
    onSendComment: () -> Unit,
) {
    val post = state.post
    val commentText = state.commentText

    val authorName = post?.authorName.orEmpty()
    val plantName = post?.plantName.orEmpty()
    val height = post?.height.orEmpty()
    val condition = post?.condition.orEmpty()
    val notes = post?.notes?.ifBlank { "Perkembangan tanaman $plantName." }.orEmpty()
    val dateStr = post?.journalDate.orEmpty()
    val dateInfo = "Dari Jurnal Pertumbuhan · $dateStr"
    val currentLikes = post?.likeCount ?: 0
    val currentIsLiked = post?.isLiked ?: false
    val statusText = when {
        state.isLoading && post == null -> "Memuat postingan..."
        post == null -> state.error ?: "Postingan tidak ditemukan."
        else -> null
    }

    val commentItems = state.comments.map { comment ->
        CommentItem(comment.id, comment.authorName, comment.body, isGoldAvatar = comment.isMine)
    }

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
            if (statusText != null) {
                item {
                    Text(
                        text = statusText,
                        style = GreenieTheme.typography.body13,
                        color = GreenieTheme.colors.text.secondary,
                        modifier = Modifier.padding(GreenieTheme.spacing.screenHorizontal),
                    )
                }
                return@LazyColumn
            }

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
                            text = authorName.firstOrNull()?.uppercase().orEmpty(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = authorName,
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

                    // Tag "Postinganmu" (hanya untuk postingan sendiri)
                    if (post?.isMine == true) {
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
                        modifier = Modifier.clickable(onClick = onLikeClick)
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
                                text = listOfNotNull(
                                height.takeIf { it.isNotBlank() }?.let { "Tinggi $it" },
                                condition.takeIf { it.isNotBlank() },
                            ).joinToString(" · "),
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
                            text = comment.authorName.firstOrNull()?.uppercase().orEmpty(),
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
                    onValueChange = onCommentTextChange,
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
                        .clickable(enabled = !state.isSending && post != null, onClick = onSendComment),
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
        DetailPostContent(
            state = DetailPostUiState(
                isLoading = false,
                post = com.example.greeniepam.core.model.JournalPost(
                    id = 1, authorId = "a", authorName = "Dinda", plantName = "Monstera",
                    journalDate = "1 Nov 2026", height = "32 cm", notes = "Daun baru akhirnya membuka.",
                    condition = "Segar", isShared = true, createdAt = "", likeCount = 125,
                    isLiked = false, commentCount = 1, isMine = true,
                ),
                comments = listOf(
                    com.example.greeniepam.core.model.JournalComment(1, "Raka", "Cantik banget daunnya!", false),
                ),
            ),
            onBack = {},
            onLikeClick = {},
            onCommentTextChange = {},
            onSendComment = {},
        )
    }
}
