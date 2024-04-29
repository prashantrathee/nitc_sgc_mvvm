package com.nitc.projectsgc.composable.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import arrow.core.Either
import com.nitc.projectsgc.R
import com.nitc.projectsgc.composable.components.BasicButton
import com.nitc.projectsgc.composable.components.CardInputFieldWithValue
import com.nitc.projectsgc.composable.components.LoginCard
import com.nitc.projectsgc.composable.util.PathUtils
import com.nitc.projectsgc.composable.util.storage.StorageViewModel


@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    storageViewModel: StorageViewModel,
    loginCallback: (userType: Int) -> Unit
) {
    val density = LocalDensity.current
    val spacerTopLarge = with(density) { dimensionResource(id = R.dimen.spacer_top_large) }
    val spacerTopNormal = with(density) { dimensionResource(id = R.dimen.spacer_top_normal) }
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
    ) {
//        HeadingText(text = "Login", Color.Black)
        Image(
            modifier = Modifier.fillMaxSize(0.3F),
            contentScale = ContentScale.Fit,
            painter = painterResource(id = R.drawable.sgc_logo_blue_1),
            contentDescription = "NITC SGC"
        )
        LoginCard() { userType ->
            loginViewModel.setUserType(userType)
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(1) {
                Spacer(modifier = Modifier.size(spacerTopLarge))
                CardInputFieldWithValue(
                    hint = "Email", isPassword = false,
                    text = "",
                    modifier = Modifier
                ) {
                    loginViewModel.setUsername(it)
                }
                Spacer(modifier = Modifier.size(spacerTopNormal))
                CardInputFieldWithValue(
                    hint = "Password",
                    isPassword = true,
                    text = "",
                    modifier = Modifier
                ) {
                    loginViewModel.setPassword(it)
                }
                Spacer(modifier = Modifier.size(spacerTopLarge))
                BasicButton(
                    text = "Login",
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.navy_blue)
                    ),
                    modifier = Modifier,
                    tc = Color.White
                ) {
                    if (PathUtils.isValidUsername(
                            loginViewModel.username.value,
                            loginViewModel.userType.value
                        )
                    ) loginViewModel.authenticate()
                    else {
                        Toast.makeText(screenContext, "Enter valid username", Toast.LENGTH_SHORT)
                            .show()
                        return@BasicButton
                    }
                }
            }
        }
    }
}
