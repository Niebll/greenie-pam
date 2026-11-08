package com.example.greeniepam.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/*
 * Greenie Design System v1 — Spacing & Radius
 * Sumber: Figma › Foundations · Spacing, Radius & Efek
 */

/** Koleksi Spacing — buat gap & padding. Nama sama dengan Figma (space/16 → space16). */
@Immutable
data class GreenieSpacing(
    val space2: Dp = 2.dp,
    val space4: Dp = 4.dp,
    val space6: Dp = 6.dp,
    val space8: Dp = 8.dp,
    val space10: Dp = 10.dp,
    val space12: Dp = 12.dp,
    val space14: Dp = 14.dp,
    val space16: Dp = 16.dp,
    val space18: Dp = 18.dp,
    val space20: Dp = 20.dp,
    val space22: Dp = 22.dp,
    val space24: Dp = 24.dp,
    val space36: Dp = 36.dp,

    /** Padding kiri-kanan standar layar */
    val screenHorizontal: Dp = 20.dp,
    /** Jarak atas standar layar */
    val screenTop: Dp = 36.dp,
)

/**
 * Koleksi Radius — sudut kartu, tombol, avatar.
 * Pakai: RoundedCornerShape(GreenieTheme.radius.radius16)
 */
@Immutable
data class GreenieRadius(
    val radius8: Dp = 8.dp,
    val radius12: Dp = 12.dp,
    val radius14: Dp = 14.dp,
    val radius16: Dp = 16.dp,
    val radius18: Dp = 18.dp,
    val radius20: Dp = 20.dp,
    val radius22: Dp = 22.dp,
    val radius24: Dp = 24.dp,
    val radius26: Dp = 26.dp,
    val radius28: Dp = 28.dp,
    val radius30: Dp = 30.dp,
    val radius32: Dp = 32.dp,
    val radius34: Dp = 34.dp,
    /** radius/full — buat pill & avatar bulat */
    val full: Dp = 999.dp,
)

/** Ukuran yang dipakai lintas komponen (dari prinsip "Ramah jempol" & board Komponen) */
object GreenieSize {
    /** Target sentuh minimum */
    val minTouchTarget: Dp = 44.dp
    /** Ikon standar (24px, stroke 2) */
    val icon: Dp = 24.dp
}