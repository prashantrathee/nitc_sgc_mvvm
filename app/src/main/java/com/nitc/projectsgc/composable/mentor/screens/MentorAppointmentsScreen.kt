package com.nitc.projectsgc.composable.mentor.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import arrow.core.Either
import com.nitc.projectsgc.R
import com.nitc.projectsgc.composable.components.DateDialog
import com.nitc.projectsgc.composable.components.RemarkDialog
import com.nitc.projectsgc.composable.mentor.MentorViewModel
import com.nitc.projectsgc.composable.mentor.components.MentorAppointmentCard
import com.nitc.projectsgc.composable.news.screens.showToast
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
    val dateState = remember {
        mutableStateOf(
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()).toString()
        )
    }
    val isLoading = remember {
        mutableStateOf(true)
    }

    val dateDialogState = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(dateState.value) {
        mentorViewModel.getMentorAppointments(
            username,
            dateState.value
        )
    }

    val coroutineScope = rememberCoroutineScope()
    val appointmentsEither = mentorViewModel.appointments.collectAsState()

    val appointmentsState = remember {
        mutableStateOf(listOf<Appointment>())
    }
    val completeAppointmentState = remember {
        mutableStateOf<Appointment?>(null)
    }
    val completeState = remember {
        mutableStateOf(false)
    }

    val cancelAppointmentState = remember {
        mutableIntStateOf(-1)
    }
    LaunchedEffect(key1 = cancelAppointmentState.intValue) {
        if(cancelAppointmentState.intValue != -1){
            val cancelled = mentorViewModel.cancelAppointment(appointmentsState.value[cancelAppointmentState.intValue])
            if(cancelled){
                showToast("Cancelled appointment",myContext)
            }else{
                showToast("Could not cancel the appointment",myContext)
            }
            cancelAppointmentState.intValue = -1
        }
    }
    LaunchedEffect(key1 = completeState.value) {
        if(completeState.value && completeAppointmentState.value != null){
            val completed = mentorViewModel.completeAppointment(completeAppointmentState.value!!)
            if(completed){
                showToast("Completed appointment",myContext)
            }else{
                showToast("Error in completing this appointment",myContext)
            }
            completeState.value = false
            completeAppointmentState.value = null
        }
    }

    LaunchedEffect(appointmentsEither.value) {
        Log.d("mentorDashboard", "apointments changed")
        when (val appointmentsEitherState = appointmentsEither.value) {
            is Either.Right -> {
                if (appointmentsEitherState.value.isEmpty()) {
                    Toast.makeText(
                        myContext,
                        "No appointments found for given date",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    appointmentsState.value = appointmentsEitherState.value
                    Log.d("mentorDashboard", "Appointments got")
                    isLoading.value = false
                }
            }

            is Either.Left -> {
                Toast.makeText(
                    myContext,
                    (appointmentsEitherState.value),
                    Toast.LENGTH_SHORT
                ).show()
                isLoading.value = false
            }

            null -> {
                isLoading.value = false
//                Toast.makeText(
//                    myContext,
//                    "Error in accessing appointments",
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading.value) CircularProgressIndicator(Modifier.fillMaxSize())

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (appointmentsState.value.isEmpty()) {
                
            } else {
                items(count = appointmentsState.value.size,
                    itemContent = { index ->
                        MentorAppointmentCard(
                            appointment = appointmentsState.value[index],
                            mentorViewModel = mentorViewModel,
                            completeCallback = { completeAppointmentState.value = appointmentsState.value[index] },
                            viewPastRecordCallback = {
                                recordCallback(appointmentsState.value[index].studentID)
                            },
                            cancelCallback = {
                                cancelAppointmentState.intValue = index
                            })
                    })
            }
        }

        FloatingActionButton(
            onClick = {
                dateDialogState.value = true
            },
            shape = RoundedCornerShape(25),
            containerColor = colorResource(id = R.color.navy_blue),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
                .clip(RoundedCornerShape(25))
                .background(colorResource(id = R.color.navy_blue))
        ) {
            if (dateDialogState.value) {
                DateDialog(
                    heading = "Choose Date",
                    isVisible = dateDialogState.value
                ) { dateChosen ->
                    dateState.value = dateChosen
                    Log.d("dateChosen", "Date chosen value + ${dateState.value}")
                    dateDialogState.value = false
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.background(Color.Transparent)
            ) {
                Icon(Icons.Filled.DateRange, "Choose Date", tint = Color.White)
                Text(
                    text = dateState.value,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 7.dp)
                )
            }
        }

    }
    if (completeAppointmentState.value != null && !completeState.value) {
        RemarkDialog(value = "", closeDialog = {
            completeAppointmentState.value = null
        }) {remark->
            completeAppointmentState.value = completeAppointmentState.value!!.copy(remarks = remark)
            completeState.value = true
        }
    }

}

@Preview
@Composable
fun MentorAppointmentsPreview() {

}