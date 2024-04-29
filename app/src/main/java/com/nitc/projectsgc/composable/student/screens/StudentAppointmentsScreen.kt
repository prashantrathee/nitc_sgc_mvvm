package com.nitc.projectsgc.composable.student.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import arrow.core.Either
import com.nitc.projectsgc.R
import com.nitc.projectsgc.composable.components.RescheduleDialog
import com.nitc.projectsgc.composable.student.components.BookedAppointmentCard
import com.nitc.projectsgc.composable.student.viewmodels.BookingViewModel
import com.nitc.projectsgc.composable.student.viewmodels.StudentViewModel
import com.nitc.projectsgc.models.Appointment


@Composable
fun StudentAppointmentsScreen(
    rollNo: String,
    studentViewModel: StudentViewModel,
    bookingViewModel: BookingViewModel,
    bookCallback: () -> Unit
) {

    val density = LocalDensity.current
    val normalPadding = with(density) { dimensionResource(id = R.dimen.normal_padding) }
    val smallPadding = with(density) { dimensionResource(id = R.dimen.small_padding) }


    val myContext = LocalContext.current

    val isLoading = remember {
        mutableStateOf(true)
    }
    LaunchedEffect(Unit) {
        studentViewModel.getAppointments(rollNo)
    }

    val reschedulingState = remember {
        mutableStateOf<Appointment?>(null)
    }
    val coroutineScope = rememberCoroutineScope()
    val appointmentsEitherState by studentViewModel.appointments.collectAsState()

    val appointmentsState = remember {
        mutableStateOf(listOf<Appointment>())
    }

    val cancelState = remember {
        mutableStateOf<Appointment?>(null)
    }
    LaunchedEffect(cancelState.value) {
        if (cancelState.value != null) {
            val cancelled = studentViewModel.cancelAppointment(cancelState.value!!)
            if (cancelled) {
                showToast("Cancelled appointment", myContext)
            } else {
                showToast("Could not cancel the appointment", myContext)
            }
            cancelState.value = null
            studentViewModel.getAppointments(rollNo)
        }
    }

    LaunchedEffect(appointmentsEitherState) {
        Log.d("studentDashboard", "apointments changed")
        when (val appointmentsEither = appointmentsEitherState) {
            is Either.Right -> {
                Log.d("studentDashboard", "apointments state something")
                if (appointmentsEither.value.isEmpty()) {
                    Toast.makeText(
                        myContext,
                        "No appointments found",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    appointmentsState.value = appointmentsEither.value
                    Log.d("studentDashboard", "Appointments got")
                }
                isLoading.value = false
            }

            is Either.Left -> {
                Toast.makeText(
                    myContext,
                    (appointmentsEither.value),
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

    if (reschedulingState.value != null) {
        RescheduleDialog(
            oldAppointment = reschedulingState.value!!,
            onDismiss = {
                reschedulingState.value = null
                studentViewModel.getAppointments(rollNo)
            },
            bookingViewModel = bookingViewModel
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading.value) CircularProgressIndicator(
                Modifier
                    .fillMaxSize(0.6F)
                    .align(Alignment.Center)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = appointmentsState.value.size,
                    itemContent = { index ->
                        BookedAppointmentCard(
                            appointment = appointmentsState.value[index],
                            rescheduleCallback = {
                                reschedulingState.value = appointmentsState.value[index]
                            },
                            cancelCallback = {
                                cancelState.value = appointmentsState.value[index]
                            })
                    })
            }

            FloatingActionButton(
                onClick = {
                    bookCallback()
                },
                shape = RoundedCornerShape(25),
                containerColor = colorResource(id = R.color.navy_blue),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(normalPadding)
                    .clip(RoundedCornerShape(25))
                    .background(colorResource(id = R.color.navy_blue))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(Color.Transparent)
                        .padding(smallPadding),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(Icons.Filled.DateRange, "Book Appointment", tint = Color.White)
                    Spacer(modifier = Modifier.size(normalPadding))
                    Text(
                        text = "Book\nAppointment",
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = smallPadding)
                    )
                }
            }

        }
    }
}


@Preview
@Composable
fun StudentAppointmentsPreview() {

}