package com.example.greeniepam.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/*
 * Greenie Design System v1 — Warna
 * Sumber: Figma › 🌿 Design System › Foundations · Warna
 *
 * Aturan: di screen/komponen SELALU pakai token semantik lewat
 * GreenieTheme.colors.xxx (mis. GreenieTheme.colors.text.primary).
 * Primitif di bawah cuma buat mendefinisikan nilai, jangan dipakai langsung.
 */

// region Primitives (solid) — internal, jangan dipakai di screen
internal object GreeniePalette {
    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF000000)

    val Green50 = Color(0xFFF2F6F1)
    val Green200 = Color(0xFFA3BAAA)
    val Green300 = Color(0xFF8FD0A6)
    val Green500 = Color(0xFF5E9A62)
    val Green600 = Color(0xFF3E8C5A)
    val Green700 = Color(0xFF1B4A31)
    val Green800 = Color(0xFF142C20)
    val Green900 = Color(0xFF0F2418)
    val Green950 = Color(0xFF0C1A12)

    val Gold400 = Color(0xFFE3B95B)
    val Gold600 = Color(0xFFB58A3C)

    val Amber300 = Color(0xFFFFD76A)
    val Amber500 = Color(0xFFF2A33A)
    val Amber900 = Color(0xFF5A2E00)

    val Red400 = Color(0xFFE76B65)
    val Teal600 = Color(0xFF3B7F86)

    val Gray300 = Color(0xFFBFC4C2)
    val Gray600 = Color(0xFF3C403F)
    val Gray700 = Color(0xFF2D3130)
    val Gray800 = Color(0xFF1E2120)
}

/** Shorthand buat primitif transparan, mis. "white-a8" = White.a(0.08f) */
private fun Color.a(alpha: Float) = copy(alpha = alpha)
// endregion

// region Token semantik — sama persis dengan nama di Figma
@Immutable
data class BackgroundColors(
    val canvas: Color,      // background/canvas
    val canvasMid: Color,   // background/canvas-mid
    val canvasTop: Color,   // background/canvas-top
)

@Immutable
data class TextColors(
    val primary: Color,     // text/primary
    val secondary: Color,   // text/secondary
    val placeholder: Color, // text/placeholder
    val bodySoft: Color,    // text/body-soft
    val onAccent: Color,    // text/on-accent
    val accent: Color,      // text/accent
    val danger: Color,      // text/danger
    val inverse: Color,     // text/inverse
    val streak: Color,      // text/streak
)

@Immutable
data class SurfaceColors(
    val card: Color,            // surface/card
    val cardSubtle: Color,      // surface/card-subtle
    val cardMuted: Color,       // surface/card-muted
    val cardFaint: Color,       // surface/card-faint
    val raised: Color,          // surface/raised
    val raisedStrong: Color,    // surface/raised-strong
    val glass: Color,           // surface/glass
    val handle: Color,          // surface/handle
    val handleStrong: Color,    // surface/handle-strong
    val sheet: Color,           // surface/sheet
    val navbar: Color,          // surface/navbar
    val white: Color,           // surface/white
    val accent: Color,          // surface/accent
    val accentPressed: Color,   // surface/accent-pressed
    val accentSoft: Color,      // surface/accent-soft
    val accentTint: Color,      // surface/accent-tint
    val accentSubtle: Color,    // surface/accent-subtle
    val success: Color,         // surface/success
    val successSoft: Color,     // surface/success-soft
    val brand: Color,           // surface/brand
    val brandMuted: Color,      // surface/brand-muted
    val danger: Color,          // surface/danger
    val dangerSoft: Color,      // surface/danger-soft
    val dangerSubtle: Color,    // surface/danger-subtle
)

@Immutable
data class BorderColors(
    val default: Color,       // border/default
    val subtle: Color,        // border/subtle
    val emphasis: Color,      // border/emphasis
    val strong: Color,        // border/strong
    val accent: Color,        // border/accent
    val accentSoft: Color,    // border/accent-soft
    val accentStrong: Color,  // border/accent-strong
    val success: Color,       // border/success
    val successSoft: Color,   // border/success-soft
    val dangerSoft: Color,    // border/danger-soft
    val primary: Color,       // border/primary
)

@Immutable
data class IconColors(
    val default: Color,     // icon/default
    val primary: Color,     // icon/primary
    val white: Color,       // icon/white
    val onAccent: Color,    // icon/on-accent
    val accent: Color,      // icon/accent
    val danger: Color,      // icon/danger
    val success: Color,     // icon/success
    val flameOuter: Color,  // icon/flame-outer
    val flameInner: Color,  // icon/flame-inner
)

@Immutable
data class OverlayColors(
    val scrim: Color,       // overlay/scrim
)

@Immutable
data class GreenieColors(
    val background: BackgroundColors,
    val text: TextColors,
    val surface: SurfaceColors,
    val border: BorderColors,
    val icon: IconColors,
    val overlay: OverlayColors,
)
// endregion

// region Nilai (tema gelap — satu-satunya tema Greenie)
internal val GreenieDarkColors = with(GreeniePalette) {
    GreenieColors(
        background = BackgroundColors(
            canvas = Green950,
            canvasMid = Green900,
            canvasTop = Green700,
        ),
        text = TextColors(
            primary = Green50,
            secondary = Green200,
            placeholder = Green200.a(0.80f),
            bodySoft = Green50.a(0.85f),
            onAccent = Green950,
            accent = Gold400,
            danger = Red400,
            inverse = White,
            streak = Amber900,
        ),
        surface = SurfaceColors(
            card = White.a(0.08f),
            cardSubtle = White.a(0.05f),
            cardMuted = White.a(0.07f),
            cardFaint = White.a(0.06f),
            raised = White.a(0.12f),
            raisedStrong = White.a(0.14f),
            glass = White.a(0.20f),
            handle = White.a(0.30f),
            handleStrong = White.a(0.50f),
            sheet = Green800,
            navbar = Green800.a(0.78f),
            white = White,
            accent = Gold400,
            accentPressed = Gold600,
            accentSoft = Gold400.a(0.18f),
            accentTint = Gold400.a(0.14f),
            accentSubtle = Gold400.a(0.08f),
            success = Green300,
            successSoft = Green300.a(0.20f),
            brand = Green600,
            brandMuted = Green500,
            danger = Red400,
            dangerSoft = Red400.a(0.20f),
            dangerSubtle = Red400.a(0.15f),
        ),
        border = BorderColors(
            default = White.a(0.12f),
            subtle = White.a(0.10f),
            emphasis = White.a(0.14f),
            strong = White.a(0.18f),
            accent = Gold400,
            accentSoft = Gold400.a(0.25f),
            accentStrong = Gold400.a(0.60f),
            success = Green300,
            successSoft = Green300.a(0.55f),
            dangerSoft = Red400.a(0.55f),
            primary = Green50,
        ),
        icon = IconColors(
            default = Green200,
            primary = Green50,
            white = White,
            onAccent = Green950,
            accent = Gold400,
            danger = Red400,
            success = Green300,
            flameOuter = Amber500,
            flameInner = Amber300,
        ),
        overlay = OverlayColors(
            scrim = Black.a(0.60f),
        ),
    )
}
// endregion