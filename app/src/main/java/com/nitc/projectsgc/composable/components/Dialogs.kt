package com.nitc.projectsgc.composable.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import arrow.core.Either
import com.nitc.projectsgc.R
import com.nitc.projectsgc.composable.student.screens.showToast
import com.nitc.projectsgc.composable.student.viewmodels.BookingViewModel
import com.nitc.projectsgc.composable.util.DateUtils
import com.nitc.projectsgc.models.Appointment


@Composable
fun RemarkDialog(value: String, closeDialog: () -> Unit, setValue: (String) -> Unit) {

    val txtFieldError = remember { mutableStateOf("") }
    val txtField = remember { mutableStateOf(value) }

    Dialog(onDismissRequest = { closeDialog() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 0.dp
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Complete Appointment",
                            style = TextStyle(
                                fontSize = 21.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "",
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { closeDialog() }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    CardInputFieldWithValue(
                        modifier = Modifier
                            .fillMaxWidth(),
                        hint = "Remarks",
                        isPassword = false,
                        text = "",
                        onValueChanged = {

                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

//                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                    BasicButton(
                        clickCallback = {
                            if (txtField.value.isEmpty()) {
                                txtFieldError.value = "Field can not be empty"
                            } else {
                                setValue(txtField.value)
                                closeDialog()
                            }
                        },
                        tc = Color.White,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.navy_blue)
                        ),
                        modifier = Modifier,
                        text = "Done"
                    )
//                    }
                }
            }
        }
    }
}
//
//@Preview
//@Composable
//fun RemarkDialogPreview(){
//    RemarkDialog(value = "Remarks", setShowDialog = {}) {
//
//    }
//}


//@Preview
//@Composable
//fun DateDialogPreview(){
//    DateDialog("Reschedule",true) {
//
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDialog(
    heading: String,
    isVisible: Boolean,
    dateSelected: (String) -> Unit
) {

    val datePickerState = rememberDatePickerState()
    val dateState = remember {
        mutableStateOf("")
    }
    val dateDialogState = remember {
        mutableStateOf(isVisible)
    }
    if (dateDialogState.value) {
        DatePickerDialog(
            modifier = Modifier.fillMaxSize(1F),
            shape = RoundedCornerShape(5),
            onDismissRequest = {
                dateDialogState.value = false
            }, confirmButton = {
                if (datePickerState.selectedDateMillis != null) {
                    dateState.value = DateUtils().dateToString(datePickerState.selectedDateMillis!!)
                    dateDialogState.value = false
                    dateSelected(dateState.value)
                } else {
//                Toast.makeText(LocalContext.current,"Select date first",Toast.LENGTH_LONG).show()
                }
            }) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.fillMaxSize().padding(10.dp),
                title = {
                    HeadingText(text = heading, fontColor = Color.Black, modifier = Modifier)
                },
                headline = {
                    Text(dateState.value)
                },
                showModeToggle = true
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RescheduleDialog(
    oldAppointment: Appointment,
    onDismiss: () -> Unit,
    bookingViewModel: BookingViewModel
) {
    val appointmentState = remember {
        mutableStateOf(oldAppointment)
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

    val rescheduleAppointmentState = remember {
        mutableStateOf(false)
    }
    val getTimeSlotsState = remember {
        mutableStateOf(false)
    }

    val bookingContext = LocalContext.current


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
    LaunchedEffect(key1 = rescheduleAppointmentState.value) {
        if(rescheduleAppointmentState.value){
            val rescheduled = bookingViewModel.rescheduleAppointment(oldAppointment,appointmentState.value)
            if (rescheduled) {
                showToast("Rescheduled your appointment", bookingContext)
                onDismiss()
            } else {
                showToast("Error in rescheduling your appointment", bookingContext)
            }
        }
    }
    ModalBottomSheet(
        modifier = Modifier
            .fillMaxSize(),
        onDismissRequest = {
            onDismiss()
        },
        sheetState = rememberModalBottomSheetState(false),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HeadingText(
                text = "Reschedule Appointment",
                fontColor = Color.Black,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.size(15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(0.8F),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                BasicSubHeadingButton(
                    text = appointmentState.value.mentorType,
                    tc = Color.White,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.navy_blue)
                    ),
                    clickCallback = {
                    },
                    modifier = Modifier
                )
                BasicSubHeadingButton(
                    clickCallback = {
                    },
                    text = appointmentState.value.mentorName,
                    modifier = Modifier,
                    tc = Color.Black,
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.light_gray))
                )
            }

            Spacer(modifier = Modifier.size(15.dp))
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
            Spacer(modifier = Modifier.size(15.dp))
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
                                    appointmentState.value =
                                        appointmentState.value.copy(timeSlot = timeSlot)
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
            Spacer(modifier = Modifier.size(15.dp))
            BasicSubHeadingButton(
                text = "Reschedule", colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.navy_blue)
                ), tc = Color.White, modifier = Modifier
            ) {
                appointmentState.value = appointmentState.value.copy(status = "Rescheduled")
                rescheduleAppointmentState.value = true
            }
        }
    }
}
