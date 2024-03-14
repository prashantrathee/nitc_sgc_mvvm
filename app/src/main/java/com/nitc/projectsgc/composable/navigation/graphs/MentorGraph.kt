package com.nitc.projectsgc.composable.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nitc.projectsgc.composable.mentor.screens.MentorDashboardScreen
import com.nitc.projectsgc.composable.navigation.NavigationScreen


fun NavGraphBuilder.mentorGraph(navController: NavController) {
    navigation(startDestination = NavigationScreen.MentorDashboard.route, route = "mentor") {
        composable(route = NavigationScreen.MentorDashboard.route) {
            MentorDashboardScreen(
            )
        }

    }
}
