package com.example.greeniepam.core.theme

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/*
 * Greenie Design System v1 — Gradien
 * Sumber: Figma › Foundations · Gradien
 * (Di Figma gradien belum bisa jadi variable, jadi di sini didefinisikan manual.)
 */
object GreenieGradients {

    /** Gradient/Canvas — latar semua layar (atas → bawah) */
    val canvas: Brush = Brush.verticalGradient(
        0.0f to GreeniePalette.Green700,
        0.5f to GreeniePalette.Green900,
        1.0f to GreeniePalette.Green950,
    )

    /** Gradient/Card hijau — kartu postingan */
    val cardGreen: Brush = Brush.verticalGradient(
        listOf(GreeniePalette.Green600, Color(0xFF143A27)),
    )

    /** Gradient/Card emas — kartu postingan */
    val cardGold: Brush = Brush.verticalGradient(
        listOf(GreeniePalette.Gold600, Color(0xFF5E4216)),
    )

    /** Gradient/Shade foto — taruh DI ATAS foto supaya teks di atasnya kebaca */
    val photoShade: Brush = Brush.verticalGradient(
        0.4f to Color.Transparent,
        1.0f to GreeniePalette.Black.copy(alpha = 0.6f),
    )
}

/** Shortcut buat latar layar standar: Modifier.fillMaxSize().greenieCanvas() */
fun Modifier.greenieCanvas(): Modifier = background(GreenieGradients.canvas)