package com.example.greeniepam.feature.profile.ui.edit_profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.core.theme.greenieCanvas

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onSaveSuccess: () -> Unit = {},
) {
    var name by remember { mutableStateOf("Dinda") }
    var username by remember { mutableStateOf("dinda.greenie") }
    var bio by remember { mutableStateOf("Mahasiswa, pecinta tanaman hias.\nKoleksi kecil di kamar kost.") }
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .greenieCanvas()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // ── Top Navigation Bar ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Tombol Kembali
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0x1FFFFFFF))
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp),
                    )
                }

                // Judul Halaman
                Text(
                    text = "Edit Profil",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                )

                // Tombol Simpan (Emas)
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFE5C16C))
                        .clickable {
                            Toast.makeText(context, "Profil berhasil disimpan!", Toast.LENGTH_SHORT).show()
                            onSaveSuccess()
                            onBack()
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Simpan",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F2418),
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Avatar Edit Section ──
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    contentAlignment = Alignment.BottomEnd,
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(Color(0xFF2C5E40)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Eco,
                            contentDescription = null,
                            tint = Color(0x66FFFFFF),
                            modifier = Modifier.size(48.dp),
                        )
                    }

                    // Badge ikon kamera
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE5C16C))
                            .border(2.dp, Color(0xFF0C1A12), CircleShape)
                            .clickable {
                                Toast.makeText(context, "Fitur ganti foto profil", Toast.LENGTH_SHORT).show()
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = "Ubah foto",
                            tint = Color(0xFF0F2418),
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Ubah Foto Profil",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFE5C16C),
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Fitur ganti foto profil", Toast.LENGTH_SHORT).show()
                    },
                )
            }

            Spacer(Modifier.height(32.dp))

            // ── Form Input Fields ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                FormFieldItem(
                    label = "Nama Lengkap",
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Masukkan nama lengkap",
                )

                FormFieldItem(
                    label = "Username",
                    value = username,
                    onValueChange = { username = it },
                    placeholder = "Masukkan username",
                    prefix = "@",
                )

                FormFieldItem(
                    label = "Bio / Deskripsi Profil",
                    value = bio,
                    onValueChange = { bio = it },
                    placeholder = "Tulis bio singkat...",
                    isSingleLine = false,
                    height = 100.dp,
                )
            }
        }
    }
}

@Composable
private fun FormFieldItem(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    prefix: String? = null,
    isSingleLine: Boolean = true,
    height: androidx.compose.ui.unit.Dp = 50.dp,
) {
    Column {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFA5B8AC),
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x1AFFFFFF))
                .border(1.dp, Color(0x24FFFFFF), RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.TopStart,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (prefix != null) {
                    Text(
                        text = prefix,
                        fontSize = 14.sp,
                        color = Color(0xFFE5C16C),
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.width(4.dp))
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = isSingleLine,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = Color.White,
                    ),
                    cursorBrush = SolidColor(Color(0xFFE5C16C)),
                    decorationBox = { innerTextField ->
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 14.sp,
                                color = Color(0x66A5B8AC),
                            )
                        }
                        innerTextField()
                    },
                )
            }
        }
    }
}

/* ---------- preview ---------- */

@Preview(showBackground = true, backgroundColor = 0xFF0C1A12)
@Composable
private fun EditProfileScreenPreview() {
    GreenieTheme {
        EditProfileScreen()
    }
}
