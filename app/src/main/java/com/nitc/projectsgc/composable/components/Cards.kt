package com.nitc.projectsgc.composable.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
    clickCallback:()->Unit,
    optionsCallback:()->Unit,
    content: @Composable () -> Unit
) {
    Card(modifier = Modifier
        .background(Color.Transparent)
        .fillMaxSize()
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
    val unselectedTC = remember {
        mutableStateOf(Color.Black)
    }
    val unselectedBG = remember {
        mutableStateOf(Color.White)
    }
    val selectedTC = remember {
        mutableStateOf(Color.White)
    }
    val selectedBG = remember {
        mutableStateOf(Color.Black)
    }
    val userTypes = arrayOf(
        "Admin",
        "Student",
        "Warden"
    )
    BasicCard(25, 2, 1, 0, CardDefaults.cardColors()) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(0.dp)
        ) {
            userTypes.forEachIndexed { index, type ->
                Button(
                    onClick = {
                        userType.intValue = index
                        userTypeCallback(userType.intValue)
                    },
                    contentPadding = PaddingValues(
                        horizontal = 5.dp,
                        vertical = 0.dp
                    ),
                    modifier = Modifier.padding(0.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (userType.intValue == index) selectedBG.value else unselectedBG.value,
                    )
                ) {
                    SubHeadingText(
                        text = type,
                        fontColor = if (userType.intValue == index) selectedTC.value else unselectedTC.value,
                        modifier = Modifier
                    )
                }
            }
        }
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
