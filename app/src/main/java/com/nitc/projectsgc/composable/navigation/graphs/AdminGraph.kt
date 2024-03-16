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
import com.nitc.projectsgc.composable.admin.AdminDashboardScreen
import com.nitc.projectsgc.composable.admin.viewmodels.AdminViewModel
import com.nitc.projectsgc.composable.admin.viewmodels.MentorListViewModel
import com.nitc.projectsgc.composable.admin.viewmodels.StudentListViewModel
import com.nitc.projectsgc.composable.mentor.screens.ViewMentorScreen
import com.nitc.projectsgc.composable.navigation.NavigationScreen
import com.nitc.projectsgc.composable.student.screens.StudentAppointmentsScreen
import com.nitc.projectsgc.composable.student.screens.ViewStudentScreen


fun NavGraphBuilder.adminGraph(
    titleState: MutableState<String>,
    topBarState: MutableState<Boolean>,
    navController: NavController,
    adminViewModel: AdminViewModel,
    studentListViewModel: StudentListViewModel,
    mentorListViewModel: MentorListViewModel
) {
    navigation(startDestination = NavigationScreen.AdminDashboard.route, route = "admin") {
        Log.d("adminDashboard", "In the navigation of admin")
        composable(route = NavigationScreen.AdminDashboard.route) {
            Log.d("adminDashboard", "In admin dashboard composabled")
            topBarState.value = true
//            navController.popBackStack(
//                inclusive = false,
//                route = NavigationScreen.AdminDashboard.route
//            )
            titleState.value = stringResource(id = NavigationScreen.AdminDashboard.resID)
            AdminDashboardScreen(
                adminViewModel = adminViewModel,
                navController = navController,
                mentorListViewModel = mentorListViewModel,
                studentListViewModel = studentListViewModel,
                viewStudentCallback = { rollNo ->
                    navController.navigate(NavigationScreen.ViewStudent.route + "/" + rollNo)
                },
                viewMentorCallback = { username ->
                    navController.navigate("${NavigationScreen.ViewMentor.route}/${username}")
                },
                addMentorCallback = {
                    navController.navigate(NavigationScreen.ViewMentor.route + "/no")
                },
                addStudentCallback = {
                    navController.navigate(NavigationScreen.ViewStudent.route + "/no")
                }
            )
        }

        composable(route = "${NavigationScreen.ViewMentor.route}/{username}", arguments = listOf(
            navArgument("username") { type = NavType.StringType }
        )) { navBackStackEntry ->
            topBarState.value = true
            titleState.value = stringResource(id = NavigationScreen.ViewMentor.resID)
            val usernameString = navBackStackEntry.arguments?.getString("username") ?: "no"
            ViewMentorScreen(
                username = usernameString,
                adminViewModel = adminViewModel
            )
        }

        composable(route = "${NavigationScreen.ViewStudent.route}/{rollNo}", arguments = listOf(
            navArgument("rollNo") { type = NavType.StringType }
        )) { navBackStackEntry ->
            topBarState.value = true
            titleState.value = stringResource(id = NavigationScreen.ViewStudent.resID)
            val rollNoString = navBackStackEntry.arguments?.getString("rollNo") ?: "no"
            ViewStudentScreen(rollNoString, adminViewModel)
        }



        composable(route = "${NavigationScreen.ViewStudentAppointments.route}/{rollNo}",
            arguments = listOf(
                navArgument("rollNo") { type = NavType.StringType }
            )) { navBackStackEntry ->
            topBarState.value = true
            titleState.value = stringResource(id = NavigationScreen.ViewStudentAppointments.resID)
            navBackStackEntry.arguments?.getString("rollNo")
                ?.let { rollNoString ->
                    StudentAppointmentsScreen(
                        rollNo = rollNoString,
                        studentListViewModel = studentListViewModel
                    )
                }
        }

    }
}