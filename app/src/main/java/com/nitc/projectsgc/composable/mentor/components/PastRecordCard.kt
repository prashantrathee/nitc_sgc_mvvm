package com.nitc.projectsgc.composable.mentor.components

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nitc.projectsgc.R
import com.nitc.projectsgc.composable.components.NormalText
import com.nitc.projectsgc.models.Appointment

@Composable
fun PastRecordCard(
    appointment: Appointment
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clip(
                RoundedCornerShape(15)
            )
            .background(colorResource(id = R.color.lavender))
    ) {
        Card(
            modifier = Modifier
                .background(colorResource(id = R.color.lavender))
                .padding(5.dp)
                .align(Alignment.TopCenter)
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
                        .height(130.dp)
                        .padding(top = 5.dp)
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
                    Spacer(modifier = Modifier.size(15.dp))
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
//                        Spacer(modifier = Modifier.size(15.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            NormalText(
                                text = "Time : ",
                                fontColor = Color.Black,
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            NormalText(
                                text = appointment.timeSlot,
                                fontColor = Color.Black,
                                modifier = Modifier
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(15.dp))
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
//                        Spacer(modifier = Modifier.size(15.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            NormalText(
                                text = "Type : ",
                                fontColor = Color.Black,
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            Text(
                                text = appointment.mentorType,
                                color = Color.Black,
                                modifier = Modifier
                            )
                        }
                    }
                }
                Card(
                    modifier = Modifier.padding(5.dp),
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
                            .padding(20.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}