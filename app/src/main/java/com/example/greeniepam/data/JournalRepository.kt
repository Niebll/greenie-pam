package com.example.greeniepam.data

import androidx.compose.runtime.mutableStateListOf
import com.example.greeniepam.ui.JournalEntry

object JournalRepository {
    private val _journals = mutableStateListOf<JournalEntry>().apply {
        add(
            JournalEntry(
                id = "default-journal-1",
                plantName = "Monstera",
                date = "1 Nov 2026",
                height = "32 cm",
                notes = "Daun baru akhirnya membuka. Pelan tapi pasti!",
                condition = "Segar",
                isSharedToFeed = true
            )
        )
    }

    val journals: List<JournalEntry> get() = _journals

    fun addJournal(entry: JournalEntry) {
        _journals.add(0, entry)
    }
}
