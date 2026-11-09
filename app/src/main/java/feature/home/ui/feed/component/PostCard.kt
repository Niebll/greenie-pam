package com.example.greeniepam.feature.home.ui.feed.component

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.feature.home.data.model.Post

private val CardGreenGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF388E59),
        Color(0xFF163E27),
        Color(0xFF0F2B1B),
    )
)

private val CardGoldGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFB58734),
        Color(0xFF5E4216),
        Color(0xFF38260B),
    )
)

/**
 * Kartu postingan di feed Home.
 * Menampilkan kartu full-bleed gradient dengan avatar tag di kiri atas,
 * tombol opsi di kanan atas, ikon daun di tengah, serta tag & caption di bawah.
 */
@Composable
fun PostCard(
    post: Post,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    onLikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
) {
    val gradient = if (post.isGold) CardGoldGradient else CardGreenGradient

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(380.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(gradient)
            .clickable(onClick = onClick)
            .padding(16.dp),
    ) {
        // ── Top Row: Tag Penulis (Kiri) & Tombol Opsi (Kanan) ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Tag Penulis
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0x33FFFFFF))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val initial = post.authorName.firstOrNull()?.uppercase().orEmpty()
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FFFFFF)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = initial,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }

                Spacer(Modifier.width(8.dp))

                Column {
                    Text(
                        text = post.authorName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                    Text(
                        text = post.timeAgo,
                        fontSize = 11.sp,
                        color = Color(0xCCFFFFFF),
                    )
                }

                Spacer(Modifier.width(6.dp))
            }

            // Tombol Opsi (3 titik)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFFFFF))
                    .clickable(onClick = onMoreClick),
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

        // ── Tengah: Placeholder Ikon Daun Besar ──
        Icon(
            imageVector = Icons.Outlined.Eco,
            contentDescription = null,
            tint = Color(0x33FFFFFF),
            modifier = Modifier
                .size(76.dp)
                .align(Alignment.Center),
        )

        // ── Bawah: Tag Info, Caption, & Jumlah Like/Komentar ──
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart),
        ) {
            // Tag Info (Tinggi & Kondisi Daun)
            val tagText = listOfNotNull(post.plantHeight, post.leafCondition).joinToString(" · ")
            if (tagText.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x3D000000))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                ) {
                    Text(
                        text = tagText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFE5C16C),
                    )
                }
                Spacer(Modifier.height(10.dp))
            }

            // Caption
            Text(
                text = post.caption,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                lineHeight = 20.sp,
            )

            Spacer(Modifier.height(12.dp))

            // Akses Interaksi (Like & Comment)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.clickable(onClick = onLikeClick),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Suka",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        text = post.likeCount.toString(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.clickable(onClick = onCommentClick),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Komentar",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        text = post.commentCount.toString(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
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
private fun PostCardGreenPreview() {
    GreenieTheme {
        PostCard(
            post = Post(
                id = 1,
                authorName = "Dinda",
                timeAgo = "45 menit lalu",
                plantHeight = "Tinggi 32 cm",
                leafCondition = "Daun segar",
                caption = "Daun baru akhirnya membuka. Pelan tapi pasti!",
                likeCount = 125,
                commentCount = 24,
                isGold = false,
            ),
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0C1A12)
@Composable
private fun PostCardGoldPreview() {
    GreenieTheme {
        PostCard(
            post = Post(
                id = 2,
                authorName = "Raka",
                timeAgo = "1 jam lalu",
                caption = "Tanaman monstera makin lebat.",
                likeCount = 89,
                commentCount = 12,
                isGold = true,
            ),
            modifier = Modifier.padding(16.dp),
        )
    }
}
