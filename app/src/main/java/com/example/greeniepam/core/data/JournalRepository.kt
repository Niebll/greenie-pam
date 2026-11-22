package com.example.greeniepam.core.data

import com.example.greeniepam.core.model.JournalComment
import com.example.greeniepam.core.model.JournalPost
import com.example.greeniepam.core.model.NewJournal
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Akses tabel `journals`, `journal_likes`, dan `journal_comments`.
 * Dipakai bersama oleh Beranda, Detail Post, Buat Jurnal, dan Profil.
 */
class JournalRepository(private val supabase: SupabaseClient) {

    private val currentUserId: String?
        get() = supabase.auth.currentUserOrNull()?.id

    private fun requireUserId(): String = currentUserId ?: error("User belum login")

    /** Semua jurnal yang dibagikan ke Beranda, terbaru di atas. */
    suspend fun getFeed(): List<JournalPost> =
        supabase.from(JOURNALS)
            .select(JOURNAL_COLUMNS) {
                filter { eq("is_shared", true) }
                order("created_at", Order.DESCENDING)
            }
            .decodeList<JournalRow>()
            .map { it.toPost(currentUserId) }

    /** Semua jurnal milik user yang login (dibagikan maupun pribadi). */
    suspend fun getMyJournals(): List<JournalPost> {
        val uid = requireUserId()
        return supabase.from(JOURNALS)
            .select(JOURNAL_COLUMNS) {
                filter { eq("user_id", uid) }
                order("created_at", Order.DESCENDING)
            }
            .decodeList<JournalRow>()
            .map { it.toPost(uid) }
    }

    suspend fun getJournal(id: Long): JournalPost? =
        supabase.from(JOURNALS)
            .select(JOURNAL_COLUMNS) { filter { eq("id", id) } }
            .decodeSingleOrNull<JournalRow>()
            ?.toPost(currentUserId)

    suspend fun createJournal(journal: NewJournal) {
        supabase.from(JOURNALS).insert(
            NewJournalRow(
                plantName = journal.plantName,
                journalDate = journal.journalDate,
                height = journal.height,
                notes = journal.notes,
                condition = journal.condition,
                isShared = journal.isShared,
            )
        )
    }

    suspend fun setLiked(journalId: Long, liked: Boolean) {
        if (liked) {
            supabase.from(LIKES).insert(NewLikeRow(journalId))
        } else {
            val uid = requireUserId()
            supabase.from(LIKES).delete {
                filter {
                    eq("journal_id", journalId)
                    eq("user_id", uid)
                }
            }
        }
    }

    /** Jurnal yang difavoritkan user yang login. */
    suspend fun getFavorites(): List<JournalPost> {
        val uid = requireUserId()
        return supabase.from(JOURNALS)
            .select(FAVORITE_COLUMNS) {
                // !inner di kolom → hanya jurnal yang punya baris favorit milik user ini
                filter { eq("journal_favorites.user_id", uid) }
                order("created_at", Order.DESCENDING)
            }
            .decodeList<JournalRow>()
            .map { it.toPost(uid) }
    }

    suspend fun setFavorited(journalId: Long, favorited: Boolean) {
        if (favorited) {
            supabase.from(FAVORITES).insert(NewFavoriteRow(journalId))
        } else {
            val uid = requireUserId()
            supabase.from(FAVORITES).delete {
                filter {
                    eq("journal_id", journalId)
                    eq("user_id", uid)
                }
            }
        }
    }

    suspend fun getComments(journalId: Long): List<JournalComment> {
        val uid = currentUserId
        return supabase.from(COMMENTS)
            .select(Columns.raw("id, body, user_id, profiles(name)")) {
                filter { eq("journal_id", journalId) }
                order("created_at", Order.ASCENDING)
            }
            .decodeList<CommentRow>()
            .map {
                JournalComment(
                    id = it.id,
                    authorName = it.profiles?.name.orEmpty().ifBlank { "Pengguna" },
                    body = it.body,
                    isMine = it.userId == uid,
                )
            }
    }

    suspend fun addComment(journalId: Long, body: String) {
        supabase.from(COMMENTS).insert(NewCommentRow(journalId = journalId, body = body))
    }

    private companion object {
        const val JOURNALS = "journals"
        const val LIKES = "journal_likes"
        const val COMMENTS = "journal_comments"
        const val FAVORITES = "journal_favorites"

        private const val BASE_COLUMNS =
            "id, user_id, plant_name, journal_date, height, notes, condition, is_shared, created_at, " +
                // "!journals_user_id_fkey" wajib: journals terhubung ke profiles lewat beberapa jalur
                // (penulis, yang like, yang memfavoritkan), jadi relasinya harus disebut eksplisit.
                "profiles!journals_user_id_fkey(name), journal_likes(user_id), journal_comments(count)"

        // RLS hanya mengembalikan baris favorit milik user sendiri
        val JOURNAL_COLUMNS = Columns.raw("$BASE_COLUMNS, journal_favorites(user_id)")
        val FAVORITE_COLUMNS = Columns.raw("$BASE_COLUMNS, journal_favorites!inner(user_id)")
    }
}

/* ---------- bentuk data mentah dari Supabase ---------- */

@Serializable
private data class JournalRow(
    val id: Long,
    @SerialName("user_id") val userId: String,
    @SerialName("plant_name") val plantName: String,
    @SerialName("journal_date") val journalDate: String,
    val height: String = "",
    val notes: String = "",
    val condition: String = "",
    @SerialName("is_shared") val isShared: Boolean = true,
    @SerialName("created_at") val createdAt: String,
    val profiles: NameRef? = null,
    @SerialName("journal_likes") val likes: List<LikeRef> = emptyList(),
    @SerialName("journal_comments") val comments: List<CountRef> = emptyList(),
    @SerialName("journal_favorites") val favorites: List<LikeRef> = emptyList(),
) {
    fun toPost(currentUserId: String?) = JournalPost(
        id = id,
        authorId = userId,
        authorName = profiles?.name.orEmpty().ifBlank { "Pengguna" },
        plantName = plantName,
        journalDate = journalDate,
        height = height,
        notes = notes,
        condition = condition,
        isShared = isShared,
        createdAt = createdAt,
        likeCount = likes.size,
        isLiked = currentUserId != null && likes.any { it.userId == currentUserId },
        commentCount = comments.firstOrNull()?.count ?: 0,
        isMine = userId == currentUserId,
        isFavorited = currentUserId != null && favorites.any { it.userId == currentUserId },
    )
}

@Serializable
private data class NameRef(val name: String = "")

@Serializable
private data class LikeRef(@SerialName("user_id") val userId: String)

@Serializable
private data class CountRef(val count: Int = 0)

@Serializable
private data class CommentRow(
    val id: Long,
    val body: String,
    @SerialName("user_id") val userId: String,
    val profiles: NameRef? = null,
)

@Serializable
private data class NewJournalRow(
    @SerialName("plant_name") val plantName: String,
    @SerialName("journal_date") val journalDate: String,
    val height: String,
    val notes: String,
    val condition: String,
    @SerialName("is_shared") val isShared: Boolean,
)

@Serializable
private data class NewLikeRow(@SerialName("journal_id") val journalId: Long)

@Serializable
private data class NewFavoriteRow(@SerialName("journal_id") val journalId: Long)

@Serializable
private data class NewCommentRow(
    @SerialName("journal_id") val journalId: Long,
    val body: String,
)
