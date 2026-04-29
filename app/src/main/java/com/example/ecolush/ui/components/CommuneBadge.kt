package com.example.ecolush.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun getColorForCommune(commune: String): Color {
    return when (commune.lowercase()) {
        "annexe" -> Color(0xFFE57373) // rouge clair
        "kenya" -> Color(0xFF81C784) // vert
        "lubumbashi" -> Color(0xFF64B5F6) // bleu
        "kampemba" -> Color(0xFFBA68C8) // violet
        "katuba" -> Color(0xFF4FC3F7) // cyan
        else -> Color(0xFFB0BEC5) // gris

    }

}

@Composable
fun CommuneBadge(commune: String, modifier: Modifier = Modifier) {
    androidx.compose.material3.Surface(
        shape = RoundedCornerShape(8.dp),
        color = getColorForCommune(commune),
        modifier = modifier.padding(end = 4.dp)

    ) {
        Text(
            text = commune,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = Color.White,
            style = androidx.compose.material3.MaterialTheme.typography.labelSmall
        )
    }

}