package com.nitc.projectsgc.composable.student.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import arrow.core.Either
import com.nitc.projectsgc.composable.admin.AdminViewModel
import com.nitc.projectsgc.composable.components.BasicButton
import com.nitc.projectsgc.composable.components.CardInputFieldWithValue
import com.nitc.projectsgc.composable.student.StudentViewModel
import com.nitc.projectsgc.models.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ViewStudentScreen(
    rollNo: String,
    adminViewModel: AdminViewModel
) {
    val studentState = remember {
        mutableStateOf<Student?>(null)
    }
    if (rollNo != "no") {
        adminViewModel.getStudent(rollNo)
    } else {
        adminViewModel.deleteStudentValue()
        studentState.value = Student()
    }
    val screenContext = LocalContext.current
    val updatingState = remember {
        mutableStateOf(false)
    }
    val studentEither = adminViewModel.student.collectAsState().value
    var oldPassword = ""
    val coroutineScope = rememberCoroutineScope()
    when (studentEither) {
        is Either.Left -> {
            Toast.makeText(
                LocalContext.current,
                "Error in getting student : ${studentEither.value}",
                Toast.LENGTH_LONG
            ).show()
        }

        is Either.Right -> {
            Log.d("viewStudent", "no error message")
            studentState.value = studentEither.value
            oldPassword = studentEither.value.password
            UpdateStudentScreen(studentEither.value) { updatedStudent ->
                Log.d("updateStudent", "these are new values ; $updatedStudent")
                coroutineScope.launch {
                    Log.d("updateStudent", "Now updated : $updatedStudent")
                    val updateSuccess =
                        adminViewModel.updateStudent(updatedStudent, oldPassword)
                    if (updateSuccess) {
                        studentState.value = updatedStudent
                        Toast.makeText(screenContext, "Updated Student", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(
                            screenContext,
                            "Error in updating student",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    updatingState.value = false
                }
            }
        }

        null -> {
            Log.d("viewStudent", "Student either is null")
            UpdateStudentScreen(Student()) { newStudent ->
                coroutineScope.launch {
                    val addedSuccessEither =
                        adminViewModel.addStudent(newStudent)
                    when (addedSuccessEither) {
                        is Either.Left -> {
                            Toast.makeText(
                                screenContext,
                                addedSuccessEither.value,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        is Either.Right -> {
                            Toast.makeText(screenContext, "Added Student", Toast.LENGTH_LONG)
                                .show()
                            studentState.value = newStudent
                        }
                    }
                    updatingState.value = false
                }
            }
        }
    }
//
}

@Composable
fun UpdateStudentScreen(studentFound: Student, onUpdate: (student: Student) -> Unit) {

    val studentState = remember {
        mutableStateOf(studentFound)
    }
//        val updateSuccess by adminViewModel.updateSuccess.collectAsState()
    val oldPassword = studentState.value.password
//        Log.d("updateStudent","Old password = $oldPassword")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardInputFieldWithValue(
            hint = "Name",
            text = studentState.value!!.name,
            isPassword = false,
            modifier = Modifier.fillMaxWidth(0.75F)
        ) { newName ->
            studentState.value = studentState.value.copy(name = newName)
        }
        Spacer(modifier = Modifier.size(20.dp))

        CardInputFieldWithValue(
            hint = "Roll Number",
            text = studentState.value!!.rollNo,
            isPassword = false,
            modifier = Modifier.fillMaxWidth(0.75F)
        ) { newRoll ->
            studentState.value = studentState.value!!.copy(rollNo = newRoll)
        }
        Spacer(modifier = Modifier.size(20.dp))

        CardInputFieldWithValue(
            hint = "Gender",
            text = studentState.value!!.gender,
            isPassword = false,
            modifier = Modifier.fillMaxWidth(0.75F)
        ) { newGender ->
            studentState.value = studentState.value!!.copy(gender = newGender)

        }
        Spacer(modifier = Modifier.size(20.dp))

        CardInputFieldWithValue(
            hint = "Phone Number",
            text = studentState.value!!.phoneNumber,
            isPassword = false,
            modifier = Modifier.fillMaxWidth(0.75F)
        ) { newPhone ->
            studentState.value = studentState.value!!.copy(phoneNumber = newPhone)
        }
        Spacer(modifier = Modifier.size(20.dp))

        CardInputFieldWithValue(
            hint = "Password",
            text = studentState.value!!.password,
            isPassword = false,
            modifier = Modifier.fillMaxWidth(0.75F)
        ) { newPassword ->
            studentState.value = studentState.value!!.copy(password = newPassword)
            Log.d("updateStudent", "student value now is ${studentState.value.toString()}")
        }
        Spacer(modifier = Modifier.size(70.dp))

        BasicButton(
            text = if (studentFound.name.isEmpty()) "Add" else "Update",
            colors = ButtonDefaults.buttonColors(),
            modifier = Modifier.padding(20.dp),
            tc = Color.White
        ) {
            Log.d("updateStudent", "Now updated : ${studentState.value}")
//                updatingState.value = true
            onUpdate(studentState.value)

        }

    }
}
//
//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun ViewStudentPreview() {
//    ViewStudentScreen(rollNo = "m210704ca")
//}