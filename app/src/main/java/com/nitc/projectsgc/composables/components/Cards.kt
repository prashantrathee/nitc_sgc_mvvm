package com.nitc.projectsgc.composables.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun LoginCard() {
    CardWithElevation(25,4) {
        Row(
            modifier = Modifier
                .background(Color.White)
        ) {
            Button(
                onClick = { },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                HeadingText(text = "Admin", fontColor = Color.Black)
            }
            Button(
                onClick = { },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                HeadingText(text = "Student", fontColor = Color.White)
            }

            Button(
                onClick = { },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                HeadingText(text = "Mentor", fontColor = Color.Black)
            }
        }
    }
}

@Composable
fun CardWithElevation(cr:Int,elevation:Int,content:@Composable ()->Unit){
    Card(
        modifier = Modifier
            .padding(8.dp),
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(cr.dp),
        elevation = CardDefaults.cardElevation(elevation.dp)
    ){
        content()
    }
}

@Composable
fun CardWithoutRadius(elevation:Int = 3,content:@Composable ()->Unit){
    Card(
        modifier = Modifier
            .padding(8.dp),
        border = BorderStroke(1.dp, Color.Black),
        elevation = CardDefaults.cardElevation(elevation.dp)
    ){
        content()
    }
}