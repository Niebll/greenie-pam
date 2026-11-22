package com.example.greeniepam.core.util

import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("id", "ID"))

/** Ubah timestamp ISO dari Supabase jadi teks relatif, mis. "5 menit lalu". */
fun timeAgo(isoTimestamp: String, now: OffsetDateTime = OffsetDateTime.now()): String {
    val time = runCatching { OffsetDateTime.parse(isoTimestamp) }.getOrNull() ?: return ""
    val diff = Duration.between(time, now)
    return when {
        diff.toMinutes() < 1 -> "Baru saja"
        diff.toMinutes() < 60 -> "${diff.toMinutes()} menit lalu"
        diff.toHours() < 24 -> "${diff.toHours()} jam lalu"
        diff.toDays() < 7 -> "${diff.toDays()} hari lalu"
        else -> time.format(dateFormatter)
    }
}

/** Ubah pesan error teknis jadi pesan yang ramah untuk user. */
fun Throwable.toUserMessage(): String {
    // Pesan asli tetap dicatat di Logcat (tag "Greenie") untuk debugging
    android.util.Log.e("Greenie", "Error dari Supabase", this)
    val msg = message.orEmpty()
    return when {
        msg.contains("Invalid login credentials", ignoreCase = true) -> "Email atau password salah."
        msg.contains("already registered", ignoreCase = true) -> "Email sudah terdaftar. Silakan masuk."
        msg.contains("Email not confirmed", ignoreCase = true) -> "Email belum dikonfirmasi. Cek kotak masuk email kamu."
        msg.contains("Password should be", ignoreCase = true) -> "Password minimal 6 karakter."
        msg.contains("profiles_username_key", ignoreCase = true) -> "Username sudah dipakai orang lain."
        msg.contains("Unable to resolve host", ignoreCase = true) ||
            msg.contains("timeout", ignoreCase = true) -> "Tidak ada koneksi internet."
        else -> "Terjadi kesalahan. Coba lagi."
    }
}
