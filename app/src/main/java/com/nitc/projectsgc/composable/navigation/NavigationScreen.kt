package com.nitc.projectsgc.composable.navigation

import androidx.annotation.StringRes
import com.nitc.projectsgc.R

sealed class NavigationScreen(val route:String, @StringRes val resID:Int){
    data object FlashScreen:NavigationScreen("flashScreen", R.string.app_name)
    data object LoginScreen:NavigationScreen("loginScreen", R.string.login_screen)
    data object AdminDashboard:NavigationScreen("adminDashboard", R.string.admin_dashboard)
    data object MentorDashboard:NavigationScreen("mentorDashboard", R.string.mentor_dashboard)
    data object StudentDashboard:NavigationScreen("studentDashboard", R.string.student_dashboard)
    data object BookingScreen:NavigationScreen("bookingScreen", R.string.booking_screen)
    data object RescheduleScreen:NavigationScreen("rescheduleScreen", R.string.reschedule_screen)
    data object NewsScreen:NavigationScreen("newsScreen", R.string.news_screen)
    data object ViewStudent:NavigationScreen("viewStudent", R.string.view_student)
    data object ViewStudentAppointments:NavigationScreen("viewStudentAppointments", R.string.view_student_appointments)
    data object ViewMentor:NavigationScreen("viewMentor", R.string.view_mentor)
    data object ViewMentorAppointments:NavigationScreen("viewMentorAppointments", R.string.view_mentor_appointments)
}