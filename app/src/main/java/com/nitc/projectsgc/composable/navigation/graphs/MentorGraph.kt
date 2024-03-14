package com.nitc.projectsgc.composable.navigation.graphs

import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.nitc.projectsgc.composable.mentor.MentorViewModel
import com.nitc.projectsgc.composable.mentor.screens.MentorAppointmentsScreen
import com.nitc.projectsgc.composable.mentor.screens.MentorDashboardScreen
import com.nitc.projectsgc.composable.navigation.NavigationScreen


fun NavGraphBuilder.mentorGraph(
    navController: NavController,
    mentorViewModel: MentorViewModel,
    titleState:MutableState<String>
) {
    navigation(startDestination = "${NavigationScreen.MentorDashboard.route}/{username}", route = "mentor") {
        composable(route = "${NavigationScreen.MentorDashboard.route}/{username}", arguments = listOf(
            navArgument("username"){type = NavType.StringType}
        )) {navBackStackEntry->
            titleState.value = stringResource(id = NavigationScreen.MentorDashboard.resID)
            navBackStackEntry.arguments?.getString("username")
                ?.let { usernameString ->
                    MentorDashboardScreen(
                        username = usernameString,
                        mentorViewModel = mentorViewModel,
                        navController,
                        pastRecordCallback = {rollNo ->

                        }
                    )
                }
        }
//
//        composable(route = "${NavigationScreen.ViewMentorAppointments.route}/{username}",
//            arguments = listOf(
//                navArgument("username") { type = NavType.StringType }
//            )) { navBackStackEntry ->
//            titleState.value = stringResource(id = NavigationScreen.ViewMentorAppointments.resID)
//            navBackStackEntry.arguments?.getString("username")
//                ?.let { usernameString ->
//                    MentorAppointmentsScreen(
//                        username = usernameString,
//                        mentorViewModel = mentorViewModel,
//
//                    )
//                }
//        }

    }
}
