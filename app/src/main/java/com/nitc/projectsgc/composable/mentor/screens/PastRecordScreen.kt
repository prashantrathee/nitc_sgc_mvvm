package com.nitc.projectsgc.composable.mentor.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import arrow.core.Either
import com.nitc.projectsgc.composable.mentor.MentorViewModel
import com.nitc.projectsgc.composable.mentor.components.PastRecordCard
import com.nitc.projectsgc.composable.news.screens.showToast
import com.nitc.projectsgc.composable.student.components.BookedAppointmentCard
import com.nitc.projectsgc.models.Appointment

@Composable
fun PastRecordScreen(
    mentorViewModel: MentorViewModel,
    rollNo: String,
    username: String
) {

    val myContext = LocalContext.current

    val isLoading = remember {
        mutableStateOf(true)
    }
    LaunchedEffect(Unit) {
        mentorViewModel.getStudentPastRecord(username, rollNo)
    }

    val pastRecordEitherState by mentorViewModel.pastRecord.collectAsState()

    val pastRecordState = remember {
        mutableStateOf(listOf<Appointment>())
    }

    when (val pastRecordEither = pastRecordEitherState) {
        is Either.Left -> {
            showToast(pastRecordEither.value, myContext)
        }

        is Either.Right -> {
            pastRecordState.value = pastRecordEither.value
        }

        null -> {

        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            count = pastRecordState.value.size,
            itemContent = { index ->
                PastRecordCard(
                    appointment = pastRecordState.value[index]
                )
            })
    }
}