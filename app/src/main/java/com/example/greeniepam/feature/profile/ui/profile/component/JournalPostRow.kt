package com.example.greeniepam.feature.profile.ui.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greeniepam.core.model.JournalPost
import com.example.greeniepam.core.theme.GreenieTheme

/**
 * Kartu ringkas satu postingan jurnal.
 * Dipakai di tab "Dibagikan" dan halaman Favorit.
 *
 * @param showAuthor tampilkan nama penulis (untuk Favorit, karena bisa milik orang lain)
 */
@Composable
fun JournalPostRow(
    post: JournalPost,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showAuthor: Boolean = false,
) {
    val shape = RoundedCornerShape(GreenieTheme.radius.radius20)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(GreenieTheme.colors.surface.card)
            .border(1.dp, GreenieTheme.colors.border.subtle, shape)
            .clickable(onClick = onClick)
            .padding(GreenieTheme.spacing.space16),
    ) {
        if (showAuthor) {
            Text(
                text = post.authorName,
                style = GreenieTheme.typography.label12,
                color = GreenieTheme.colors.text.accent,
            )
            Spacer(Modifier.height(GreenieTheme.spacing.space4))
        }
        Text(
            text = "${post.plantName} · ${post.journalDate}",
            style = GreenieTheme.typography.title14,
            color = GreenieTheme.colors.text.primary,
        )
        if (post.notes.isNotBlank()) {
            Spacer(Modifier.height(GreenieTheme.spacing.space4))
            Text(
                text = post.notes,
                style = GreenieTheme.typography.body13,
                color = GreenieTheme.colors.text.secondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(Modifier.height(GreenieTheme.spacing.space8))
        Text(
            text = "${post.likeCount} suka · ${post.commentCount} komentar",
            style = GreenieTheme.typography.body11,
            color = GreenieTheme.colors.text.accent,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0C1A12)
@Composable
private fun JournalPostRowPreview() {
    GreenieTheme {
        JournalPostRow(
            post = JournalPost(
                id = 1, authorId = "a", authorName = "Raka", plantName = "Monstera",
                journalDate = "1 Nov 2026", height = "32 cm", notes = "Daun baru akhirnya membuka.",
                condition = "Segar", isShared = true, createdAt = "", likeCount = 12,
                isLiked = false, commentCount = 3, isMine = false, isFavorited = true,
            ),
            onClick = {},
            showAuthor = true,
        )
    }
}
