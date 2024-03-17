package com.nitc.projectsgc.composable.student.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import arrow.core.Either
import com.nitc.projectsgc.R
import com.nitc.projectsgc.composable.components.BasicSubHeadingButton
import com.nitc.projectsgc.composable.components.CardInputFieldWithValue
import com.nitc.projectsgc.composable.components.DateDialog
import com.nitc.projectsgc.composable.components.SubHeadingText
import com.nitc.projectsgc.composable.student.viewmodels.BookingViewModel
import com.nitc.projectsgc.models.Appointment
import com.nitc.projectsgc.models.Mentor


@Composable
fun BookingScreen(
    rollNo: String,
    bookingViewModel: BookingViewModel,
    bookCallback: () -> Unit
) {
    val appointmentState = remember {
        mutableStateOf(Appointment(studentID = rollNo))
    }
    val bookingContext = LocalContext.current

    val mentorTypesDropdownState = remember {
        mutableStateOf(false)
    }
    val mentorTypesState = remember {
        mutableStateOf<List<String>?>(null)
    }
    LaunchedEffect(Unit) {
        bookingViewModel.getMentorTypes()
        bookingViewModel.mentorTypes.collect { mentorTypesEither ->
            when (mentorTypesEither) {
                is Either.Left -> {
                    showToast(mentorTypesEither.value, bookingContext)
                }

                is Either.Right -> {
                    mentorTypesState.value = mentorTypesEither.value
                    if (mentorTypesState.value.isNullOrEmpty()) {
                        showToast("No mentor types found", bookingContext)
                    }
                }

                null -> {
//                    showToast("Error in getting mentor types",bookingContext)
                }
            }
        }
    }
    val mentorsState = remember {
        mutableStateOf<List<Mentor>?>(null)
    }
    val mentorsDropdownState = remember {
        mutableStateOf(false)
    }
    val dateState = remember {
        mutableStateOf(false)
    }

    val timeSlotsDropdownState = remember {
        mutableStateOf(false)
    }
    val timeSlotsState = remember {
        mutableStateOf(emptyList<String>())
    }

    val bookAppointmentState = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(bookAppointmentState.value) {
        if (bookAppointmentState.value) {
            val booked = bookingViewModel.bookAppointment(appointmentState.value)
            if (booked) {
                showToast("Booked your appointment", bookingContext)
                bookCallback()
            } else {
                showToast("Error in booking your appointment", bookingContext)
            }
        }
    }
//    val mentorsEitherState by bookingViewModel.mentors.collectAsState()
    val getMentorState = remember {
        mutableStateOf(false)
    }
    val getTimeSlotsState = remember {
        mutableStateOf(false)
    }
    val mentorsEitherState by bookingViewModel.mentors.collectAsState()
    LaunchedEffect(getMentorState.value) {
        if (getMentorState.value) {
            bookingViewModel.getMentors(appointmentState.value.mentorType)
        }
    }
    LaunchedEffect(key1 = mentorsEitherState) {
//        bookingViewModel.mentors.collect { mentorsEither ->
        if (getMentorState.value) {

            getMentorState.value = false
            when (val mentorsEither = mentorsEitherState) {

//            Log.d("getMentorNames", "In launched effect")
//            if (appointment.value.mentorType.isNotEmpty()) {
//                when (mentorsEither) {
                is Either.Left -> {
                    Toast.makeText(bookingContext, mentorsEither.value, Toast.LENGTH_LONG)
                        .show()
                }

                is Either.Right -> {
                    mentorsState.value = mentorsEither.value
                    mentorsDropdownState.value = true
                }

                null -> {
                    Toast.makeText(
                        bookingContext,
                        "Error in getting mentors",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }

    val timeSlotsEitherState by bookingViewModel.availableTimeSlots.collectAsState()

    LaunchedEffect(getTimeSlotsState.value) {
        if (getTimeSlotsState.value) {
            bookingViewModel.getAvailableTimeSlots(
                appointmentState.value.mentorType,
                appointmentState.value.mentorID,
                appointmentState.value.date
            )

        }
    }
    LaunchedEffect(key1 = timeSlotsEitherState) {
        if (getTimeSlotsState.value) {
            getTimeSlotsState.value = false
            when (val timeSlotsEither = timeSlotsEitherState) {
                is Either.Right -> {
                    timeSlotsState.value = timeSlotsEither.value
                    timeSlotsDropdownState.value = true
                }

                is Either.Left -> {
                    showToast(
                        timeSlotsEither.value,
                        bookingContext
                    )
                }

                null -> {
                    showToast(
                        "Error in getting time slots",
                        bookingContext,
                    )
                }
            }
        }
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Box {
                BasicSubHeadingButton(
                    text = appointmentState.value.mentorType.ifEmpty { "Select Mentor Type" },
                    tc = Color.White,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.navy_blue)
                    ),
                    clickCallback = {
                        mentorTypesDropdownState.value = true
                    },
                    modifier = Modifier
                )


                DropdownMenu(expanded = mentorTypesDropdownState.value,
                    onDismissRequest = {
                        mentorTypesDropdownState.value = false
                    }) {
                    if (!mentorTypesState.value.isNullOrEmpty()) {
                        mentorTypesState.value!!.forEachIndexed { index, mentorType ->
                            DropdownMenuItem(text = {
                                Text(text = mentorType, color = Color.Black)
                            }, onClick = {
                                appointmentState.value =
                                    appointmentState.value.copy(mentorType = mentorType)
                                mentorTypesDropdownState.value = false
                            })
                        }
                    }
                }
            }

        }
        item {
            Box {
                BasicSubHeadingButton(
                    clickCallback = {
                        if (appointmentState.value.mentorType.isNotEmpty()) {
                            getMentorState.value = true
//                            bookingViewModel.getMentors(
//                                appointment.value.mentorType
//                            )
                            mentorsDropdownState.value = true
                        } else showToast("Choose a mentorship type first", bookingContext)
                    },
                    text = appointmentState.value.mentorName.ifEmpty { "Select Mentor" },
                    modifier = Modifier,
                    tc = Color.Black,
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.light_gray))
                )

//                if(!mentorsState.value.isNullOrEmpty()) {
                DropdownMenu(expanded = mentorsDropdownState.value, onDismissRequest = {
                    mentorsDropdownState.value = false
                }) {
                    if (!getMentorState.value && !mentorsState.value.isNullOrEmpty()) {
                        mentorsState.value!!.forEachIndexed { index, mentor ->
                            DropdownMenuItem(text = {
                                Text(text = mentor.name, color = Color.Black)
                            }, onClick = {
                                appointmentState.value = appointmentState.value.copy(
                                    mentorID = mentor.userName,
                                    mentorName = mentor.name
                                )
                                mentorsDropdownState.value = false
                            })
                        }
                    }
//                    }
                }
            }
        }
        item {
            Box {
                BasicSubHeadingButton(
                    text = appointmentState.value.date.ifEmpty { "Select Date" },
                    colors = ButtonDefaults.buttonColors(),
                    tc = Color.White,
                    modifier = Modifier
                ) {
                    dateState.value = true
                }
                if (dateState.value) {
                    DateDialog(
                        heading = "Book for date",
                        isVisible = dateState.value
                    ) { dateChosen ->
                        appointmentState.value = appointmentState.value.copy(date = dateChosen)
                        dateState.value = false
                    }
                }
            }
        }
        item {

            Box(modifier = Modifier) {
                BasicSubHeadingButton(
                    text = appointmentState.value.timeSlot.ifEmpty { "Select Time Slot" },
                    colors = ButtonDefaults.buttonColors(),
                    tc = Color.White,
                    modifier = Modifier
                ) {
                    if (appointmentState.value.mentorID.isNotEmpty() && appointmentState.value.date.isNotEmpty() && appointmentState.value.mentorType.isNotEmpty()) {
                        getTimeSlotsState.value = true
                        timeSlotsDropdownState.value = true
                    } else {
                        showToast("Choose mentorship type and mentor first", bookingContext)
                    }
                }
                if (!getTimeSlotsState.value && timeSlotsState.value.isNotEmpty()) {
                    DropdownMenu(expanded = timeSlotsDropdownState.value, onDismissRequest = {
                        timeSlotsDropdownState.value = false
                    }) {
                        if (timeSlotsState.value.isNotEmpty()) {
                            timeSlotsState.value.forEachIndexed { index, timeSlot ->
                                DropdownMenuItem(text = {
                                    SubHeadingText(
                                        text = timeSlot,
                                        fontColor = Color.Black,
                                        modifier = Modifier
                                    )
                                }, onClick = {
                                    appointmentState.value = appointmentState.value.copy(timeSlot = timeSlot)
                                    timeSlotsDropdownState.value = false
                                })
                            }
                        } else showToast(
                            "No Time slots available for the given date",
                            bookingContext
                        )
                    }
                }
            }
        }

        item {
            CardInputFieldWithValue(
                hint = "Problem Description",
                text = "",
                isPassword = false,
                modifier = Modifier.fillMaxWidth(0.7F)
            ) { problemDesc ->
                appointmentState.value = appointmentState.value.copy(problemDescription = problemDesc)
            }

        }

        item {

            BasicSubHeadingButton(
                text = "Submit", colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.navy_blue)
                ), tc = Color.White, modifier = Modifier
            ) {
                if (appointmentState.value.problemDescription.isEmpty()) {
                    Toast.makeText(
                        bookingContext,
                        "Write your problem description",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else {
                    appointmentState.value = appointmentState.value.copy(status = "Booked")
                    bookAppointmentState.value = true

                }
            }
            Spacer(modifier = Modifier.size(100.dp))
        }
    }
}

fun showToast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}