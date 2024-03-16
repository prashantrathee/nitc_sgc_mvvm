package com.nitc.projectsgc.composable.student.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import arrow.core.Either
import com.nitc.projectsgc.R
import com.nitc.projectsgc.composable.components.BasicButton
import com.nitc.projectsgc.composable.components.CardInputFieldWithValue
import com.nitc.projectsgc.composable.components.DateDialog
import com.nitc.projectsgc.composable.components.SimpleToast
import com.nitc.projectsgc.composable.components.SubHeadingText
import com.nitc.projectsgc.composable.student.viewmodels.BookingViewModel
import com.nitc.projectsgc.models.Appointment
import com.nitc.projectsgc.models.Mentor


@Composable
fun BookingScreen(
    rollNo: String,
    bookingViewModel: BookingViewModel,
    bookCallback:()->Unit
) {
    val appointment = remember {
        mutableStateOf(Appointment(studentID = rollNo))
    }
    val bookingContext = LocalContext.current

    val mentorTypesDropdownState = remember {
        mutableStateOf(false)
    }
    val mentorTypesState = remember {
        mutableStateOf<List<String>?>(null)
    }
    bookingViewModel.getMentorTypes()
    when(val mentorTypesEither = bookingViewModel.mentorTypes.collectAsState().value){
        is Either.Left->{
            SimpleToast(mentorTypesEither.value)
        }
        is Either.Right->{
            mentorTypesState.value = mentorTypesEither.value
            if(mentorTypesState.value.isNullOrEmpty()){
                SimpleToast("No mentor types found")
            }
        }
        null->{
            SimpleToast("Error in getting mentor types")
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
        mutableStateOf<List<String>?>(null)
    }

    val bookAppointmentState = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = bookAppointmentState) {
        val booked = bookingViewModel.bookAppointment(appointment.value)
        if(booked){
            bookCallback()
        }else{
            Toast.makeText(bookingContext,"Error in booking your appointment",Toast.LENGTH_LONG).show()
        }
    }
    LaunchedEffect(key1 = bookingViewModel.mentors) {
        when(val mentorsEitherState = bookingViewModel.mentors.value){
            is Either.Left->{
                Toast.makeText(bookingContext,mentorsEitherState.value,Toast.LENGTH_LONG).show()
            }
            is Either.Right->{
                mentorsState.value = mentorsEitherState.value
            }
            null->{
                Toast.makeText(bookingContext,"Error in getting mentors",Toast.LENGTH_LONG).show()
            }
        }
    }

    LaunchedEffect(key1 = bookingViewModel.availableTimeSlots) {
        when(val timeSlotsEither = bookingViewModel.availableTimeSlots.value){
            is Either.Right->{
                timeSlotsState.value = timeSlotsEither.value
            }
            is Either.Left->{
                Toast.makeText(bookingContext,timeSlotsEither.value,Toast.LENGTH_LONG).show()
            }
            null->{
                Toast.makeText(bookingContext,"Error in getting time slots",Toast.LENGTH_LONG).show()
            }
        }
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box {
            Button(onClick = {
                mentorTypesDropdownState.value = true
            }) {
                SubHeadingText(
                    text = "Select Mentor type",
                    fontColor = Color.Black,
                    modifier = Modifier
                )
            }

            DropdownMenu(expanded = mentorTypesDropdownState.value, onDismissRequest = {
                mentorTypesDropdownState.value = false
            }) {
                if(!mentorTypesState.value.isNullOrEmpty()){
                    mentorTypesState.value!!.forEachIndexed { index, mentorType ->
                        DropdownMenuItem(text = {
                            Text(text = mentorType,color = Color.Black)
                        }, onClick = {
                            appointment.value = appointment.value.copy(mentorType = mentorType)
                            mentorTypesDropdownState.value = false
                        })
                    }
                }
            }
        }
        Box {
            Button(onClick = {
                if(appointment.value.mentorType.isNotEmpty()) bookingViewModel.getMentors(appointment.value.mentorType)
                mentorsDropdownState.value = true
            }) {
                SubHeadingText(
                    text = "Select Mentor",
                    fontColor = Color.Black,
                    modifier = Modifier
                )
            }

            DropdownMenu(expanded = mentorsDropdownState.value, onDismissRequest = {
                mentorsDropdownState.value = false
            }) {
                if(!mentorsState.value.isNullOrEmpty()){
                    mentorsState.value!!.forEachIndexed { index, mentor ->
                        DropdownMenuItem(text = {
                            Text(text = mentor.name,color = Color.Black)
                        }, onClick = {
                            appointment.value = appointment.value.copy(mentorID = mentor.userName, mentorName = mentor.name)
                            mentorsDropdownState.value = false
                        })
                    }
                }
            }
        }
        BasicButton(text = "Select Date", colors = ButtonDefaults.buttonColors(), tc = Color.White, modifier = Modifier) {
            dateState.value = true
        }
        DateDialog(heading = "Book for date",isVisible = dateState.value) { dateChosen->
            appointment.value = appointment.value.copy(date=dateChosen)
            dateState.value = false
        }
        Box(modifier = Modifier){
            BasicButton(text = "Select Time Slot", colors = ButtonDefaults.buttonColors(), tc = Color.White, modifier = Modifier) {
                if(appointment.value.mentorID.isNotEmpty() && appointment.value.date.isNotEmpty() && appointment.value.mentorType.isNotEmpty()){
                    bookingViewModel.getAvailableTimeSlots(
                        appointment.value.mentorType,
                        appointment.value.mentorID,
                        appointment.value.date
                    )
                    timeSlotsDropdownState.value = true
                }
            }
            DropdownMenu(expanded = timeSlotsDropdownState.value, onDismissRequest = {
                timeSlotsDropdownState.value = false
            }) {
                if(!timeSlotsState.value.isNullOrEmpty()){
                    timeSlotsState.value!!.forEachIndexed{index,timeSlot->
                        DropdownMenuItem(text = {
                            SubHeadingText(text = timeSlot, fontColor = Color.Black, modifier = Modifier)
                        }, onClick = {
                            appointment.value = appointment.value.copy(timeSlot = timeSlot)
                            timeSlotsDropdownState.value = false
                        })
                    }
                }
            }
        }

        CardInputFieldWithValue(
            hint = "Problem Description",
            text = "",
            isPassword = false,
            modifier = Modifier.fillMaxWidth(0.7F)
        ) {problemDesc->
            appointment.value = appointment.value.copy(problemDescription = problemDesc)
        }


        BasicButton(text = "Submit", colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.navy_blue)
        ), tc = Color.White, modifier = Modifier) {
            if(appointment.value.problemDescription.isEmpty()){
                Toast.makeText(bookingContext,"Write your problem description",Toast.LENGTH_LONG).show()
            }else{
                appointment.value = appointment.value.copy(status = "Booked")

            }
        }
    }
}