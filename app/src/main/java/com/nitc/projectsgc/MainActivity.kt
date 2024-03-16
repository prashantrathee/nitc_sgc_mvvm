package com.nitc.projectsgc

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nitc.projectsgc.composable.admin.viewmodels.AdminViewModel
import com.nitc.projectsgc.composable.admin.viewmodels.MentorListViewModel
import com.nitc.projectsgc.composable.admin.viewmodels.StudentListViewModel
import com.nitc.projectsgc.composable.components.HeadingText
import com.nitc.projectsgc.composable.components.SimpleSnackBar
import com.nitc.projectsgc.composable.components.SimpleToast
import com.nitc.projectsgc.composable.components.SubHeadingText
import com.nitc.projectsgc.composable.login.LoginScreen
import com.nitc.projectsgc.composable.util.storage.StorageManagerImpl
import com.nitc.projectsgc.composable.login.LoginViewModel
import com.nitc.projectsgc.composable.mentor.MentorViewModel
import com.nitc.projectsgc.composable.navigation.NavigationScreen
import com.nitc.projectsgc.composable.navigation.graphs.adminGraph
import com.nitc.projectsgc.composable.navigation.graphs.mentorGraph
import com.nitc.projectsgc.composable.navigation.graphs.studentGraph
import com.nitc.projectsgc.composable.student.viewmodels.BookingViewModel
import com.nitc.projectsgc.composable.student.viewmodels.StudentViewModel
import com.nitc.projectsgc.composable.util.storage.StorageViewModel
import com.nitc.projectsgc.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var binding: ActivityMainBinding
    val sharedViewModel: SharedViewModel by viewModels()
    private val adminViewModel: AdminViewModel by viewModels()
    private val studentListViewModel: StudentListViewModel by viewModels()
    private val mentorListViewModel: MentorListViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val mentorViewModel: MentorViewModel by viewModels()
    private val studentViewModel: StudentViewModel by viewModels()
    private val bookingViewModel: BookingViewModel by viewModels()
    private val storageViewModel: StorageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            val titleStateHolder = TitleStateHolder(initialTitle = "Login")
            AllContent()

        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AllContent() {
        val titleState = remember {
            mutableStateOf("SGC NITC")
        }
        val topBarState = remember {
            mutableStateOf(false)
        }
        val dropDownState = remember {
            mutableStateOf(false)
        }
        val dashboardState = remember {
            mutableStateOf(false)
        }
        val logoutState = remember {
            mutableStateOf(false)
        }
        Scaffold(
            topBar = {
                if (topBarState.value) {
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
                        ),
                        actions = {
                            Box(modifier = Modifier) {
                                IconButton(onClick = {
                                    dropDownState.value = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.MoreVert,
                                        contentDescription = "More Options"
                                    )
                                }
                                DropdownMenu(
                                    modifier = Modifier.align(Alignment.BottomEnd),
                                    expanded = dropDownState.value,
                                    onDismissRequest = {
                                        dropDownState.value = false
                                    }) {
                                    DropdownMenuItem(
                                        text = {
                                            SubHeadingText(
                                                text = "Dashboard",
                                                fontColor = Color.Black,
                                                modifier = Modifier
                                            )
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Filled.Home,
                                                contentDescription = "Dashboard Icon"
                                            )
                                        },
                                        onClick = {
                                            dashboardState.value = true
                                            dropDownState.value = false
                                        })
                                    DropdownMenuItem(
                                        text = {
                                            SubHeadingText(
                                                text = "Logout",
                                                fontColor = Color.Black,
                                                modifier = Modifier
                                            )
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                                contentDescription = "Logout Icon"
                                            )
                                        },
                                        onClick = {
                                            logout()

                                            dropDownState.value = false
                                            logoutState.value = true
                                        })
                                }
                            }
                        },
                    )
                }
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    AllNavigation(titleState, topBarState, dashboardState,logoutState)
                }
            }
        )

    }


    @Composable
    fun AllNavigation(
        titleState: MutableState<String>,
        topBarState: MutableState<Boolean>,
        dashboardState: MutableState<Boolean>,
        logoutState: MutableState<Boolean>
    ) {
        Log.d("inLogin", "Herehreheh")
        val navController = rememberNavController()
        var userType = -1
//        LaunchedEffect(key1 = Unit) {
//            navController.navigate(NavigationScreen.FlashScreen.route)
//        }
//        LaunchedEffect(key1 = logoutState.value) {
//            if (logoutState.value) {
//                navController.navigate(NavigationScreen.LoginScreen.route)
//            }
//        }
//        LaunchedEffect(key1 = dashboardState.value) {
//            if (dashboardState.value) {
//                dashboardState.value = false
//                navController.popBackStack(NavigationScreen.FlashScreen.route, true)
//                when (userType) {
//                    0 -> navController.navigate("admin")
//                    1 -> navController.navigate("student")
//                    2 -> navController.navigate("mentor")
//                    else -> navController.navigate(NavigationScreen.LoginScreen.route)
//                }
//            }
//        }
        NavHost(
            navController = navController,
            startDestination = NavigationScreen.FlashScreen.route
        ) {
            composable(route = NavigationScreen.FlashScreen.route) {
                userType = storageViewModel.getUserType()
                topBarState.value = false
                Log.d("userType","User type is $userType")
                FlashScreen {
//                    navController.popBackStack(NavigationScreen.FlashScreen.route, true)
                    when (userType) {
                        0 -> navController.navigate("admin")
                        1 -> navController.navigate("student")
                        2 -> navController.navigate("mentor")
                        else -> navController.navigate(NavigationScreen.LoginScreen.route)
                    }
                }
            }
            composable(route = NavigationScreen.LoginScreen.route) {
                topBarState.value = false
//                logoutState.value = false
                LoginScreen(navController = navController, loginViewModel = loginViewModel,storageViewModel = storageViewModel) {
                    Log.d("loginSuccess","Login successful")
//                    storeData(
//                        loginViewModel.userType.value,
//                        loginViewModel.username.value,
//                        loginViewModel.password.value
//                    )
                    userType = storageViewModel.getUserType()
                    sharedViewModel.userType = userType
//                    navController.popBackStack(NavigationScreen.LoginScreen.route, true)
                    Log.d("userType","User type is $userType")
                    when (userType) {
                        0 -> {
                            Log.d("userType","in 0")
                            navController.navigate("admin")
                        }
                        1 -> {
                            navController.navigate("student")
                        }
                        2 -> {
                            navController.navigate("mentor")
                        }
                        else -> navController.navigate(NavigationScreen.LoginScreen.route)
                    }

                }
            }
            studentGraph(
                titleState,
                topBarState,
                navController,
                studentViewModel,
                bookingViewModel
            )

            adminGraph(
                titleState,
                topBarState,
                navController,
                adminViewModel,
                studentListViewModel,
                mentorListViewModel
            )
            mentorGraph(
                topBarState,
                navController,
                mentorViewModel,
                titleState
            )
        }
    }


    private fun logout() {
        storageViewModel.deleteData()
    }
}