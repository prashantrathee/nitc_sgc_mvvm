package com.nitc.projectsgc.composable.mentor.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nitc.projectsgc.R
import com.nitc.projectsgc.composable.components.BasicButton
import com.nitc.projectsgc.composable.components.SubHeadingText
import com.nitc.projectsgc.models.Appointment
import com.nitc.projectsgc.models.Student


@Preview
@Composable
fun MentorAppointmentCardPreview() {
    MentorAppointmentCard(
        appointment = Appointment(
            date = "24/11/2111",
            mentorID = "sakshi_health_2",
            mentorName = "Sakshi",
            studentID = "prashant_m210704ca",
            status = "Booked",
            remarks = "",
            problemDescription = "This is the problem hsdfhs dfhsdf h",
            rescheduled = false,
            timeSlot = "4-5",
            mentorType = "Success"
        ),
        student = Student(
            "Phone",
            "Name",
            "Career",
            "24/11/2311",
            "email sdfsf",
            "Male",
            "password",
            "248545345234"
        ),
        {},
        {},
        {},
        {},
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MentorAppointmentCard(
    appointment: Appointment,
    student: Student,
    rescheduleCallback: () -> Unit,
    completeCallback: () -> Unit,
    viewPastRecordCallback: () -> Unit,
    cancelCallback: () -> Unit,
) {


    val optionsMenuState = remember {
        mutableStateOf(false)
    }
//    Surface(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(7.dp)
//            .clip(
//                RoundedCornerShape(15)
//            )
//            .height(intrinsicSize = IntrinsicSize.Min)
//            .background(colorResource(id = R.color.lavender))
//    ) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clip(
                RoundedCornerShape(10)
            )
            .background(colorResource(id = R.color.lavender))
    ) {
        Card(
            modifier = Modifier
                .background(colorResource(id = R.color.lavender))
                .padding(5.dp)
                .align(Alignment.TopCenter)
                .combinedClickable(
                    onClick = {
                    },
                    onLongClick = {
                        optionsMenuState.value = true
                    }
                )
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .background(colorResource(id = R.color.lavender)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.3F)
                        .padding(top = 15.dp)
                        .background(Color.Transparent),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxHeight(0.85F)
                            .clip(RoundedCornerShape(50)),
                        painter = painterResource(id = R.drawable.boy_face),
                        contentDescription = "Mentor image"
                    )
                    Spacer(modifier = Modifier.size(5.dp))
                    Column(
                        verticalArrangement = Arrangement.Top,
                    ) {
                        Spacer(modifier = Modifier.size(5.dp))
                        SubHeadingText(
                            text = appointment.studentName,
                            fontColor = Color.Black,
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.size(5.dp))
                        Text(
                            text = student.rollNo,
                            color = Color.Black,
                            modifier = Modifier,
                            fontSize = 17.sp
                        )
                        Spacer(modifier = Modifier.size(5.dp))
                        Text(text = student.phoneNumber, color = Color.Black, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.size(15.dp))
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.size(5.dp))
                        Row {

                            SubHeadingText(
                                text = "DOB : ",
                                fontColor = Color.Black,
                                modifier = Modifier
                            )

                            Text(
                                text = student.dateOfBirth,
                                color = Color.Black,
                                fontSize = 16.sp,
                                modifier = Modifier
                            )
                        }
                        Spacer(modifier = Modifier.size(15.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SubHeadingText(
                                text = "Time : ",
                                fontColor = Color.Black,
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            Text(text = appointment.timeSlot, color = Color.Black, fontSize = 16.sp)
                        }
                    }
                }

                Card(
                    modifier = Modifier.padding(5.dp).fillMaxWidth(0.8F),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Magenta
                    )
                ) {
                    SubHeadingText(
                        text = appointment.problemDescription!!,
                        fontColor = Color.White,
                        modifier = Modifier.padding(15.dp)
                    )
                }

                Card(
                    modifier = Modifier.padding(5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.ivory)
                    ),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(40),
                ) {
                    SubHeadingText(
                        text = appointment.status,
                        fontColor = Color.Black,
                        modifier = Modifier
                            .padding(20.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.purple_700)
                        ),
                        modifier = Modifier.padding(2.dp),
                        shape = RoundedCornerShape(25),
                        elevation = ButtonDefaults.buttonElevation(3.dp),
                        onClick = {
                            viewPastRecordCallback()
                        }
                    ) {
                        SubHeadingText(
                            text = "View Past Record",
                            fontColor = Color.White,
                            modifier = Modifier
                        )
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.light_gray)
                        ),
                        shape = RoundedCornerShape(25),
                        modifier = Modifier.padding(2.dp),
                        elevation = ButtonDefaults.buttonElevation(3.dp),
                        onClick = {
                            cancelCallback()
                        }
                    ) {
                        SubHeadingText(
                            text = "Cancel",
                            fontColor = Color.Black,
                            modifier = Modifier
                        )
                    }
                }
            }
        }

        DropdownMenu(
            expanded = optionsMenuState.value,
            onDismissRequest = {
                optionsMenuState.value = false
            }) {
            DropdownMenuItem(text = {
                SubHeadingText(text = "Reschedule", fontColor = Color.Black, modifier = Modifier)
            }, onClick = {
                optionsMenuState.value = false
            })

            DropdownMenuItem(text = {
                SubHeadingText(text = "Complete", fontColor = Color.Black, modifier = Modifier)
            }, onClick = {
                optionsMenuState.value = false
            })
        }
    }
}