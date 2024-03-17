package com.nitc.projectsgc.composable.navigation.graphs

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.nitc.projectsgc.composable.navigation.NavigationScreen
import com.nitc.projectsgc.composable.student.screens.BookingScreen
import com.nitc.projectsgc.composable.student.screens.StudentAppointmentsScreen
import com.nitc.projectsgc.composable.student.viewmodels.StudentViewModel
import com.nitc.projectsgc.composable.student.screens.StudentDashboardScreen
import com.nitc.projectsgc.composable.student.viewmodels.BookingViewModel
import com.nitc.projectsgc.models.Appointment


fun NavGraphBuilder.studentGraph(
    titleState: MutableState<String>,
    topBarState: MutableState<Boolean>,
    navController: NavController,
    studentViewModel: StudentViewModel,
    bookingViewModel: BookingViewModel
) {
    navigation(
        startDestination = "${NavigationScreen.StudentDashboard.route}/{rollNo}",
        route = "student/{rollNo}"
    ) {
        composable(
            route = "${NavigationScreen.StudentDashboard.route}/{rollNo}",
            arguments = listOf(
                navArgument("rollNo") { type = NavType.StringType }
            )
        ) { navEntry ->
            titleState.value = stringResource(NavigationScreen.StudentDashboard.resID)
            topBarState.value = true
            val roll = navEntry.arguments?.getString("rollNo") ?: ""
            Log.d("studentDashboard", "Roll no found : $roll")
            StudentDashboardScreen(
                roll,
                studentViewModel,
                bookingViewModel,
                goToBooking = {
                    navController.navigate(NavigationScreen.BookingScreen.route + "/${roll}")
                }
            )
        }
//        composable(
//            route = "${NavigationScreen.RescheduleScreen.route}/{rollNo}",
//            arguments = listOf(
//                navArgument("rollNo") { type = NavType.StringType }
//            )
//        ) { navEntry ->
//            navEntry.arguments?.getString("rollNo").let {roll->
//                StudentDashboardScreen(roll)
//            }
//        }
        composable(
            route = "${NavigationScreen.BookingScreen.route}/{rollNo}",
            arguments = listOf(
                navArgument("rollNo") { type = NavType.StringType },
            )
        ) { navEntry ->
            titleState.value = stringResource(NavigationScreen.BookingScreen.resID)
            val roll = navEntry.arguments?.getString("rollNo") ?: ""
            BookingScreen(
                rollNo = roll,
                bookingViewModel = bookingViewModel,
                bookCallback = {
                    navController.popBackStack(NavigationScreen.BookingScreen.route, true)
                    navController.navigate("${NavigationScreen.StudentDashboard.route}/${roll}")
                })
        }

    }
}
