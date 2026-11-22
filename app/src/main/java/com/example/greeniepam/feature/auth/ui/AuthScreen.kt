package com.example.greeniepam.feature.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.core.theme.greenieCanvas
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    AuthContent(
        state = state,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSubmit = viewModel::submit,
        onToggleMode = viewModel::toggleMode,
        modifier = modifier,
    )
}

@Composable
private fun AuthContent(
    state: AuthUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onToggleMode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .greenieCanvas()
            .statusBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = GreenieTheme.spacing.screenHorizontal),
    ) {
        Spacer(Modifier.height(GreenieTheme.spacing.screenTop * 2))

        Box(
            modifier = Modifier
                .size(64.dp) // ukuran logo
                .clip(RoundedCornerShape(GreenieTheme.radius.radius20))
                .background(GreenieTheme.colors.surface.brand),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Eco,
                contentDescription = null,
                tint = GreenieTheme.colors.icon.white,
                modifier = Modifier.size(32.dp),
            )
        }

        Spacer(Modifier.height(GreenieTheme.spacing.space24))

        Text(
            text = if (state.isRegister) "Buat akun Greenie" else "Selamat datang kembali",
            style = GreenieTheme.typography.heading28,
            color = GreenieTheme.colors.text.primary,
        )
        Spacer(Modifier.height(GreenieTheme.spacing.space6))
        Text(
            text = if (state.isRegister) "Mulai catat perkembangan tanamanmu" else "Masuk untuk lanjut merawat tanamanmu",
            style = GreenieTheme.typography.body13,
            color = GreenieTheme.colors.text.secondary,
        )

        Spacer(Modifier.height(GreenieTheme.spacing.space36))

        Column(verticalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space16)) {
            if (state.isRegister) {
                AuthField(
                    label = "Nama",
                    value = state.name,
                    onValueChange = onNameChange,
                    placeholder = "Nama kamu",
                )
            }
            AuthField(
                label = "Email",
                value = state.email,
                onValueChange = onEmailChange,
                placeholder = "nama@email.com",
                keyboardType = KeyboardType.Email,
            )
            AuthField(
                label = "Password",
                value = state.password,
                onValueChange = onPasswordChange,
                placeholder = "Minimal 6 karakter",
                keyboardType = KeyboardType.Password,
                isPassword = true,
            )
        }

        state.error?.let {
            Spacer(Modifier.height(GreenieTheme.spacing.space12))
            Text(text = it, style = GreenieTheme.typography.body12Medium, color = GreenieTheme.colors.text.danger)
        }
        state.info?.let {
            Spacer(Modifier.height(GreenieTheme.spacing.space12))
            Text(text = it, style = GreenieTheme.typography.body12Medium, color = GreenieTheme.colors.text.accent)
        }

        Spacer(Modifier.height(GreenieTheme.spacing.space24))

        // Tombol aksi utama (emas)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp) // tinggi tombol utama sesuai Buat Jurnal
                .clip(RoundedCornerShape(GreenieTheme.radius.full))
                .background(GreenieTheme.colors.surface.accent)
                .clickable(enabled = !state.isLoading, onClick = onSubmit),
            contentAlignment = Alignment.Center,
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = GreenieTheme.colors.text.onAccent,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(22.dp),
                )
            } else {
                Text(
                    text = if (state.isRegister) "Daftar" else "Masuk",
                    style = GreenieTheme.typography.title15,
                    color = GreenieTheme.colors.text.onAccent,
                )
            }
        }

        Spacer(Modifier.height(GreenieTheme.spacing.space16))

        Text(
            text = if (state.isRegister) "Sudah punya akun? Masuk" else "Belum punya akun? Daftar",
            style = GreenieTheme.typography.label13,
            color = GreenieTheme.colors.text.accent,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(GreenieTheme.radius.radius8))
                .clickable(enabled = !state.isLoading, onClick = onToggleMode)
                .padding(GreenieTheme.spacing.space12),
        )

        Spacer(Modifier.height(GreenieTheme.spacing.space24))
    }
}

@Composable
private fun AuthField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
) {
    Column {
        Text(text = label, style = GreenieTheme.typography.label12, color = GreenieTheme.colors.text.secondary)
        Spacer(Modifier.height(GreenieTheme.spacing.space8))
        val shape = RoundedCornerShape(GreenieTheme.radius.radius16)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(GreenieTheme.colors.surface.card)
                .border(1.dp, GreenieTheme.colors.border.subtle, shape)
                .padding(horizontal = GreenieTheme.spacing.space16, vertical = GreenieTheme.spacing.space14),
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = GreenieTheme.typography.body14Medium.copy(color = GreenieTheme.colors.text.primary),
                cursorBrush = SolidColor(GreenieTheme.colors.text.accent),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { inner ->
                    if (value.isEmpty()) {
                        Text(text = placeholder, style = GreenieTheme.typography.body14, color = GreenieTheme.colors.text.placeholder)
                    }
                    inner()
                },
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0C1A12)
@Composable
private fun AuthContentPreview() {
    GreenieTheme {
        AuthContent(
            state = AuthUiState(email = "dinda@email.com"),
            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onSubmit = {},
            onToggleMode = {},
        )
    }
}
