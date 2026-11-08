package com.example.greeniepam.core.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

/*
 * Greenie Design System v1 — Theme
 *
 * Cara pakai:
 *   MainActivity:  setContent { GreenieTheme { AppNavHost() } }
 *   Di screen:     GreenieTheme.colors.text.primary
 *                  GreenieTheme.typography.heading28
 *                  GreenieTheme.spacing.space16
 *                  RoundedCornerShape(GreenieTheme.radius.radius16)
 *                  Modifier.background(GreenieGradients.cardGreen)
 */

private val LocalGreenieColors = staticCompositionLocalOf { GreenieDarkColors }
private val LocalGreenieTypography = staticCompositionLocalOf { GreenieTypography() }
private val LocalGreenieSpacing = staticCompositionLocalOf { GreenieSpacing() }
private val LocalGreenieRadius = staticCompositionLocalOf { GreenieRadius() }

@Composable
fun GreenieTheme(content: @Composable () -> Unit) {
    val colors = GreenieDarkColors
    val typography = GreenieTypography()

    CompositionLocalProvider(
        LocalGreenieColors provides colors,
        LocalGreenieTypography provides typography,
        LocalGreenieSpacing provides GreenieSpacing(),
        LocalGreenieRadius provides GreenieRadius(),
    ) {
        // MaterialTheme tetap dipasang supaya komponen Material bawaan
        // (TextField, Switch, ModalBottomSheet, dll) ikut warna & font Greenie.
        MaterialTheme(
            colorScheme = colors.toMaterialColorScheme(),
            typography = typography.toMaterialTypography(),
        ) {
            CompositionLocalProvider(
                LocalContentColor provides colors.text.primary,
                content = content,
            )
        }
    }
}

/** Akses token: GreenieTheme.colors / typography / spacing / radius */
object GreenieTheme {
    val colors: GreenieColors
        @Composable @ReadOnlyComposable get() = LocalGreenieColors.current
    val typography: GreenieTypography
        @Composable @ReadOnlyComposable get() = LocalGreenieTypography.current
    val spacing: GreenieSpacing
        @Composable @ReadOnlyComposable get() = LocalGreenieSpacing.current
    val radius: GreenieRadius
        @Composable @ReadOnlyComposable get() = LocalGreenieRadius.current
}

// region Mapping ke Material 3 (hanya buat komponen Material bawaan)
private fun GreenieColors.toMaterialColorScheme() = darkColorScheme(
    primary = surface.accent,               // tombol emas = aksi utama
    onPrimary = text.onAccent,
    primaryContainer = surface.accentSoft,
    onPrimaryContainer = text.accent,
    secondary = surface.brand,
    onSecondary = text.primary,
    secondaryContainer = surface.successSoft,
    onSecondaryContainer = text.primary,
    background = background.canvas,
    onBackground = text.primary,
    surface = surface.sheet,
    onSurface = text.primary,
    surfaceVariant = surface.card,
    onSurfaceVariant = text.secondary,
    surfaceContainer = surface.sheet,
    surfaceContainerHigh = surface.sheet,
    surfaceContainerLow = background.canvasMid,
    error = surface.danger,
    onError = text.onAccent,
    errorContainer = surface.dangerSoft,
    onErrorContainer = text.danger,
    outline = border.default,
    outlineVariant = border.subtle,
    scrim = overlay.scrim,
)

private fun GreenieTypography.toMaterialTypography() = Typography(
    displayLarge = display52,
    headlineLarge = heading28,
    headlineMedium = heading20,
    headlineSmall = heading18,
    titleLarge = title17,
    titleMedium = title15,
    titleSmall = title14,
    bodyLarge = body14Medium,
    bodyMedium = body13Medium,
    bodySmall = body12,
    labelLarge = title15,
    labelMedium = label12,
    labelSmall = label11,
)
// endregion