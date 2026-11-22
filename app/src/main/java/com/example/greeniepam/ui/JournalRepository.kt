package com.example.greeniepam.ui

import androidx.compose.runtime.mutableStateListOf
import java.util.UUID

data class CommentItemData(
    val id: String = UUID.randomUUID().toString(),
    val authorName: String,
    val text: String
)

data class JournalEntry(
    val id: String = UUID.randomUUID().toString(),
    val plantName: String,
    val date: String,
    val height: String,
    val notes: String,
    val condition: String,
    val isSharedToFeed: Boolean = true,
    val likesCount: Int = 125,
    val isLiked: Boolean = false,
    val comments: MutableList<CommentItemData> = mutableListOf(
        CommentItemData(authorName = "Raka", text = "Cantik banget daunnya!"),
        CommentItemData(authorName = "Salsa", text = "Pakai pupuk apa, Kak?")
    ),
    val timestamp: Long = System.currentTimeMillis()
)

object JournalRepository {
    private val _journals = mutableStateListOf<JournalEntry>().apply {
        add(
            JournalEntry(
                id = "post-1",
                plantName = "Monstera",
                date = "45 menit lalu",
                height = "32 cm",
                notes = "Daun baru akhirnya membuka. Pelan tapi pasti!",
                condition = "Daun segar",
                isSharedToFeed = true,
                likesCount = 125
            )
        )
        add(
            JournalEntry(
                id = "post-2",
                plantName = "Pothos",
                date = "1 jam lalu",
                height = "25 cm",
                notes = "Tanaman pothos makin lebat dan merambat indah.",
                condition = "Segar",
                isSharedToFeed = true,
                likesCount = 89
            )
        )
    }

    val journals: List<JournalEntry> get() = _journals

    fun addJournal(entry: JournalEntry) {
        _journals.add(0, entry)
    }

    fun updateJournal(updatedEntry: JournalEntry) {
        val index = _journals.indexOfFirst { it.id == updatedEntry.id }
        if (index != -1) {
            _journals[index] = updatedEntry
        }
    }

    fun deleteJournal(id: String) {
        _journals.removeAll { it.id == id }
    }

    fun toggleLike(id: String) {
        val index = _journals.indexOfFirst { it.id == id }
        if (index != -1) {
            val current = _journals[index]
            val newLiked = !current.isLiked
            val newCount = if (newLiked) current.likesCount + 1 else maxOf(0, current.likesCount - 1)
            _journals[index] = current.copy(isLiked = newLiked, likesCount = newCount)
        }
    }

    fun addComment(id: String, authorName: String, text: String) {
        val index = _journals.indexOfFirst { it.id == id }
        if (index != -1) {
            val current = _journals[index]
            current.comments.add(CommentItemData(authorName = authorName, text = text))
            _journals[index] = current.copy()
        }
    }
}
