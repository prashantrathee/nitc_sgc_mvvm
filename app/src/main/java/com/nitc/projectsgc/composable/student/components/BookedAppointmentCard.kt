package com.nitc.projectsgc.composable.student.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nitc.projectsgc.R
import com.nitc.projectsgc.composable.components.CardFieldWithValue
import com.nitc.projectsgc.composable.components.CardInputFieldWithValue
import com.nitc.projectsgc.composable.components.ClickableCard
import com.nitc.projectsgc.composable.components.HeadingText
import com.nitc.projectsgc.composable.components.NormalText
import com.nitc.projectsgc.composable.components.SubHeadingText
import com.nitc.projectsgc.models.Appointment


@Preview
@Composable
fun BookedAppointmentCardPreview() {
    BookedAppointmentCard(
        appointment = Appointment(
            date = "24/11/2111",
            mentorID = "sakshi_health_2",
            mentorName = "Sakshi",
            studentID = "prashant_m210704ca",
            studentName = "Prashant",
            status = "Booked",
            remarks = "ahsdfl haskldfh lkashdfl ahskldfhasl hdflkahsd lfkjhasaskhfd lksahdflk ahsl",
            rescheduled = false,
            timeSlot = "4-5",
            mentorType = "Success"
        ),
        {},
        {}
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookedAppointmentCard(
    appointment: Appointment,
    rescheduleCallback: () -> Unit,
    cancelCallback: () -> Unit
) {

    val density = LocalDensity.current
    val normalPadding = with(density) { dimensionResource(id = R.dimen.normal_padding) }
    val smallPadding = with(density) { dimensionResource(id = R.dimen.small_padding) }
    val spacerTopNormal = with(density) { dimensionResource(id = R.dimen.spacer_top_normal) }
    val spacerTopSmall = with(density) { dimensionResource(id = R.dimen.spacer_top_small) }
    val imageSize = with(density) { dimensionResource(id = R.dimen.card_image_size) }
    val appointmentCardHeight = with(density) { dimensionResource(id = R.dimen.appointment_card_height) }

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
    Card(
        modifier = Modifier
            .padding(smallPadding)
            .combinedClickable(
                onClick = {
                },
                onLongClick = {
                    optionsMenuState.value = true
                }
            )
            .fillMaxWidth(),
        shape = RoundedCornerShape(10),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.lavender)
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(smallPadding)
                .clip(
                    RoundedCornerShape(15)
                )
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
                        .height(appointmentCardHeight)
                        .padding(top = smallPadding)
                        .background(Color.Transparent),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxHeight(0.65F)
                            .clip(RoundedCornerShape(50)),
                        painter = painterResource(id = R.drawable.boy_face),
                        contentDescription = "Mentor image"
                    )
                    Spacer(modifier = Modifier.size(normalPadding))
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                    ) {
//                        Spacer(modifier = Modifier.size(10.dp))
                        NormalText(
                            text = appointment.mentorName,
                            fontColor = Color.Black,
                            modifier = Modifier
                        )
//                        Spacer(modifier = Modifier.size(normalPadding))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            NormalText(
                                text = "Time : ",
                                fontColor = Color.Black,
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.size(smallPadding))
                            NormalText(
                                text = appointment.timeSlot,
                                fontColor = Color.Black,
                                modifier = Modifier
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(normalPadding))
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
//                        Spacer(modifier = Modifier.size(10.dp))
                        Text(
                            text = appointment.date,
                            color = Color.Black,
                            modifier = Modifier
                        )
//                        Spacer(modifier = Modifier.size(normalPadding))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            NormalText(
                                text = "Type : ",
                                fontColor = Color.Black,
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.size(smallPadding))
                            Text(
                                text = appointment.mentorType,
                                color = Color.Black,
                                modifier = Modifier
                            )
                        }
                    }
                }
                Card(
                    modifier = Modifier.padding(smallPadding),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.ivory)
                    ),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(40),
                ) {
                    NormalText(
                        text = appointment.status,
                        fontColor = Color.Black,
                        modifier = Modifier
                            .padding(normalPadding)
                            .align(Alignment.CenterHorizontally)
                    )
                }
                if (appointment.completed) {
                    CardFieldWithValue(
                        hint = "Remarks",
                        text = appointment.remarks,
                        modifier = Modifier
                            .padding(horizontal = smallPadding)
                            .fillMaxWidth(0.85F)
                    )
                }
            }

            if (!appointment.completed && !appointment.cancelled) {

                DropdownMenu(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .background(Color.White),
                    expanded = optionsMenuState.value,
                    onDismissRequest = {
                        optionsMenuState.value = false
                    }) {
                    DropdownMenuItem(text = {
                        NormalText(
                            text = "Reschedule",
                            fontColor = Color.Black,
                            modifier = Modifier
                        )
                    }, onClick = {
                        rescheduleCallback()
                        optionsMenuState.value = false
                    }
                    )
                    DropdownMenuItem(text = {
                        NormalText(text = "Cancel", fontColor = Color.Black, modifier = Modifier)
                    }, onClick = {
                        cancelCallback()
                        optionsMenuState.value = false
                    }
                    )
                }
            }
        }

    }
//            .height(intrinsicSize = IntrinsicSize.Min)
//            .background(colorResource(id = R.color.lavender))
//    ) {


}
