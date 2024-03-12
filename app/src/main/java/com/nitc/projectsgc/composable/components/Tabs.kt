package com.nitc.projectsgc.composable.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabLayout(
    tabs: List<String>,
    fontColor: Color,
    bg: Color,
    pageChanged: @Composable (Int) -> Unit
) {

    var selectedTabIndex by remember {
        mutableStateOf(0)
    }
    val pagerState = rememberPagerState(initialPage = selectedTabIndex) {
        tabs.size
    }
    LaunchedEffect(pagerState.currentPage,pagerState.isScrollInProgress) {
//        if(!pagerState.isScrollInProgress)
            selectedTabIndex = pagerState.currentPage
    }
    LaunchedEffect(selectedTabIndex) {
//        Log.d("tabIndex","Tab index changed to ${selectedTabIndex}")
        pagerState.scrollToPage(selectedTabIndex)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            contentColor = Color.Black,
            containerColor = bg,
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            divider = {

            },
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = {
//                        Log.d("tabIndex","Clicked is $index")
                        selectedTabIndex = index
                    },
                    modifier = Modifier
                ) {
                    SubHeadingText(
                        text = tab,
                        fontColor = fontColor,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) { pageIndex ->
            Box(modifier = Modifier.fillMaxSize()){
                pageChanged(pageIndex)
            }
        }
    }

}