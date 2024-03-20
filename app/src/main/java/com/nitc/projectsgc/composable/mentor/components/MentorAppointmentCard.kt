package com.nitc.projectsgc.composable.mentor.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.core.Either
import com.nitc.projectsgc.R
import com.nitc.projectsgc.composable.components.NormalText
import com.nitc.projectsgc.composable.components.SubHeadingText
import com.nitc.projectsgc.composable.mentor.MentorViewModel
import com.nitc.projectsgc.models.Appointment
import com.nitc.projectsgc.models.Student


//@Preview
//@Composable
//fun MentorAppointmentCardPreview() {
//    MentorAppointmentCard(
//        appointment = Appointment(
//            date = "24/11/2111",
//            mentorID = "sakshi_health_2",
//            mentorName = "Sakshi",
//            studentID = "prashant_m210704ca",
//            status = "Booked",
//            remarks = "",
//            problemDescription = "This is the problem hsdfhs dfhsdf h",
//            rescheduled = false,
//            timeSlot = "4-5",
//            mentorType = "Success"
//        ),
//        studentRoll = "m210704ca",
//        {},
//        {},
//        {},
//    )
//}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MentorAppointmentCard(
    appointment: Appointment,
    mentorViewModel: MentorViewModel,
    completeCallback: () -> Unit,
    viewPastRecordCallback: () -> Unit,
    cancelCallback: () -> Unit,
) {

    Log.d("getStudent", "in the mentor appointment card")

    LaunchedEffect(Unit) {
        mentorViewModel.getStudent(appointment.studentID)
    }
    val studentEitherState by mentorViewModel.student.collectAsState()

    val studentState = remember {
        mutableStateOf(Student())
    }
    when (val studentEither = studentEitherState) {
        is Either.Left -> {
            Log.d("getStudent", studentEither.value)
        }

        is Either.Right -> {
            Log.d("getStudent", "Student either value = ${studentEither.value}")
            studentState.value = studentEither.value
            ShowMentorAppointmentCard(
                appointment,
                studentState.value,
                completeCallback,
                viewPastRecordCallback,
                cancelCallback
            )
        }

        null -> {

        }
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

}


@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ShowMentorAppointmentCard(
    appointment: Appointment,
    student: Student,
    completeCallback: () -> Unit,
    viewPastRecordCallback: () -> Unit,
    cancelCallback: () -> Unit
) {
    val optionsMenuState = remember {
        mutableStateOf(false)
    }
//    var pointerOffset = Offset.Zero
//
//
//    val cardHeight = remember {
//        mutableStateOf(0)
//    }
//    val cardWidth = remember {
//        mutableStateOf(0)
//    }
    Card(
        modifier = Modifier
//            .onSizeChanged {size->
//                cardHeight.value = size.height
//                cardWidth.value = size.width
//            }
            .combinedClickable(
                onClick = {
                },
                onLongClick = {
                    optionsMenuState.value = true
                }
            )
//            .pointerInteropFilter {
//                pointerOffset = Offset(it.x, it.y)
////                Log.d("pointerOffset","x = ${it.x} and y = ${it.y}")
//                false
//            }
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.lavender)),
        shape = RoundedCornerShape(10)
    ) {

        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .background(colorResource(id = R.color.lavender))
        ) {
            Column(
                modifier = Modifier
                    .background(colorResource(id = R.color.lavender)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                        .background(Color.Transparent),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Image(
                        modifier = Modifier
                            .height(70.dp)
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
                            text = student.name,
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

                            NormalText(
                                text = "DOB : ",
                                fontColor = Color.Black,
                                modifier = Modifier
                            )

                            NormalText(
                                text = student.dateOfBirth,
                                fontColor = Color.Black,
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
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(0.8F),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.light_gray)
                    )
                ) {
                    SubHeadingText(
                        text = appointment.problemDescription,
                        fontColor = Color.Black,
                        modifier = Modifier.padding(15.dp)
                    )
                }

                Card(
                    modifier = Modifier.padding(5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.ivory)
                    ),
                    elevation = CardDefaults.cardElevation(1.dp),
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

            }
            DropdownMenu(
                modifier = Modifier.background(Color.White),
//                offset = DpOffset((pointerOffset.x % cardWidth.value).dp, (pointerOffset.y % cardHeight.value).dp),
                expanded = optionsMenuState.value,
                onDismissRequest = {
                    optionsMenuState.value = false
                }) {
                DropdownMenuItem(text = {
                    SubHeadingText(
                        text = "View Past Record",
                        fontColor = Color.Black,
                        modifier = Modifier
                    )
                }, onClick = {
                    viewPastRecordCallback()
                    optionsMenuState.value = false
                })
                if (!appointment.cancelled && !appointment.completed) {
                    DropdownMenuItem(text = {
                        SubHeadingText(
                            text = "Complete",
                            fontColor = Color.Black,
                            modifier = Modifier
                        )
                    }, onClick = {
                        completeCallback()
                        optionsMenuState.value = false
                    })
                    DropdownMenuItem(text = {
                        SubHeadingText(
                            text = "Cancel",
                            fontColor = Color.Black,
                            modifier = Modifier
                        )
                    }, onClick = {
                        cancelCallback()
                        optionsMenuState.value = false
                    })
                }
            }
        }

    }
}
