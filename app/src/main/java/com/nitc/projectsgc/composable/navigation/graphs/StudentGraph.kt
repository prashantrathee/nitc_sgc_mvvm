package com.nitc.projectsgc.composable.navigation.graphs

import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.nitc.projectsgc.composable.navigation.NavigationScreen
import com.nitc.projectsgc.composable.student.screens.BookingScreen
import com.nitc.projectsgc.composable.student.viewmodels.StudentViewModel
import com.nitc.projectsgc.composable.student.screens.StudentDashboardScreen
import com.nitc.projectsgc.composable.student.viewmodels.BookingViewModel


fun NavGraphBuilder.studentGraph(
    titleState: MutableState<String>,
    topBarState:MutableState<Boolean>,
    navController: NavController,
    studentViewModel: StudentViewModel,
    bookingViewModel: BookingViewModel
) {
    topBarState.value = true
    navigation(
        startDestination = "${NavigationScreen.StudentDashboard.route}/{rollNo}",
        route = "student"
    ) {
        composable(
            route = "${NavigationScreen.StudentDashboard.route}/{rollNo}",
            arguments = listOf(
                navArgument("rollNo") { type = NavType.StringType }
            )
        ) { navEntry ->
            val roll = navEntry.arguments?.getString("rollNo") ?: ""
            StudentDashboardScreen(roll, studentViewModel)
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
            route = "${NavigationScreen.BookingScreen.route}/{rollNo}/{studentName}",
            arguments = listOf(
                navArgument("rollNo") { type = NavType.StringType },
                navArgument("studentName") { type = NavType.StringType }
            )
        ) { navEntry ->
            val roll = navEntry.arguments?.getString("rollNo") ?: ""
            val studentName = navEntry.arguments?.getString("studentName") ?: ""
            BookingScreen(
                rollNo = roll,
                studentName = studentName,
                bookingViewModel = bookingViewModel,
                bookCallback = {
                    navController.navigate("${NavigationScreen.StudentDashboard.route}/${roll}")
                })
        }
    }
}
