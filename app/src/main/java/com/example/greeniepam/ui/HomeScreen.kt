package com.example.greeniepam.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.greeniepam.core.theme.GreenieGradients
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.core.theme.greenieCanvas

data class CareTask(
    val id: String,
    val title: String,
    val timeOrStatus: String,
    val isCompleted: Boolean
)

data class FeedPost(
    val id: String,
    val authorName: String,
    val timeAgo: String,
    val plantTitle: String,
    val tagText: String,
    val caption: String,
    val likes: Int,
    val comments: Int,
    val bookmarks: Int
)

private val initialCareTasks = listOf(
    CareTask("1", "Siram Monstera", "08.00", false),
    CareTask("2", "Pupuk Pothos", "10.00", false),
    CareTask("3", "Siram Lidah mertua", "Selesai", true)
)

private val initialFeedPosts = listOf(
    FeedPost(
        id = "post-1",
        authorName = "Dinda",
        timeAgo = "45 menit lalu",
        plantTitle = "Monstera",
        tagText = "Tinggi 32 cm · Daun segar",
        caption = "Daun baru akhirnya membuka. Pelan tapi pasti!",
        likes = 125,
        comments = 24,
        bookmarks = 8
    )
)

private val berandaTabs = listOf("Populer", "Terbaru", "Mengikuti")

@Composable
fun HomeScreen(
    journalList: List<JournalEntry> = emptyList(),
    onNavigateToDetail: (String) -> Unit = {},
    onNavigateToDetailPost: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("Populer") }
    val careTasks = remember { mutableStateListOf(*initialCareTasks.toTypedArray()) }

    val completedCount = careTasks.count { it.isCompleted }

    val allFeedPosts = remember(journalList) {
        val userPosts = journalList.filter { it.isSharedToFeed }.map { entry ->
            FeedPost(
                id = entry.id,
                authorName = "Dinda",
                timeAgo = entry.date,
                plantTitle = entry.plantName,
                tagText = "Tinggi ${entry.height} · ${entry.condition}",
                caption = entry.notes.ifBlank { "Perkembangan tanaman hari ini." },
                likes = 0,
                comments = 0,
                bookmarks = 0
            )
        }
        userPosts + initialFeedPosts
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .greenieCanvas(),
        contentPadding = PaddingValues(
            top = GreenieTheme.spacing.screenTop,
            bottom = 100.dp
        )
    ) {
        // Top Header
        item {
            Column(
                modifier = Modifier.padding(horizontal = GreenieTheme.spacing.screenHorizontal)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile & Greeting (with weight(1f, fill=false) to prevent squeezing action buttons)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Box(
                            modifier = Modifier
                                .requiredSize(50.dp)
                                .clip(CircleShape)
                                .border(2.dp, GreenieTheme.colors.border.accent, CircleShape)
                                .background(GreenieGradients.cardGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "D",
                                style = GreenieTheme.typography.title15,
                                color = GreenieTheme.colors.text.primary
                            )
                        }

                        Spacer(modifier = Modifier.width(GreenieTheme.spacing.space12))

                        Column {
                            Text(
                                text = "Halo, Dinda",
                                style = GreenieTheme.typography.heading20,
                                color = GreenieTheme.colors.text.primary
                            )
                            Text(
                                text = "Sudah siram tanamanmu hari ini?",
                                style = GreenieTheme.typography.body13,
                                color = GreenieTheme.colors.text.secondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Action Icons (Notification & Search) - using requiredSize to ensure exact circles on small screens
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TopActionButton(
                            icon = Icons.Outlined.Notifications,
                            contentDescription = "Notifikasi",
                            onClick = {
                                Toast.makeText(context, "Tidak ada notifikasi baru", Toast.LENGTH_SHORT).show()
                            }
                        )

                        TopActionButton(
                            icon = Icons.Outlined.Search,
                            contentDescription = "Cari",
                            onClick = {
                                Toast.makeText(context, "Pencarian", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(GreenieTheme.spacing.space20))

                // Perawatan Hari Ini Card
                val careCardShape = RoundedCornerShape(24.dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(careCardShape)
                        .background(GreenieTheme.colors.surface.card)
                        .border(1.dp, GreenieTheme.colors.border.subtle, careCardShape)
                        .padding(GreenieTheme.spacing.space16)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Perawatan hari ini",
                            style = GreenieTheme.typography.title14,
                            color = GreenieTheme.colors.text.primary
                        )
                        Text(
                            text = "$completedCount dari ${careTasks.size} selesai",
                            style = GreenieTheme.typography.body12,
                            color = GreenieTheme.colors.text.accent
                        )
                    }

                    Spacer(modifier = Modifier.height(GreenieTheme.spacing.space12))

                    careTasks.forEachIndexed { index, task ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    careTasks[index] = task.copy(isCompleted = !task.isCompleted)
                                }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (task.isCompleted) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (task.isCompleted) GreenieTheme.colors.icon.success else GreenieTheme.colors.icon.default,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(GreenieTheme.spacing.space12))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = task.title,
                                    style = GreenieTheme.typography.title14,
                                    color = if (task.isCompleted) GreenieTheme.colors.text.secondary else GreenieTheme.colors.text.primary
                                )
                                Text(
                                    text = if (task.isCompleted) "Selesai" else task.timeOrStatus,
                                    style = GreenieTheme.typography.body12,
                                    color = GreenieTheme.colors.text.secondary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(GreenieTheme.spacing.space20))

                // Feed Tabs (Populer, Terbaru, Mengikuti)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space24),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    berandaTabs.forEach { tab ->
                        val isSelected = tab == selectedTab
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { selectedTab = tab }
                                .padding(vertical = GreenieTheme.spacing.space4)
                        ) {
                            Text(
                                text = tab,
                                style = if (isSelected) GreenieTheme.typography.title15 else GreenieTheme.typography.body13Medium,
                                color = if (isSelected) GreenieTheme.colors.text.accent else GreenieTheme.colors.text.secondary
                            )
                            Spacer(modifier = Modifier.height(GreenieTheme.spacing.space4))
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .width(36.dp)
                                        .height(3.dp)
                                        .clip(RoundedCornerShape(GreenieTheme.radius.full))
                                        .background(GreenieTheme.colors.surface.accent)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(GreenieTheme.spacing.space16))
            }
        }

        // Feed Post Cards List
        items(allFeedPosts, key = { it.id }) { post ->
            FeedPostCard(
                post = post,
                onClick = { onNavigateToDetailPost(post.id) },
                modifier = Modifier.padding(
                    horizontal = GreenieTheme.spacing.screenHorizontal,
                    vertical = GreenieTheme.spacing.space8
                )
            )
        }
    }
}

@Composable
private fun TopActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .requiredSize(42.dp)
            .clip(CircleShape)
            .background(GreenieTheme.colors.surface.card)
            .border(1.dp, GreenieTheme.colors.border.subtle, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = GreenieTheme.colors.icon.primary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun FeedPostCard(
    post: FeedPost,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(24.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(GreenieTheme.colors.surface.card)
            .border(1.dp, GreenieTheme.colors.border.subtle, cardShape)
            .clickable(onClick = onClick)
            .padding(GreenieTheme.spacing.space16)
    ) {
        // Author Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(GreenieTheme.colors.surface.brand),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.authorName.take(1),
                        style = GreenieTheme.typography.title13,
                        color = GreenieTheme.colors.text.primary
                    )
                }
                Spacer(modifier = Modifier.width(GreenieTheme.spacing.space10))
                Column {
                    Text(
                        text = post.authorName,
                        style = GreenieTheme.typography.title14,
                        color = GreenieTheme.colors.text.primary
                    )
                    Text(
                        text = post.timeAgo,
                        style = GreenieTheme.typography.body12,
                        color = GreenieTheme.colors.text.secondary
                    )
                }
            }

            Icon(
                imageVector = Icons.Outlined.MoreHoriz,
                contentDescription = "Lainnya",
                tint = GreenieTheme.colors.icon.default
            )
        }

        Spacer(modifier = Modifier.height(GreenieTheme.spacing.space12))

        // Post Banner Image Box with Tag
        val bannerShape = RoundedCornerShape(18.dp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(bannerShape)
                .background(GreenieGradients.cardGreen),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Spa,
                contentDescription = null,
                tint = GreenieTheme.colors.icon.primary.copy(alpha = 0.6f),
                modifier = Modifier.size(64.dp)
            )

            // Tag badge at bottom-left of image
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(GreenieTheme.radius.full))
                    .background(GreenieTheme.colors.surface.card)
                    .border(1.dp, GreenieTheme.colors.border.subtle, RoundedCornerShape(GreenieTheme.radius.full))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = post.tagText,
                    style = GreenieTheme.typography.label11,
                    color = GreenieTheme.colors.text.accent
                )
            }
        }

        Spacer(modifier = Modifier.height(GreenieTheme.spacing.space12))

        // Caption text
        Text(
            text = post.caption,
            style = GreenieTheme.typography.body13Paragraph,
            color = GreenieTheme.colors.text.primary
        )

        Spacer(modifier = Modifier.height(GreenieTheme.spacing.space16))

        // Footer Stats (Likes, Comments, Bookmarks)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space20)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Suka",
                        tint = GreenieTheme.colors.icon.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${post.likes}",
                        style = GreenieTheme.typography.title13,
                        color = GreenieTheme.colors.text.primary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Komen",
                        tint = GreenieTheme.colors.icon.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${post.comments}",
                        style = GreenieTheme.typography.title13,
                        color = GreenieTheme.colors.text.primary
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.BookmarkBorder,
                    contentDescription = "Simpan",
                    tint = GreenieTheme.colors.icon.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${post.bookmarks}",
                    style = GreenieTheme.typography.title13,
                    color = GreenieTheme.colors.text.primary
                )
            }
        }
    )
}
