package com.nitc.projectsgc

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nitc.projectsgc.composable.components.HeadingText
import kotlinx.coroutines.delay

@Composable
fun FlashScreen(onTimeout:()->Unit) {
    LaunchedEffect(Unit) {
        delay(300) // Delay for 5 seconds
        onTimeout()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.sgc_logo_blue_1),
            contentDescription = "SGC Logo",
            modifier = Modifier.fillMaxSize(0.6F)
        )
        HeadingText(text = stringResource(id = R.string.app_name), fontColor = Color.Black, modifier = Modifier)
    }
}