package com.nitc.projectsgc.composable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
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
    BasicCard(15, 3, 1, 4, CardDefaults.cardColors()) {
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
                HeadingText(text = "Email", fontColor = Color.Black, modifier = Modifier)
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
fun CardInputFieldWithOptions(
    hint: String,
    text:String,
    isPassword: Boolean,
    modifier: Modifier,
    keyboardOptions: KeyboardOptions,
    onValueChanged: (String) -> Unit
) {
    val inputValue = remember {
        mutableStateOf(text)
    }
    BasicCard(15, 3, 0, 4,CardDefaults.cardColors(
        containerColor = Color.White,
        contentColor = Color.White
    )) {
        TextField(
            modifier = modifier,
            value = inputValue.value,
            shape = RoundedCornerShape(25.dp),
            onValueChange = {
                inputValue.value = it
                onValueChanged(it)
            },
            label = {
                Text(text = hint, modifier = Modifier)
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            ),
            keyboardOptions = keyboardOptions
        )
    }
}

@Composable
fun CardInputFieldWithValue(
    hint: String,
    text:String,
    isPassword: Boolean,
    modifier: Modifier,
    onValueChanged: (String) -> Unit
) {
    val inputValue = remember {
        mutableStateOf(text)
    }
    BasicCard(15, 3, 0, 7,CardDefaults.cardColors(
        containerColor = Color.White,
        contentColor = Color.White
    )) {
        TextField(
            modifier = modifier,
            value = inputValue.value,
            shape = RoundedCornerShape(25.dp),
            onValueChange = {
                inputValue.value = it
                onValueChanged(it)
            },
            label = {
                Text(text = hint, modifier = Modifier)
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            )
        )
    }
}
@Composable
fun CardFieldWithValue(
    hint: String,
    text:String,
    modifier: Modifier,
) {
    BasicCard(15, 3, 0, 7,CardDefaults.cardColors(
        containerColor = Color.White,
        contentColor = Color.White
    )) {
        TextField(
            modifier = modifier,
            value = text,
            shape = RoundedCornerShape(25.dp),
            enabled = false,
            onValueChange = {
            },
            label = {
                Text(text = hint, modifier = Modifier, color = Color.Black)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                focusedLabelColor = Color.Black,
                disabledTextColor = Color.Black
            )
        )
    }
}
@Composable
fun BasicInputFieldWithColors(
    hint: String,
    isPassword: Boolean,
    cardColors: CardColors,
    textFieldColors: TextFieldColors,
    onValueChanged: (String) -> Unit
) {
    val inputValue = remember {
        mutableStateOf("")
    }
    BasicCard(15, 3, 1, 4, cardColors) {
        TextField(
            modifier = Modifier
                .background(Color.White)
                .padding(5.dp),
            value = inputValue.value,
            shape = RoundedCornerShape(25.dp),
            onValueChange = {
                inputValue.value = it
                onValueChanged(it)
            },
            label = {
                HeadingText(text = hint, fontColor = Color.Black, modifier = Modifier)
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            colors = textFieldColors
        )
    }
}


@Composable
fun BasicInputField(hint: String, isPassword: Boolean, onValueChanged: (String) -> Unit) {
    val passwordValue = remember {
        mutableStateOf("")
    }
    BasicCard(15, 3, 1, 4, CardDefaults.cardColors()) {
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
                HeadingText(text = hint, fontColor = Color.Black, modifier = Modifier)
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
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
