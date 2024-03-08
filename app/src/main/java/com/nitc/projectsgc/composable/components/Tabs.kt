package com.nitc.projectsgc.composable.components

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun TabLayout(
    tabs: List<String>,
    fontColor: Color,
    bg: Color,
    selectedTabIndex: MutableState<Int>
) {
    TabRow(
        selectedTabIndex = selectedTabIndex.value,
        containerColor = bg
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = index == selectedTabIndex.value,
                onClick = {
                    selectedTabIndex.value = index
                }
            ) {
                SubHeadingText(text = tab, fontColor = fontColor,modifier = Modifier)
            }
        }
    }
}