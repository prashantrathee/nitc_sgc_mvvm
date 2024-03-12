package com.nitc.projectsgc

import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.nitc.projectsgc.composable.admin.AdminViewModel
import com.nitc.projectsgc.composable.admin.viewmodels.MentorListViewModel
import com.nitc.projectsgc.composable.admin.viewmodels.StudentListViewModel
import com.nitc.projectsgc.composable.components.HeadingText
import com.nitc.projectsgc.composable.components.SimpleSnackBar
import com.nitc.projectsgc.composable.components.SimpleToast
import com.nitc.projectsgc.composable.login.LoginScreen
import com.nitc.projectsgc.composable.mentor.MentorDashboardScreen
import com.nitc.projectsgc.composable.student.screens.StudentDashboardScreen
import com.nitc.projectsgc.composable.util.StorageAccess
import com.nitc.projectsgc.composable.login.LoginViewModel
import com.nitc.projectsgc.composable.navigation.NavigationScreen
import com.nitc.projectsgc.composable.navigation.graphs.adminGraph
import com.nitc.projectsgc.composable.navigation.graphs.mentorGraph
import com.nitc.projectsgc.composable.navigation.graphs.studentGraph
import com.nitc.projectsgc.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var binding: ActivityMainBinding
    val sharedViewModel: SharedViewModel by viewModels()
    private val adminViewModel : AdminViewModel by viewModels()
    private val studentListViewModel : StudentListViewModel by viewModels()
    private val mentorListViewModel : MentorListViewModel by viewModels()
    private val loginViewModel : LoginViewModel by viewModels()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val titleState = remember {
                mutableStateOf("Login")
            }
//            val titleStateHolder = TitleStateHolder(initialTitle = "Login")
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            HeadingText(
                                text = titleState.value,
                                fontColor = Color.Black,
                                modifier = Modifier
                            )
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
                content = { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        AllNavigation(titleState)
                    }
                }
            )

        }
    }


    @Composable
    fun AllNavigation(titleState: MutableState<String>) {
        Log.d("inLogin","Herehreheh")
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = NavigationScreen.LoginScreen.route) {
            composable(route = NavigationScreen.LoginScreen.route) {
                when(getUserType()){
                    0->navController.navigate("admin")
                    1->navController.navigate("student")
                    2->navController.navigate("mentor")
                    else->{

                    }
                }
                LoginScreen(navController = navController, loginViewModel = loginViewModel) {
                    storeData(
                        loginViewModel.userType.value,
                        loginViewModel.username.value,
                        loginViewModel.password.value
                    )
                    sharedViewModel.userType = loginViewModel.userType.value
                    when(sharedViewModel.userType) {
                        0->navController.navigate("admin")
                        1->navController.navigate("student")
                        2->navController.navigate("mentor")
                    }
                }
            }
            adminGraph(titleState,navController,adminViewModel,studentListViewModel,mentorListViewModel)
            studentGraph(navController)
            mentorGraph(navController)
        }
    }
    @Composable
    fun TitleStateHolder(initialTitle: String) {
        val titleState = remember { mutableStateOf(initialTitle) }
        ScaffoldProvider(titleState) // Pass the state holder down
    }

    @Composable
    private fun ScaffoldProvider(titleState: MutableState<String>) {

    }


    private fun getUserType() :Int{
        val stored = StorageAccess(this).getUserType()
        return stored
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