package com.nitc.projectsgc.composable.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun loadLogin() {
    LoginCard {

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClickableCard(
    padding: Int,
    clickCallback: () -> Unit,
    optionsCallback: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(modifier = Modifier
        .background(Color.Transparent)
        .fillMaxSize()
        .height(IntrinsicSize.Min)
        .padding(padding.dp)
        .combinedClickable(
            onClick = {
                clickCallback()
            },
            onLongClick = {
                optionsCallback()
            }
        )
    ) {
        content()
    }
}

@Composable
fun LoginCard(userTypeCallback: (Int) -> Unit) {
    val userType = remember {
        mutableIntStateOf(0)
    }
    val unselectedTC = Color.Black
    val unselectedBG = Color.White
    val selectedTC = Color.White
    val selectedBG = Color.Black
    val userTypes = arrayOf(
        "Admin",
        "Student",
        "Mentor"
    )
    BasicCustomCard(
        50,
        2,
        1,
        CardDefaults.cardColors(),
        modifier = Modifier.height(50.dp).padding(horizontal = 25.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(0.dp)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            userTypes.forEachIndexed { index, type ->
                Card(
                    onClick = {
                        userType.intValue = index
                        userTypeCallback(userType.intValue)
                    },
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxHeight()
                        .weight(1F),
                    shape = RoundedCornerShape(50),
                    colors = CardDefaults.cardColors(
                        containerColor = if (userType.intValue == index) selectedBG else unselectedBG,
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = type,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            color = if (userType.intValue == index) selectedTC else unselectedTC,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BasicCustomCard(
    cr: Int,
    elevation: Int,
    stroke: Int,
    cardColors: CardColors,
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        border = BorderStroke(stroke.dp, Color.Black),
        shape = RoundedCornerShape(cr),
        elevation = CardDefaults.cardElevation(elevation.dp),
        colors = cardColors
    ) {
        content()
    }
}

@Composable
fun BasicCard(
    cr: Int,
    elevation: Int,
    stroke: Int,
    padding: Int,
    cardColors: CardColors,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(padding.dp),
        border = BorderStroke(stroke.dp, Color.Black),
        shape = RoundedCornerShape(cr.dp),
        elevation = CardDefaults.cardElevation(elevation.dp),
        colors = cardColors
    ) {
        content()
    }
}
