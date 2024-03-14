package com.nitc.projectsgc.composable.mentor.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import arrow.core.Either
import com.nitc.projectsgc.composable.admin.AdminViewModel
import com.nitc.projectsgc.composable.components.BasicButton
import com.nitc.projectsgc.composable.components.CardInputFieldWithOptions
import com.nitc.projectsgc.composable.components.CardInputFieldWithValue
import com.nitc.projectsgc.models.Mentor
import kotlinx.coroutines.launch


@Composable
fun ViewMentorScreen(
    username: String,
    adminViewModel: AdminViewModel
) {
    val mentorState = remember {
        mutableStateOf<Mentor?>(if (username != "no") null else Mentor())
    }
    if (username != "no") {
        adminViewModel.getMentor(username)
    } else {
        adminViewModel.deleteMentorValue()
        mentorState.value = Mentor()
    }
    val screenContext = LocalContext.current
    val updatingState = remember {
        mutableStateOf(false)
    }
    val mentorEither = adminViewModel.mentor.collectAsState().value
    var oldPassword = ""
    val coroutineScope = rememberCoroutineScope()
    when (mentorEither) {
        is Either.Left -> {
            Toast.makeText(
                LocalContext.current,
                "Error in getting mentor : ${mentorEither.value}",
                Toast.LENGTH_LONG
            ).show()
        }

        is Either.Right -> {
            Log.d("viewMentor", "no error message")
            mentorState.value = mentorEither.value
            oldPassword = mentorEither.value.password
            UpdateMentorScreen(mentorEither.value) { updatedMentor ->
                Log.d("updateMentor", "these are new values ; $updatedMentor")
                coroutineScope.launch {
                    Log.d("updateMentor", "Now updated : $updatedMentor")
                    val updateSuccess =
                        adminViewModel.updateMentor(updatedMentor, oldPassword)
                    if (updateSuccess) {
                        mentorState.value = updatedMentor
                        Toast.makeText(screenContext, "Updated Mentor", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(
                            screenContext,
                            "Error in updating mentor",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    updatingState.value = false
                }
            }
        }

        null -> {
            Log.d("viewMentor", "Mentor either is null")
            UpdateMentorScreen(Mentor()) { newMentor ->
                coroutineScope.launch {
                    val addedSuccessEither =
                        adminViewModel.addMentor(newMentor)
                    when (addedSuccessEither) {
                        is Either.Left -> {
                            Toast.makeText(
                                screenContext,
                                addedSuccessEither.value,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        is Either.Right -> {
                            Toast.makeText(screenContext, "Added Mentor", Toast.LENGTH_LONG)
                                .show()
                            mentorState.value = newMentor
                        }
                    }
                    updatingState.value = false
                }
            }
        }
    }
//
}

@Preview
@Composable
fun ViewMentorPreview() {

}