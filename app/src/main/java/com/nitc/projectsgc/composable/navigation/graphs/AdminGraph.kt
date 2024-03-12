package com.nitc.projectsgc.composable.navigation.graphs

import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.nitc.projectsgc.composable.admin.AdminDashboardScreen
import com.nitc.projectsgc.composable.admin.AdminViewModel
import com.nitc.projectsgc.composable.admin.viewmodels.MentorListViewModel
import com.nitc.projectsgc.composable.admin.viewmodels.StudentListViewModel
import com.nitc.projectsgc.composable.mentor.MentorAppointmentsScreen
import com.nitc.projectsgc.composable.mentor.MentorViewModel
import com.nitc.projectsgc.composable.mentor.ViewMentorScreen
import com.nitc.projectsgc.composable.navigation.NavigationScreen
import com.nitc.projectsgc.composable.student.screens.StudentAppointmentsScreen
import com.nitc.projectsgc.composable.student.StudentViewModel
import com.nitc.projectsgc.composable.student.screens.ViewStudentScreen


fun NavGraphBuilder.adminGraph(
    titleState: MutableState<String>,
    navController: NavController,
    adminViewModel: AdminViewModel,
    studentListViewModel: StudentListViewModel,
    mentorListViewModel: MentorListViewModel
) {

    navigation(startDestination = NavigationScreen.AdminDashboard.route, route = "admin") {
        composable(route = NavigationScreen.AdminDashboard.route) {
//            navController.popBackStack(
//                inclusive = false,
//                route = NavigationScreen.AdminDashboard.route
//            )
//            val adminViewModel: AdminViewModel = viewModel()
//            val mentorListViewModel: MentorListViewModel = viewModel()
//            val studentListViewModel: StudentListViewModel = viewModel()
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
                    navController.navigate("${NavigationScreen.ViewMentor.route}/$username")
                },
                studentAppointmentsCallback = { rollNo ->

                },
                mentorAppointmentsCallback = { username ->

                }
            )
        }

        composable(route = "${NavigationScreen.ViewMentor.route}/{username}", arguments = listOf(
            navArgument("username") { type = NavType.StringType }
        )) { navBackStackEntry ->
            titleState.value = stringResource(id = NavigationScreen.ViewMentor.resID)
            val mentorViewModel: MentorViewModel = viewModel()
            navBackStackEntry.arguments?.getString("username")
                ?.let { usernameString ->
                    ViewMentorScreen(
                        username = usernameString,
                        mentorViewModel = mentorViewModel
                    )
                }
        }

        composable(route = "${NavigationScreen.ViewStudent.route}/{rollNo}", arguments = listOf(
            navArgument("rollNo") { type = NavType.StringType }
        )) { navBackStackEntry ->
            titleState.value = stringResource(id = NavigationScreen.ViewStudent.resID)
            navBackStackEntry.arguments?.getString("rollNo")
                ?.let { rollNoString ->
                    ViewStudentScreen(rollNoString, adminViewModel)
                }
        }


        composable(route = "${NavigationScreen.ViewMentorAppointments.route}/{username}",
            arguments = listOf(
                navArgument("username") { type = NavType.StringType }
            )) { navBackStackEntry ->
            titleState.value = stringResource(id = NavigationScreen.ViewMentorAppointments.resID)
            navBackStackEntry.arguments?.getString("username")
                ?.let { usernameString ->
                    MentorAppointmentsScreen(
                        username = usernameString,
                        mentorListViewModel = mentorListViewModel
                    )
                }
        }

        composable(route = "${NavigationScreen.ViewStudentAppointments.route}/{rollNo}",
            arguments = listOf(
                navArgument("rollNo") { type = NavType.StringType }
            )) { navBackStackEntry ->
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