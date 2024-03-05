package com.nitc.projectsgc.composables.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeadingText(text: String, fontColor: Color) {
    Text(
        text = text,
        fontSize = 21.sp,
        fontWeight = FontWeight.Medium,
        color = fontColor
    )
}

@Composable
fun HeadingCard(text: String, bg: Color, fontColor: Color) {
    Card(
        modifier = Modifier.background(bg).padding(10.dp)
    ) {
        HeadingText(text = text, fontColor = fontColor)
    }
}


@Preview
@Composable
fun PreviewHeading() {
    HeadingCard(text = "Text", bg = Color.White, fontColor = Color.Black)
}
