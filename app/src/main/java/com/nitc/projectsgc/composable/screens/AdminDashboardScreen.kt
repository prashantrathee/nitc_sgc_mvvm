package com.nitc.projectsgc.composable.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.nitc.projectsgc.composable.components.TabLayout
import com.nitc.projectsgc.composable.login.LoginViewModel


@Composable
fun AdminDashboardScreen(
    loginViewModel: LoginViewModel,
    navController: NavController
){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val selectedTabIndex = remember {
            mutableIntStateOf(0)
        }
        TabLayout(
            tabs = listOf("Students", "Mentors"),
            fontColor = Color.Black,
            bg = Color.White,
            selectedTabIndex = selectedTabIndex
        )
        when(selectedTabIndex.intValue){
            0->{

            }
            1->{

            }
        }
    }
}