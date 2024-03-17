package com.nitc.projectsgc.composable.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun BasicButton(text: String, colors:ButtonColors, tc: Color,modifier: Modifier, clickCallback: () -> Unit) {
    Button(
        onClick = { clickCallback() },
        shape = RoundedCornerShape(15.dp),
        colors = colors,
        modifier = modifier
    ) {
        HeadingText(text, tc,modifier = Modifier)
    }
}

@Composable
fun BasicSubHeadingButton(text: String, colors:ButtonColors, tc: Color,modifier: Modifier, clickCallback: () -> Unit) {
    Button(
        onClick = { clickCallback() },
        shape = RoundedCornerShape(15.dp),
        colors = colors,
        modifier = modifier
    ) {
        SubHeadingText(text, tc,modifier = Modifier)
    }
}
