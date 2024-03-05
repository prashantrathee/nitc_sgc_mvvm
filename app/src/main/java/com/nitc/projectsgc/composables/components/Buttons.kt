package com.nitc.projectsgc.composables.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun BasicButton(text: String, bg: Color, tc: Color, clickCallback: (Boolean) -> Unit) {
    Button(
        onClick = { clickCallback(true) },
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = bg
        )
    ) {
        HeadingText(text, tc)
    }
}