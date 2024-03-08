package com.nitc.projectsgc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nitc.projectsgc.composable.components.HeadingText
import com.nitc.projectsgc.composable.components.SimpleSnackBar
import com.nitc.projectsgc.composable.components.SimpleToast
import com.nitc.projectsgc.composable.screens.AdminDashboardScreen
import com.nitc.projectsgc.composable.login.LoginScreen
import com.nitc.projectsgc.composable.screens.MentorDashboardScreen
import com.nitc.projectsgc.composable.screens.StudentDashboardScreen
import com.nitc.projectsgc.composable.util.StorageAccess
import com.nitc.projectsgc.composable.login.LoginViewModel
import com.nitc.projectsgc.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    lateinit var binding: ActivityMainBinding
    val sharedViewModel:SharedViewModel by viewModels()
    val loginViewModel: LoginViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val titleState = remember{
                mutableStateOf("Login")
            }
            Scaffold(
                topBar = {
                    TopAppBar(title = {
                    HeadingText(text = "Login", fontColor = Color.Black,modifier = Modifier)
                },
                    colors = TopAppBarColors(
                        containerColor = Color.LightGray,
                        titleContentColor = Color.Black,
                        actionIconContentColor = Color.White,
                        scrolledContainerColor = Color.Yellow,
                        navigationIconContentColor = Color.Black
                    )
                )
                },
                content = {paddingValues ->
                    Column(
                        modifier = Modifier.fillMaxSize().padding(paddingValues)
                    ) {
                        AllNavigations()
                    }
                }
            )

        }
    }


    @Composable
    fun AllNavigations(){
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "Login"){
            composable(route = "Login"){
                LoginScreen(navController = navController,loginViewModel = loginViewModel){
                    storeData(
                        loginViewModel.userType.value,
                        loginViewModel.username.value,
                        loginViewModel.password.value
                    )
                    sharedViewModel.userType = loginViewModel.userType.value
                }
            }
            composable(route = "Admin Dashboard"){
                AdminDashboardScreen(loginViewModel = loginViewModel, navController = navController)
            }

            composable(route = "Mentor Dashboard"){
                MentorDashboardScreen(loginViewModel = loginViewModel, navController = navController)
            }

            composable(route = "Student Dashboard"){
                StudentDashboardScreen(loginViewModel = loginViewModel, navController = navController)
            }

        }
    }

    private fun storeData(userType: Int, username: String, password: String) {
        val stored = StorageAccess(this).saveUsername(
            userType,
            username,
            password
        )
        if (stored) {
            setContent {
                SimpleToast(message = "Logged in")
            }
        } else {
            setContent {
                SimpleSnackBar(text = "Error in logging in")
            }
        }
    }
}