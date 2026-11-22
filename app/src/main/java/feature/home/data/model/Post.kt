package com.example.greeniepam.feature.home.data.model

/**
 * Model data untuk postingan di feed Home.
 */
data class Post(
    val id: Long,
    val authorName: String,
    val timeAgo: String,
    val imageUrl: String? = null,
    val plantHeight: String? = null,
    val leafCondition: String? = null,
    val caption: String,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    /** true = kartu emas (gold), false = kartu hijau (green) */
    val isGold: Boolean = false,
)
