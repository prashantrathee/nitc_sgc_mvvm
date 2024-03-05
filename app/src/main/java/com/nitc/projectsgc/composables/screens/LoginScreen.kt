package com.nitc.projectsgc.composables.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nitc.projectsgc.composables.components.BasicButton
import com.nitc.projectsgc.composables.components.BasicInputField
import com.nitc.projectsgc.composables.components.CardWithElevation
import com.nitc.projectsgc.composables.components.CardWithoutRadius
import com.nitc.projectsgc.composables.components.EmailInputField
import com.nitc.projectsgc.composables.components.HeadingCard
import com.nitc.projectsgc.composables.components.HeadingText
import com.nitc.projectsgc.composables.components.LoginCard


@Preview
@Composable
fun LoginScreen() {
    val userType = remember{
        mutableStateOf("Student")
    }
    val emailValue = remember{
        mutableStateOf("")
    }

    val passwordValue = remember{
        mutableStateOf("")
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HeadingText(text = "Login", Color.Black)
        Spacer(modifier = Modifier.height(10.dp))
        CardWithoutRadius() {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoginCard()
                Spacer(modifier = Modifier.size(200.dp))
                BasicInputField("Email",false){
                    emailValue.value = it
                }
                Spacer(modifier = Modifier.size(20.dp))
                BasicInputField("Password",true){
                    passwordValue.value = it
                }
                Spacer(modifier = Modifier.size(20.dp))
                BasicButton(text = "Login", bg = Color.Black, tc = Color.White) {clicked->
                    if(clicked){
                        
                    }
                }
            }
        }
    }
}