package com.nitc.projectsgc.composable.student.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import arrow.core.Either
import com.nitc.projectsgc.composable.components.TabLayout
import com.nitc.projectsgc.composable.login.LoginViewModel
import com.nitc.projectsgc.composable.mentor.screens.MentorAppointmentsScreen
import com.nitc.projectsgc.composable.student.viewmodels.StudentViewModel
import com.nitc.projectsgc.models.Mentor
import com.nitc.projectsgc.models.Student
import kotlinx.coroutines.launch


@Composable
fun StudentDashboardScreen(
    rollNo: String,
    studentViewModel: StudentViewModel,
    goToBooking:()->Unit
) {


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val selectedTabIndex = remember {
            mutableIntStateOf(0)
        }
        TabLayout(
            tabs = listOf("Appointments", "Profile"),
            fontColor = Color.Black,
            bg = Color.White,
        ) { pageIndex ->
            when (pageIndex) {
                0 -> {
                    StudentAppointmentsScreen(
                        rollNo = rollNo,
                        studentViewModel = studentViewModel,
                        bookCallback = {
                            goToBooking()
                        }
                    )
                }

                1 -> {
                    GetProfile(rollNo, studentViewModel)
                }
            }
        }
    }
}


@Composable
fun GetProfile(rollNo: String, studentViewModel: StudentViewModel) {

    Log.d("studentDashboard", "rollNo is : $rollNo")
    studentViewModel.getProfile(rollNo)
    val screenContext = LocalContext.current
    val updatingState = remember {
        mutableStateOf(false)
    }
    val studentState = remember {
        mutableStateOf<Student?>(null)
    }
    val studentEither = studentViewModel.profile.collectAsState().value
    var oldPassword = ""
    val coroutineScope = rememberCoroutineScope()
    when (studentEither) {
        is Either.Left -> {
            Toast.makeText(
                LocalContext.current,
                "Error in getting student : ${studentEither.value}",
                Toast.LENGTH_LONG
            ).show()
        }

        is Either.Right -> {
            Log.d("viewMentor", "no error message")
            studentState.value = studentEither.value
            oldPassword = studentEither.value.password
            UpdateStudentScreen(studentEither.value) { updatedStudent ->
                Log.d("updateMentor", "these are new values ; $updatedStudent")
                coroutineScope.launch {
                    Log.d("updateMentor", "Now updated : $updatedStudent")
                    val updateSuccess =
                        studentViewModel.updateProfile(updatedStudent, oldPassword)
                    if (updateSuccess) {
                        studentState.value = updatedStudent
                        Toast.makeText(screenContext, "Updated Mentor", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(
                            screenContext,
                            "Error in updating student",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    updatingState.value = false
                }
            }
        }

        null -> {
            Log.d("viewMentor", "Mentor either is null")
        }
    }
}