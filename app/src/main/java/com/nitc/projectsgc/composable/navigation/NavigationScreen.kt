package com.nitc.projectsgc.composable.navigation

import androidx.annotation.StringRes
import com.nitc.projectsgc.R

sealed class NavigationScreen(val route:String, @StringRes val resID:Int){
    data object FlashScreen:NavigationScreen("flashScreen", R.string.app_name)
    data object LoginScreen:NavigationScreen("loginScreen", R.string.login_screen)
    data object AdminDashboard:NavigationScreen("adminDashboard", R.string.admin_dashboard)
    data object MentorDashboard:NavigationScreen("adminDashboard", R.string.mentor_dashboard)
    data object StudentDashboard:NavigationScreen("adminDashboard", R.string.student_dashboard)
    data object ViewStudent:NavigationScreen("viewStudent", R.string.view_student)
    data object ViewStudentAppointments:NavigationScreen("viewStudentAppointments", R.string.view_student_appointments)
    data object ViewMentor:NavigationScreen("viewMentor", R.string.view_mentor)
    data object ViewMentorAppointments:NavigationScreen("viewMentorAppointments", R.string.view_mentor_appointments)
}