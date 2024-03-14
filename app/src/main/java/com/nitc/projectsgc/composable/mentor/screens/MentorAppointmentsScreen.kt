package com.nitc.projectsgc.composable.mentor.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import arrow.core.Either
import com.nitc.projectsgc.composable.mentor.MentorViewModel
import com.nitc.projectsgc.composable.mentor.components.MentorAppointmentCard
import com.nitc.projectsgc.models.Appointment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun MentorAppointmentsScreen(
    username: String,
    mentorViewModel: MentorViewModel,
    recordCallback: (rollNo: String) -> Unit
) {
    val myContext = LocalContext.current
    mentorViewModel.getMentorAppointments(
        username,
        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()).toString()
    )
    val appointmentsEither = mentorViewModel.appointments.collectAsState().value
    val appointmentsState = remember {
        mutableStateOf<List<Appointment>?>(null)
    }
    val studentEither = mentorViewModel.student.collectAsState().value
    LaunchedEffect(key1 = appointmentsEither) {
        when (appointmentsEither) {
            is Either.Right -> {
                appointmentsState.value = appointmentsEither.value
            }
            is Either.Left -> {
                appointmentsState.value = null
                Toast.makeText(
                    myContext,
                    (appointmentsEither.value),
                    Toast.LENGTH_LONG
                ).show()
            }
            null -> {
                appointmentsState.value = null
                Toast.makeText(
                    myContext,
                    "Error in accessing appointments",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        if(appointmentsState.value != null){
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(count = appointmentsState.value!!.size,
                    itemContent = {index->
                        mentorViewModel.getStudent(appointmentsState.value!![index].studentID)
                        when(studentEither){
                            is Either.Left->{
                                Log.d("getStudent",studentEither.value)
                            }
                            is Either.Right->{
                                MentorAppointmentCard(
                                    appointment = appointmentsState.value!![index],
                                    student = studentEither.value,
                                    rescheduleCallback = {

                                    },
                                    completeCallback = {

                                    },
                                    viewPastRecordCallback = {
                                        recordCallback(studentEither.value.rollNo)
                                    },
                                    cancelCallback = {

                                    })
                            }
                            null->{

                            }
                        }

                    })
            }
        }
    }

}

@Preview
@Composable
fun MentorAppointmentsPreview() {

}