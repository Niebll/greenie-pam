package com.example.greeniepam.core.model

/** Jurnal pertumbuhan lengkap dengan info penulis, like, dan jumlah komentar. */
data class JournalPost(
    val id: Long,
    val authorId: String,
    val authorName: String,
    val plantName: String,
    val journalDate: String,
    val height: String,
    val notes: String,
    val condition: String,
    val isShared: Boolean,
    /** Waktu dibuat dalam format ISO-8601 dari Supabase. */
    val createdAt: String,
    val likeCount: Int,
    val isLiked: Boolean,
    val commentCount: Int,
    val isMine: Boolean,
)

data class JournalComment(
    val id: Long,
    val authorName: String,
    val body: String,
    val isMine: Boolean,
)

/** Data yang diisi user saat membuat jurnal baru. */
data class NewJournal(
    val plantName: String,
    val journalDate: String,
    val height: String,
    val notes: String,
    val condition: String,
    val isShared: Boolean,
)
