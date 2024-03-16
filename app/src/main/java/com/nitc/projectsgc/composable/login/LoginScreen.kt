package com.nitc.projectsgc.composable.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import arrow.core.Either
import com.nitc.projectsgc.composable.components.BasicButton
import com.nitc.projectsgc.composable.components.BasicCard
import com.nitc.projectsgc.composable.components.BasicInputField
import com.nitc.projectsgc.composable.components.LoginCard
import com.nitc.projectsgc.composable.util.storage.StorageViewModel


@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    storageViewModel: StorageViewModel,
    loginCallback: (userType: Int) -> Unit
) {
    val screenContext = LocalContext.current
    val authenticatedEitherState by loginViewModel.isAuthenticated.collectAsState()
    LaunchedEffect(authenticatedEitherState) {
//        if(loginState.value){
        Log.d("loginSuccess", "in launched effect")
        when (val authenticated = loginViewModel.isAuthenticated.value) {
            is Either.Left -> {
                Toast.makeText(screenContext, authenticated.value, Toast.LENGTH_SHORT).show()
            }

            is Either.Right -> {
                storageViewModel.storeUserData(
                    loginViewModel.loginCredential.value.userType,
                    loginViewModel.loginCredential.value.username,
                    loginViewModel.loginCredential.value.password,
                )
                loginCallback(loginViewModel.userType.value)
            }

            null -> {
//                    Toast.makeText(screenContext,"Error in logging you in",Toast.LENGTH_LONG).show()
                Log.d("loginSuccess", "Authenticated state is null")
            }
        }
//        }else Log.d("loginSuccess","value is false")
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
//        HeadingText(text = "Login", Color.Black)
//        Spacer(modifier = Modifier.height(10.dp))
        BasicCard(15, 3, 1, 4, CardDefaults.cardColors()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoginCard() { userType ->
                    loginViewModel.setUserType(userType)
                }
                Spacer(modifier = Modifier.size(200.dp))
                BasicInputField("Email", false) {
                    loginViewModel.setUsername(it)
                }
                Spacer(modifier = Modifier.size(20.dp))
                BasicInputField("Password", true) {
                    loginViewModel.setPassword(it)
                }
                Spacer(modifier = Modifier.size(20.dp))
                BasicButton(
                    text = "Login",
                    colors = ButtonDefaults.buttonColors(),
                    modifier = Modifier,
                    tc = Color.White
                ) {
                    loginViewModel.authenticate()
                }
            }
        }
    }
}
