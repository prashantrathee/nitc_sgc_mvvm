package com.nitc.projectsgc.composable.mentor.screens

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
import com.nitc.projectsgc.composable.mentor.MentorViewModel
import com.nitc.projectsgc.models.Mentor
import kotlinx.coroutines.launch


@Composable
fun MentorDashboardScreen(
    username: String,
    mentorViewModel: MentorViewModel,
    pastRecordCallback: (rollNo: String) -> Unit
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
                    MentorAppointmentsScreen(
                        username = username,
                        mentorViewModel = mentorViewModel,
                        recordCallback = { studentRoll ->
                            pastRecordCallback(studentRoll)
                        }
                    )
                }

                1 -> {
                    GetProfile(username = username, mentorViewModel = mentorViewModel)
                }
            }
        }
    }
}

@Composable
fun GetProfile(username: String, mentorViewModel: MentorViewModel) {
    val mentorState = remember {
        mutableStateOf<Mentor?>(if (username != "no") null else Mentor())
    }
    Log.d("mentorDashboard","username is : $username")
    mentorViewModel.getProfile(username)
    val screenContext = LocalContext.current
    val updatingState = remember {
        mutableStateOf(false)
    }
    val mentorEither = mentorViewModel.mentor.collectAsState().value
    var oldPassword = ""
    val coroutineScope = rememberCoroutineScope()
    when (mentorEither) {
        is Either.Left -> {
            Toast.makeText(
                LocalContext.current,
                "Error in getting mentor : ${mentorEither.value}",
                Toast.LENGTH_LONG
            ).show()
        }

        is Either.Right -> {
            Log.d("viewMentor", "no error message")
            mentorState.value = mentorEither.value
            oldPassword = mentorEither.value.password
            UpdateMentorScreen(mentorEither.value) { updatedMentor ->
                Log.d("updateMentor", "these are new values ; $updatedMentor")
                coroutineScope.launch {
                    Log.d("updateMentor", "Now updated : $updatedMentor")
                    val updateSuccess =
                        mentorViewModel.updateProfile(updatedMentor, oldPassword)
                    if (updateSuccess) {
                        mentorState.value = updatedMentor
                        Toast.makeText(screenContext, "Updated Mentor", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(
                            screenContext,
                            "Error in updating mentor",
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

