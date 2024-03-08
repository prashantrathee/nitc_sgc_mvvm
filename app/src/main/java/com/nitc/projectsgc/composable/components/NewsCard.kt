package com.nitc.projectsgc.composable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nitc.projectsgc.models.Mentor

@Preview
@Composable
fun loadNewsCard() {
//    NewsCard(
//        date = "today",
//        mentorName = "Alex",
//        body = "htihsiodhf ohsodfho shfoshfd ohsiofd psh "
//    )
    NewsCard("date","Mentor name","Body of the news")
}


@Composable
fun NewsCard(
    date: String,
    mentorName: String,
    body: String
) {
    BasicCard(
        cr = 5, elevation = 2, stroke = 1, padding = 3, CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.White,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            ) {
                SubHeadingText(
                    text = date, fontColor = Color.Black, modifier = Modifier.align(
                        Alignment.CenterStart
                    )
                )
                Row(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SubHeadingText(text = "By", fontColor = Color.Black, modifier = Modifier)
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = mentorName,
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                }
            }
            Spacer(modifier = Modifier.size(35.dp))

            Text(
                text = body,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.size(15.dp))

        }
    }
}

