package com.nitc.projectsgc.composable.components

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
import androidx.compose.material3.CardElevation
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
import com.nitc.projectsgc.models.Mentor
import com.nitc.projectsgc.models.Student


@Preview
@Composable
fun loadStudentCard() {
//    NewsCard(
//        date = "today",
//        mentorName = "Alex",
//        body = "htihsiodhf ohsodfho shfoshfd ohsiofd psh "
//    )
    StudentCard(student = Student(
        "Phone",
        "Name",
        "Career",
        "24/11/2311",
        "email sdfsf",
        "Male",
        "password",
        "248545345234"
    ), deleteCallback = {}, clickCallback = {}) {

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudentCard(
    student: Student,
    deleteCallback: () -> Unit,
    clickCallback: () -> Unit,
    appointmentsCallback: () -> Unit
) {
    val deleteMenuState = remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .height(intrinsicSize = IntrinsicSize.Min)
            .clip(
                RoundedCornerShape(15)
            )
            .background(colorResource(id = R.color.lavender))
    ) {
        Card(
            modifier = Modifier
                .background(colorResource(id = R.color.lavender))
                .padding(10.dp)
                .combinedClickable(
                    onClick = {
                        clickCallback()
                    },
                    onLongClick = {
                        deleteMenuState.value = true

                    }
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.lavender)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.background(Color.Transparent)
                ) {
                    Image(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(50)),
                        painter = if (student.gender == "Male") painterResource(id = R.drawable.boy_face) else painterResource(
                            id = R.drawable.girl_face
                        ),
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
                            text = student.name,
                            fontColor = Color.Black,
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.size(5.dp))
                        Text(text = student.rollNo, color = Color.Black)
                        Spacer(modifier = Modifier.size(5.dp))
                        Text(
                            text = student.phoneNumber,
                            color = Color.Black,
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.size(15.dp))
                        Button(
                            onClick = { appointmentsCallback() },
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.purple_700)
                            ),
                            elevation = ButtonDefaults.buttonElevation(5.dp)
                        ) {
                            Text(text = "View Appointments", color = Color.White)
                        }
                    }
                }
                Text(
                    text = student.dateOfBirth,
                    color = Color.Black,
                    fontSize = 15.sp,
                    modifier = Modifier.align(Alignment.Top)
                )
            }
        }
        DropdownMenu(expanded = deleteMenuState.value, onDismissRequest = {
            deleteMenuState.value = false
        }) {
            DropdownMenuItem(text = {
                SubHeadingText(text = "Delete", fontColor = Color.Black, modifier = Modifier)
            }, onClick = {
                deleteMenuState.value = false
                deleteCallback()
            })
        }
    }

}