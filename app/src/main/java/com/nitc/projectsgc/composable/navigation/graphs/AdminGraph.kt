package com.nitc.projectsgc.composable.navigation.graphs

import androidx.activity.viewModels
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
import com.nitc.projectsgc.composable.student.screens.StudentViewModel
import com.nitc.projectsgc.composable.student.screens.ViewStudentScreen


fun NavGraphBuilder.adminGraph(navController: NavController) {

    navigation(startDestination = NavigationScreen.AdminDashboard.route, route = "admin") {
        composable(route = NavigationScreen.AdminDashboard.route) {
            val adminViewModel: AdminViewModel = viewModel()
            val mentorListViewModel: MentorListViewModel = viewModel()
            val studentListViewModel: StudentListViewModel = viewModel()

            AdminDashboardScreen(
                adminViewModel = adminViewModel,
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
            val mentorViewModel:MentorViewModel = viewModel()
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
            val studentViewModel: StudentViewModel = viewModel()
            navBackStackEntry.arguments?.getString("rollNo")
                ?.let { rollNoString ->
                    ViewStudentScreen(
                        rollNo = rollNoString,
                        studentViewModel = studentViewModel
                    )
                }
        }


        composable(route = "${NavigationScreen.ViewMentorAppointments.route}/{username}", arguments = listOf(
            navArgument("username") { type = NavType.StringType }
        )) { navBackStackEntry ->
            val mentorViewModel:MentorViewModel = viewModel()
            navBackStackEntry.arguments?.getString("username")
                ?.let { usernameString ->
                    MentorAppointmentsScreen(
                        username = usernameString,
                        mentorViewModel = mentorViewModel
                    )
                }
        }

        composable(route = "${NavigationScreen.ViewStudentAppointments.route}/{rollNo}", arguments = listOf(
            navArgument("rollNo") { type = NavType.StringType }
        )) { navBackStackEntry ->
            val studentViewModel: StudentViewModel = viewModel()
            navBackStackEntry.arguments?.getString("rollNo")
                ?.let { rollNoString ->
                    StudentAppointmentsScreen(
                        rollNo = rollNoString,
                        studentViewModel = studentViewModel
                    )
                }
        }

    }
}