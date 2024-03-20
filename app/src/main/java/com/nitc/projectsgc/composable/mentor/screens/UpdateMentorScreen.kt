package com.nitc.projectsgc.composable.mentor.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import arrow.core.Either
import com.nitc.projectsgc.composable.components.BasicButton
import com.nitc.projectsgc.composable.components.CardInputFieldWithOptions
import com.nitc.projectsgc.composable.components.CardInputFieldWithValue
import com.nitc.projectsgc.composable.components.SimpleToast
import com.nitc.projectsgc.composable.util.PathUtils
import com.nitc.projectsgc.models.Mentor


@Composable
fun UpdateMentorScreen(mentorFound: Mentor, onUpdate: (mentor: Mentor) -> Unit) {

    val mentorState = remember {
        mutableStateOf(mentorFound)
    }
    val screenContext = LocalContext.current
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            CardInputFieldWithValue(
                hint = "Name",
                text = mentorState.value!!.name,
                isPassword = false,
                modifier = Modifier.fillMaxWidth(0.75F)
            ) { newName ->
                mentorState.value = mentorState.value.copy(name = newName)
            }
        }

        item {
            CardInputFieldWithValue(
                hint = "Email",
                text = mentorState.value!!.email,
                isPassword = false,
                modifier = Modifier.fillMaxWidth(0.75F)
            ) { newEmail ->
                mentorState.value = mentorState.value!!.copy(email = newEmail)
            }
        }
        item {
            CardInputFieldWithValue(
                hint = "Username",
                text = mentorState.value!!.userName,
                isPassword = false,
                modifier = Modifier.fillMaxWidth(0.75F)
            ) { newUsername ->
                mentorState.value = mentorState.value!!.copy(userName = newUsername)

            }
        }

        item {
            CardInputFieldWithOptions(
                hint = "Phone Number",
                text = mentorState.value!!.phone,
                isPassword = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                modifier = Modifier.fillMaxWidth(0.75F)
            ) { newPhone ->
                mentorState.value = mentorState.value!!.copy(phone = newPhone)
            }
        }
        item {
            CardInputFieldWithValue(
                hint = "Password",
                text = mentorState.value!!.password,
                isPassword = false,
                modifier = Modifier.fillMaxWidth(0.75F)
            ) { newPassword ->
                mentorState.value = mentorState.value!!.copy(password = newPassword)
                Log.d("updateMentor", "mentor value now is ${mentorState.value.toString()}")
            }
        }

        item {
            Spacer(modifier = Modifier.size(20.dp))
            BasicButton(
                text = if (mentorFound.name.isEmpty()) "Add" else "Update",
                colors = ButtonDefaults.buttonColors(),
                modifier = Modifier.padding(20.dp),
                tc = Color.White
            ) {
                Log.d("updateMentor", "Now updated : ${mentorState.value}")
//                updatingState.value = true
                val username = mentorState.value.userName
                when (val mentorTypeEither = PathUtils.getMentorType(username)) {
                    is Either.Left -> {
                        Toast.makeText(
                            screenContext,
                            mentorTypeEither.value.message!!,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is Either.Right -> {
                        mentorState.value = mentorState.value.copy(
                            type = mentorTypeEither.value
                        )
                        onUpdate(mentorState.value)
                    }
                }
//            if(mentorFound.name.isEmpty()){
//            }
            }
        }

    }


}
