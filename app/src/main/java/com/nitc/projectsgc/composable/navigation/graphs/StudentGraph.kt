package com.nitc.projectsgc.composable.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nitc.projectsgc.composable.navigation.NavigationScreen
import com.nitc.projectsgc.composable.student.StudentViewModel
import com.nitc.projectsgc.composable.student.screens.StudentDashboardScreen


fun NavGraphBuilder.studentGraph(navController: NavController,studentViewModel: StudentViewModel) {
    navigation(startDestination = NavigationScreen.StudentDashboard.route, route = "student") {
        composable(route = NavigationScreen.StudentDashboard.route) {
            StudentDashboardScreen(
            )
        }

    }
}
