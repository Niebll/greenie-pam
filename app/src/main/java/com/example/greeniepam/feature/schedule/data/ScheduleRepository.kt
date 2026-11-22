package com.example.greeniepam.feature.schedule.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Baris tabel `care_tasks`. `taskDate` berformat yyyy-MM-dd. */
@Serializable
data class CareTaskRow(
    val id: Long,
    @SerialName("plant_name") val plantName: String,
    @SerialName("action_name") val actionName: String,
    @SerialName("task_time") val taskTime: String = "",
    @SerialName("task_date") val taskDate: String,
    @SerialName("is_done") val isDone: Boolean = false,
)

class ScheduleRepository(private val supabase: SupabaseClient) {

    private fun requireUserId(): String =
        supabase.auth.currentUserOrNull()?.id ?: error("User belum login")

    suspend fun getMyTasks(): List<CareTaskRow> =
        supabase.from(TABLE)
            .select {
                filter { eq("user_id", requireUserId()) }
                order("task_date", Order.ASCENDING)
                order("task_time", Order.ASCENDING)
            }
            .decodeList<CareTaskRow>()

    suspend fun addTask(plantName: String, actionName: String, taskTime: String, taskDate: String): CareTaskRow =
        supabase.from(TABLE)
            .insert(NewCareTaskRow(plantName, actionName, taskTime, taskDate)) { select() }
            .decodeSingle<CareTaskRow>()

    suspend fun setDone(id: Long, isDone: Boolean) {
        supabase.from(TABLE).update({ set("is_done", isDone) }) {
            filter { eq("id", id) }
        }
    }

    private companion object {
        const val TABLE = "care_tasks"
    }
}

@Serializable
private data class NewCareTaskRow(
    @SerialName("plant_name") val plantName: String,
    @SerialName("action_name") val actionName: String,
    @SerialName("task_time") val taskTime: String,
    @SerialName("task_date") val taskDate: String,
)
