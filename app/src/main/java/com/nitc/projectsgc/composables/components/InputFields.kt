package com.nitc.projectsgc.composables.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun EmailInputField() {
    val emailValue = remember {
        mutableStateOf("")
    }
    CardWithElevation(15,3) {
        TextField(
            modifier = Modifier
                .background(Color.White)
                .padding(5.dp),
            value = emailValue.value,
            shape = RoundedCornerShape(25.dp),
            onValueChange = {
                emailValue.value = it
            },
            label = {
                HeadingText(text = "Email", fontColor = Color.Black)
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.DarkGray,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.DarkGray,
                unfocusedContainerColor = Color.White
            )
        )
    }
}


@Composable
fun BasicInputField(hint:String,isPassword:Boolean,onValueChanged:(String)->Unit) {
    val passwordValue = remember {
        mutableStateOf("")
    }
    CardWithElevation(15,3) {
        TextField(
            modifier = Modifier
                .background(Color.White)
                .padding(5.dp),
            value = passwordValue.value,
            shape = RoundedCornerShape(25.dp),
            onValueChange = {
                passwordValue.value = it
                onValueChanged(it)
            },
            label = {
                HeadingText(text = hint, fontColor = Color.Black)
            },
            visualTransformation = if(isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Gray,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Gray,
                unfocusedContainerColor = Color.White
            )
        )
    }
}
