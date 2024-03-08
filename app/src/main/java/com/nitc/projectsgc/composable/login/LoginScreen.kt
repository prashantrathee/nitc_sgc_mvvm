package com.nitc.projectsgc.composable.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nitc.projectsgc.composable.components.BasicButton
import com.nitc.projectsgc.composable.components.BasicCard
import com.nitc.projectsgc.composable.components.BasicInputField
import com.nitc.projectsgc.composable.components.LoginCard


@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    loginCallback:()->Unit
) {
    val isAuthenticated by loginViewModel.isAuthenticated.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
//        HeadingText(text = "Login", Color.Black)
//        Spacer(modifier = Modifier.height(10.dp))
        BasicCard(15,3,1,4,CardDefaults.cardColors()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoginCard(){userType->
                    loginViewModel.setUserType(userType)
                }
                Spacer(modifier = Modifier.size(200.dp))
                BasicInputField("Email",false){
                    loginViewModel.setUsername(it)
                }
                Spacer(modifier = Modifier.size(20.dp))
                BasicInputField("Password",true){
                    loginViewModel.setPassword(it)
                }
                Spacer(modifier = Modifier.size(20.dp))
                BasicButton(text = "Login", bg = Color.Black, tc = Color.White) {
                    loginViewModel.authenticate()

                }
            }
        }
    }
    if(isAuthenticated){
        loginCallback()
    }
}
