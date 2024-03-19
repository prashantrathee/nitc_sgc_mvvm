package com.nitc.projectsgc.composable.news.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import arrow.core.Either
import com.nitc.projectsgc.R
import com.nitc.projectsgc.composable.components.NewsCard
import com.nitc.projectsgc.composable.components.SubHeadingText
import com.nitc.projectsgc.composable.news.NewsViewModel
import com.nitc.projectsgc.composable.util.UserRole
import com.nitc.projectsgc.models.News


@Composable
fun NewsScreen(
    newsViewModel: NewsViewModel,
    userType: Int,
    username: String,
) {

    val newsListState = remember {
        mutableStateOf(emptyList<News>())
    }

    val newsContext = LocalContext.current


    LaunchedEffect(Unit) {
        newsViewModel.getNews()
    }
    val newsEitherState by newsViewModel.news.collectAsState()

    LaunchedEffect(key1 = newsEitherState) {
        when (val newsEither = newsEitherState) {
            is Either.Right -> {
                newsListState.value = newsEither.value
            }

            is Either.Left -> {
                showToast(newsEither.value, newsContext)
            }

        }
    }

    val deleteNewsState = remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(key1 = deleteNewsState.value) {
        if (deleteNewsState.value != null) {
            val deleted = newsViewModel.deleteNews(userType, deleteNewsState.value!!)
            if (deleted) {
                showToast("Deleted news", newsContext)
                newsViewModel.getNews()
            } else {
                showToast("Could not delete news", newsContext)
            }
            deleteNewsState.value = null
        }
    }
    val addNewsState = remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(newsListState.value.size) { index ->
                Box {

                    NewsCard(
                        date = newsListState.value[index].publishDate,
                        mentorName = if (newsListState.value[index].mentorID.contains('_')) newsListState.value[index].mentorID.substring(
                            0,
                            newsListState.value[index].mentorID.indexOfFirst { it == '_' }).replaceFirstChar(Char::titlecase) else newsListState.value[index].mentorID.replaceFirstChar(Char::titlecase),
                        body = newsListState.value[index].news,
                        clickCallback = {
                            if (userType == UserRole.Admin || (userType == UserRole.Mentor && username == newsListState.value[index].mentorID)) {
                                deleteNewsState.value = newsListState.value[index].newsID
                            }
                        }
                    )
                }

            }
        }
        if (userType != UserRole.Student) {

            FloatingActionButton(
                containerColor = colorResource(id = R.color.navy_blue),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp),
                onClick = {
                    addNewsState.value = true
                }) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(7.dp),
                    modifier = Modifier.padding(20.dp)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add News Button",
                        tint = Color.White
                    )
                    Text(
                        text = "Add \nNews",
                        color = Color.White,
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    if (addNewsState.value) {
        AddNewsScreen(newsViewModel = newsViewModel, mentorID = username, onDismiss = {
            addNewsState.value = false
        })
    }


}
