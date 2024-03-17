package com.nitc.projectsgc.composable.components

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
fun HeadingText(text: String, fontColor: Color,modifier: Modifier) {
    Text(
        text = text,
        fontSize = 21.sp,
        fontWeight = FontWeight.Medium,
        color = fontColor,
        modifier = modifier
    )
}

@Composable
fun SubHeadingText(text: String, fontColor: Color,modifier: Modifier) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        color = fontColor,
        modifier = modifier
    )
}

@Composable
fun NormalText(text: String, fontColor: Color,modifier: Modifier) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = fontColor,
        modifier = modifier
    )
}

@Composable
fun HeadingCard(text: String, bg: Color, fontColor: Color) {
    Card(
        modifier = Modifier.background(bg).padding(10.dp)
    ) {
        HeadingText(text = text, fontColor = fontColor,modifier = Modifier)
    }
}


@Preview
@Composable
fun PreviewHeading() {
    HeadingCard(text = "Text", bg = Color.White, fontColor = Color.Black)
}
