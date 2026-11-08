package com.example.greeniepam.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/*
 * Greenie Design System v1 — Tipografi
 * Sumber: Figma › Foundations · Tipografi
 *
 * Font: font bawaan sistem (Roboto di kebanyakan HP Android), bukan Plus Jakarta Sans.
 * Ukuran & ketebalan tetap ikut Figma. Format nama: Peran/Ukuran
 *   Heading = ExtraBold, Title = Bold, Label = SemiBold, Body = Regular/Medium
 * Line height di Figma "auto", jadi dibiarkan default kecuali yang ditulis eksplisit.
 *
 * Kalau nanti mau ganti font custom, cukup ubah AppFontFamily di bawah.
 */
val AppFontFamily: FontFamily = FontFamily.Default

private fun style(
    size: Int,
    weight: FontWeight,
    lineHeight: TextUnit = TextUnit.Unspecified,
    italic: Boolean = false,
) = TextStyle(
    fontFamily = AppFontFamily,
    fontSize = size.sp,
    fontWeight = weight,
    fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
    lineHeight = lineHeight,
    letterSpacing = 0.sp,
)

@Immutable
data class GreenieTypography(
    // Display
    val display52: TextStyle = style(52, FontWeight.ExtraBold),       // Angka streak

    // Heading (ExtraBold)
    val heading28: TextStyle = style(28, FontWeight.ExtraBold),       // Judul halaman (H1)
    val heading20: TextStyle = style(20, FontWeight.ExtraBold),       // Judul seksi besar
    val heading18: TextStyle = style(18, FontWeight.ExtraBold),       // Judul dialog
    val heading17: TextStyle = style(17, FontWeight.ExtraBold),       // Judul bottom sheet / kartu
    val heading16: TextStyle = style(16, FontWeight.ExtraBold),       // Judul kartu kecil
    val heading15: TextStyle = style(15, FontWeight.ExtraBold),       // Judul list
    val heading13: TextStyle = style(13, FontWeight.ExtraBold),       // Penekanan kecil

    // Title (Bold)
    val title17: TextStyle = style(17, FontWeight.Bold),              // Judul top bar
    val title15: TextStyle = style(15, FontWeight.Bold),              // Label tombol besar
    val title14: TextStyle = style(14, FontWeight.Bold),              // Nama item / judul baris
    val title13: TextStyle = style(13, FontWeight.Bold),              // Nama pengguna, label tombol kecil
    val title12: TextStyle = style(12, FontWeight.Bold),              // Label tegas kecil
    val title11: TextStyle = style(11, FontWeight.Bold),              // Label chip/tag tegas

    // Label (SemiBold)
    val label13: TextStyle = style(13, FontWeight.SemiBold),          // Label tab/chip
    val label12: TextStyle = style(12, FontWeight.SemiBold),          // Label field
    val label11: TextStyle = style(11, FontWeight.SemiBold),          // Tag
    val label10: TextStyle = style(10, FontWeight.SemiBold),          // Label navbar / kalender

    // Body Medium
    val body17MediumEditor: TextStyle = style(17, FontWeight.Medium, lineHeight = 1.55.em), // Editor jurnal
    val body14Medium: TextStyle = style(14, FontWeight.Medium),       // Isi input terisi
    val body13Medium: TextStyle = style(13, FontWeight.Medium),       // Isi teks utama
    val body13MediumParagraph: TextStyle = style(13, FontWeight.Medium, lineHeight = 18.sp), // Caption postingan
    val body12Medium: TextStyle = style(12, FontWeight.Medium),
    val body11Medium: TextStyle = style(11, FontWeight.Medium),

    // Body Regular
    val body14: TextStyle = style(14, FontWeight.Normal),             // Placeholder input
    val body13: TextStyle = style(13, FontWeight.Normal),             // Deskripsi
    val body13Paragraph: TextStyle = style(13, FontWeight.Normal, lineHeight = 20.sp), // Deskripsi panjang
    val body12: TextStyle = style(12, FontWeight.Normal),             // Teks pendukung
    val body11: TextStyle = style(11, FontWeight.Normal),             // Caption / waktu

    // Body Italic
    val body13Italic: TextStyle = style(13, FontWeight.Normal, italic = true), // Nama latin
    val body12Italic: TextStyle = style(12, FontWeight.Normal, italic = true), // Nama latin kecil
)