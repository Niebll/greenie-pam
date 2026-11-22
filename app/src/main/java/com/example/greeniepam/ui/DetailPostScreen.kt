package com.example.greeniepam.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.greeniepam.core.theme.GreenieGradients
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.core.theme.greenieCanvas

data class CommentItem(
    val id: String,
    val authorName: String,
    val avatarInitial: String,
    val avatarBgColor: Color,
    val text: String
)

private val initialComments = listOf(
    CommentItem("1", "Raka", "R", Color(0xFF3E8C5A), "Cantik banget daunya!"),
    CommentItem("2", "Salsa", "S", Color(0xFFB58A3C), "Pakai pupuk apa, Kak?")
)

@Composable
fun DetailPostScreen(
    postId: String = "post-1",
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var isLiked by remember { mutableStateOf(false) }
    var likesCount by remember { mutableIntStateOf(125) }
    var newCommentText by remember { mutableStateOf("") }
    val comments = remember { mutableStateListOf(*initialComments.toTypedArray()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .greenieCanvas()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = GreenieTheme.spacing.screenHorizontal, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Kembali",
                        tint = GreenieTheme.colors.icon.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = "Postingan",
                    style = GreenieTheme.typography.title17,
                    color = GreenieTheme.colors.text.primary
                )

                // More Options button
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .clickable {
                            Toast.makeText(context, "Opsi postingan", Toast.LENGTH_SHORT).show()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MoreHoriz,
                        contentDescription = "Lainnya",
                        tint = GreenieTheme.colors.icon.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Scrollable Post Details & Comments
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = GreenieTheme.spacing.screenHorizontal)
            ) {
                Spacer(modifier = Modifier.height(GreenieTheme.spacing.space8))

                // Author Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar Circle
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3E8C5A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "D",
                            style = GreenieTheme.typography.title15,
                            color = GreenieTheme.colors.text.primary
                        )
                    }

                    Spacer(modifier = Modifier.width(GreenieTheme.spacing.space12))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Dinda",
                            style = GreenieTheme.typography.title15,
                            color = GreenieTheme.colors.text.primary
                        )
                        Text(
                            text = "Monstera · 45 menit lalu",
                            style = GreenieTheme.typography.body12,
                            color = GreenieTheme.colors.text.secondary
                        )
                    }

                    // "Postinganmu" Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(GreenieTheme.radius.full))
                            .background(GreenieTheme.colors.surface.accentSoft)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Postinganmu",
                            style = GreenieTheme.typography.label11,
                            color = GreenieTheme.colors.text.accent
                        )
                    }
                }

                Spacer(modifier = Modifier.height(GreenieTheme.spacing.space16))

                // Post Image Banner Box
                val imageShape = RoundedCornerShape(24.dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(imageShape)
                        .background(GreenieGradients.cardGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Spa,
                        contentDescription = null,
                        tint = GreenieTheme.colors.icon.primary.copy(alpha = 0.6f),
                        modifier = Modifier.size(72.dp)
                    )
                }

                Spacer(modifier = Modifier.height(GreenieTheme.spacing.space16))

                // Likes & Comments Stats Bar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space20)
                ) {
                    // Likes
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            isLiked = !isLiked
                            if (isLiked) likesCount++ else likesCount--
                        }
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Suka",
                            tint = if (isLiked) GreenieTheme.colors.text.accent else GreenieTheme.colors.icon.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$likesCount",
                            style = GreenieTheme.typography.title14,
                            color = GreenieTheme.colors.text.primary
                        )
                    }

                    // Comments Count
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = "Komentar",
                            tint = GreenieTheme.colors.icon.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${22 + comments.size}",
                            style = GreenieTheme.typography.title14,
                            color = GreenieTheme.colors.text.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(GreenieTheme.spacing.space16))

                // Journal Content Card
                val cardShape = RoundedCornerShape(GreenieTheme.radius.radius20)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(cardShape)
                        .background(GreenieTheme.colors.surface.card)
                        .border(1.dp, GreenieTheme.colors.border.subtle, cardShape)
                        .padding(GreenieTheme.spacing.space16)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Monstera",
                            style = GreenieTheme.typography.heading18,
                            color = GreenieTheme.colors.text.primary
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(GreenieTheme.radius.full))
                                .background(GreenieTheme.colors.surface.accentSoft)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Tinggi 32 cm · Daun segar",
                                style = GreenieTheme.typography.label11,
                                color = GreenieTheme.colors.text.accent
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(GreenieTheme.spacing.space8))

                    Text(
                        text = "Daun baru akhirnya membuka. Pelan tapi pasti!",
                        style = GreenieTheme.typography.body13Paragraph,
                        color = GreenieTheme.colors.text.primary
                    )

                    Spacer(modifier = Modifier.height(GreenieTheme.spacing.space8))

                    Text(
                        text = "Dari Jurnal Pertumbuhan · 1 Nov 2026",
                        style = GreenieTheme.typography.body12,
                        color = GreenieTheme.colors.text.secondary
                    )
                }

                Spacer(modifier = Modifier.height(GreenieTheme.spacing.space24))

                // Comments Header
                Text(
                    text = "Komentar (${22 + comments.size})",
                    style = GreenieTheme.typography.title14,
                    color = GreenieTheme.colors.text.primary
                )

                Spacer(modifier = Modifier.height(GreenieTheme.spacing.space12))

                // Comments List
                comments.forEach { comment ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = GreenieTheme.spacing.space6),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(comment.avatarBgColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = comment.avatarInitial,
                                style = GreenieTheme.typography.title13,
                                color = GreenieTheme.colors.text.primary
                            )
                        }

                        Spacer(modifier = Modifier.width(GreenieTheme.spacing.space12))

                        Column {
                            Text(
                                text = comment.authorName,
                                style = GreenieTheme.typography.title13,
                                color = GreenieTheme.colors.text.primary
                            )
                            Text(
                                text = comment.text,
                                style = GreenieTheme.typography.body13,
                                color = GreenieTheme.colors.text.secondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(GreenieTheme.spacing.space24))
            }

            // Bottom Comment Input Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GreenieTheme.colors.background.canvas)
                    .padding(horizontal = GreenieTheme.spacing.screenHorizontal, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = newCommentText,
                        onValueChange = { newCommentText = it },
                        placeholder = {
                            Text(
                                text = "Tulis komentar...",
                                style = GreenieTheme.typography.body14,
                                color = GreenieTheme.colors.text.placeholder
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(GreenieTheme.radius.full),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = GreenieTheme.colors.surface.card,
                            unfocusedContainerColor = GreenieTheme.colors.surface.card,
                            focusedBorderColor = GreenieTheme.colors.border.accent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = GreenieTheme.colors.text.primary,
                            unfocusedTextColor = GreenieTheme.colors.text.primary
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(GreenieTheme.spacing.space8))

                    // Send Button
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(GreenieTheme.colors.surface.accent)
                            .clickable {
                                if (newCommentText.isNotBlank()) {
                                    comments.add(
                                        CommentItem(
                                            id = System.currentTimeMillis().toString(),
                                            authorName = "Kamu",
                                            avatarInitial = "Y",
                                            avatarBgColor = Color(0xFF5E9A62),
                                            text = newCommentText
                                        )
                                    )
                                    newCommentText = ""
                                    Toast.makeText(context, "Komentar terkirim", Toast.LENGTH_SHORT).show()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Send,
                            contentDescription = "Kirim",
                            tint = GreenieTheme.colors.text.onAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
