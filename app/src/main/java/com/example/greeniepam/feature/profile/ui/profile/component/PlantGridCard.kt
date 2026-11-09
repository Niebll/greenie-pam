package com.example.greeniepam.feature.profile.ui.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.feature.profile.data.model.PlantGradientType
import com.example.greeniepam.feature.profile.data.model.PlantItem

private val GreenPlantGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF388E59),
        Color(0xFF163E27),
        Color(0xFF0F2B1B),
    )
)

private val GoldPlantGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFB58734),
        Color(0xFF5E4216),
        Color(0xFF38260B),
    )
)

private val TealPlantGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF357F85),
        Color(0xFF164045),
        Color(0xFF0C2427),
    )
)

@Composable
fun PlantGridCard(
    plant: PlantItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val gradient = when (plant.gradientType) {
        PlantGradientType.GREEN -> GreenPlantGradient
        PlantGradientType.GOLD -> GoldPlantGradient
        PlantGradientType.TEAL -> TealPlantGradient
    }

    Box(
        modifier = modifier
            .height(150.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(gradient)
            .clickable(onClick = onClick)
            .padding(12.dp),
    ) {
        Icon(
            imageVector = Icons.Outlined.Eco,
            contentDescription = null,
            tint = Color(0x33FFFFFF),
            modifier = Modifier
                .size(44.dp)
                .align(Alignment.Center),
        )

        Text(
            text = plant.name,
            style = GreenieTheme.typography.title13,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.BottomStart),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0C1A12)
@Composable
private fun PlantGridCardPreview() {
    GreenieTheme {
        PlantGridCard(
            plant = PlantItem(1, "Monstera", PlantGradientType.GREEN),
        )
    }
}
