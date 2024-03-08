package com.nitc.projectsgc.composable.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.nitc.projectsgc.composable.login.LoginViewModel

class LoginActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            getLogin()
        }
    }

//    @OptIn(ExperimentalMaterial3Api::class)
//    @Composable
//    fun getLogin() {
//        Scaffold(
//            modifier = Modifier.fillMaxSize(),
//            topBar = {
//                TopAppBar(title = {
//                    HeadingText(text = "Login", fontColor = Color.Black)
//                },
//                    colors = TopAppBarColors(
//                        containerColor = Color.LightGray,
//                        titleContentColor = Color.Black,
//                        actionIconContentColor = Color.White,
//                        scrolledContainerColor = Color.Yellow,
//                        navigationIconContentColor = Color.Black
//                    )
//                )
//            },
//            content = { paddingValues ->
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues)
//                ) {
//                    LoginScreen(loginViewModel = loginViewModel) {
//                        storeData(
//                            loginViewModel.userType.value,
//                            loginViewModel.username.value,
//                            loginViewModel.password.value
//                        )
//                    }
//                }
//            }
//        )
//    }



}