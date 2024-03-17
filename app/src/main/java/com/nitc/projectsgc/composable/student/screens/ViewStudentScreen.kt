package com.nitc.projectsgc.composable.student.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import arrow.core.Either
import com.nitc.projectsgc.R
import com.nitc.projectsgc.composable.admin.viewmodels.AdminViewModel
import com.nitc.projectsgc.composable.components.BasicButton
import com.nitc.projectsgc.composable.components.BasicSubHeadingButton
import com.nitc.projectsgc.composable.components.CardInputFieldWithValue
import com.nitc.projectsgc.composable.components.DateDialog
import com.nitc.projectsgc.composable.components.SimpleToast
import com.nitc.projectsgc.composable.components.SubHeadingText
import com.nitc.projectsgc.composable.util.PathUtils
import com.nitc.projectsgc.models.Student
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
            UpdateStudentScreen(
                Student(
                    dateOfBirth = SimpleDateFormat(
                        "dd-MM-yyyy",
                        Locale.getDefault()
                    ).format(Date())
                )
            ) { newStudent ->
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
    val dobDialogState = remember {
        mutableStateOf(false)
    }
    val screenContext = LocalContext.current
//        val updateSuccess by adminViewModel.updateSuccess.collectAsState()
    val oldPassword = studentState.value.password
//        Log.d("updateStudent","Old password = $oldPassword")
    if (dobDialogState.value) {
        DateDialog(heading = "Date of Birth", isVisible = dobDialogState.value) { dateChosen ->
            studentState.value = studentState.value.copy(dateOfBirth = dateChosen)
            dobDialogState.value = false
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp),
        reverseLayout = false,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(1) { index ->

            CardInputFieldWithValue(
                hint = "Name",
                text = studentState.value!!.name,
                isPassword = false,
                modifier = Modifier.fillMaxWidth(0.75F)
            ) { newName ->
                studentState.value = studentState.value.copy(name = newName)
            }
            Spacer(modifier = Modifier.size(20.dp))

            BasicSubHeadingButton(
                text = studentState.value.dateOfBirth, colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.light_gray)
                ), tc = Color.Black, modifier = Modifier
            ) {
                dobDialogState.value = true
            }

            Spacer(modifier = Modifier.size(20.dp))

            CardInputFieldWithValue(
                hint = "Email",
                text = studentState.value!!.emailId,
                isPassword = false,
                modifier = Modifier.fillMaxWidth(0.75F)
            ) { emailInput ->
                studentState.value = studentState.value!!.copy(emailId = emailInput)
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
            Spacer(modifier = Modifier.size(40.dp))

            BasicButton(
                text = if (studentFound.name.isEmpty()) "Add" else "Update",
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.navy_blue)),
                modifier = Modifier.padding(20.dp),
                tc = Color.White
            ) {
                Log.d("updateStudent", "Now updated : ${studentState.value}")
//                updatingState.value = true
                when (val rollNoEither =
                    PathUtils.getUsernameFromEmail(1, studentState.value.emailId)) {
                    is Either.Right -> {
                        studentState.value = studentState.value.copy(rollNo = rollNoEither.value)
                        onUpdate(studentState.value)
                    }

                    is Either.Left -> {
                        Toast.makeText(screenContext, "Enter valid Email", Toast.LENGTH_LONG).show()
                    }
                }


            }
        }

    }
}
//
//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun ViewStudentPreview() {
//    ViewStudentScreen(rollNo = "m210704ca")
//}