package com.nitc.projectsgc.composable.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nitc.projectsgc.R
import com.nitc.projectsgc.models.Mentor


@Preview
@Composable
fun loadMentorCard() {
//    NewsCard(
//        date = "today",
//        mentorName = "Alex",
//        body = "htihsiodhf ohsodfho shfoshfd ohsiofd psh "
//    )
    MentorCard(mentor = Mentor(
        "Name",
        "Phone",
        "email sdfsf",
        "Career",
        "password",
        "username"
    ), deleteCallback = {},
    ) {

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MentorCard(
    mentor: Mentor,
    deleteCallback: () -> Unit,
    clickCallback: () -> Unit
) {
    val deleteMenuState = remember {
        mutableStateOf(false)
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp)
            .clip(
                RoundedCornerShape(15)
            )
            .height(intrinsicSize = IntrinsicSize.Min)
            .background(colorResource(id = R.color.lavender)),
        shadowElevation = 4.dp
    ) {

        Box(
            modifier = Modifier
                .background(colorResource(id = R.color.lavender)),
        ) {
            ClickableCard(
                padding = 10,
                clickCallback = {
                    clickCallback()
                },
                optionsCallback = {
                    deleteMenuState.value = true
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.lavender))
                ) {
                    Row(
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Image(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(50)),
                            painter = painterResource(id = R.drawable.boy_face),
                            contentDescription = "Mentor Photo"
                        )
                        Spacer(modifier = Modifier.size(20.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .background(Color.Transparent)
                        ) {
                            Spacer(modifier = Modifier.size(5.dp))
                            SubHeadingText(
                                text = mentor.name,
                                fontColor = Color.Black,
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            Text(text = mentor.email, color = Color.Black)
                            Spacer(modifier = Modifier.size(5.dp))
                            Text(
                                text = mentor.phone,
                                color = Color.Black,
                                modifier = Modifier
                            )
                        }
                    }
                    SubHeadingText(
                        text = mentor.type, fontColor = Color.Black, modifier = Modifier.align(
                            Alignment.TopEnd
                        )
                    )
                }
            }
            DropdownMenu(
                modifier = Modifier.align(Alignment.BottomEnd),
                expanded = deleteMenuState.value,
                onDismissRequest = {
                    deleteMenuState.value = false
                }) {
                DropdownMenuItem(text = {
                    SubHeadingText(
                        text = "Appointments",
                        fontColor = Color.Black,
                        modifier = Modifier
                    )
                }, onClick = {
                })
                DropdownMenuItem(text = {
                    SubHeadingText(text = "Delete", fontColor = Color.Black, modifier = Modifier)
                }, onClick = {
                    deleteMenuState.value = false
                    deleteCallback()
                })
            }
        }
    }
}
