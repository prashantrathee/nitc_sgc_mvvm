package com.nitc.projectsgc.composable.admin

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nitc.projectsgc.composable.admin.viewmodels.AdminViewModel
import com.nitc.projectsgc.composable.admin.viewmodels.MentorListViewModel
import com.nitc.projectsgc.composable.admin.viewmodels.StudentListViewModel
import com.nitc.projectsgc.composable.components.MentorCard
import com.nitc.projectsgc.composable.components.StudentCard
import com.nitc.projectsgc.composable.components.TabLayout


@Composable
fun AdminDashboardScreen(
    adminViewModel: AdminViewModel,
    navController: NavController,
    mentorListViewModel: MentorListViewModel,
    studentListViewModel: StudentListViewModel,
    viewStudentCallback: (rollNo: String) -> Unit,
    viewMentorCallback: (username: String) -> Unit,
    addStudentCallback: () -> Unit,
    addMentorCallback: () -> Unit
) {
    Log.d("adminDashboard","Inside the admin dashboard screen")
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val selectedTabIndex = remember {
            mutableIntStateOf(0)
        }
        TabLayout(
            tabs = listOf("Students", "Mentors"),
            fontColor = Color.Black,
            bg = Color.White,
        ) { pageIndex ->
            when (pageIndex) {
                0 -> {
                    GetStudents(
                        studentListViewModel = studentListViewModel,
                        viewStudentCallback = {
                            viewStudentCallback(it)
                        },
                        backCallback = {
                            navController.popBackStack()
                        },
                        addStudentCallback = {
                            addStudentCallback()
                        }
                    )
                }

                1 -> {
                    GetMentors(mentorListViewModel,
                        viewMentorCallback = {
                            Log.d("viewMentor","Getting username : $it")
                            viewMentorCallback(it)
                        },
                        backCallback = {
                            navController.popBackStack()
                        },
                        addMentorCallback = {
                            addMentorCallback()
                        }
                    )
                }
            }
        }

    }
}

@Composable
fun GetStudents(
    studentListViewModel: StudentListViewModel,
    viewStudentCallback: (rollNo: String) -> Unit,
    backCallback: () -> Unit,
    addStudentCallback: () -> Unit
) {
    val myContext = LocalContext.current
    studentListViewModel.getStudents(myContext)
    val students = studentListViewModel.studentList.collectAsState()
    BackHandler(enabled = true) {
        backCallback()
    }
    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                count = students.value.size,
                itemContent = { index: Int ->
                    val student = students.value[index]
                    StudentCard(
                        student = student,
                        deleteCallback = {
                            studentListViewModel.deleteStudent(myContext, student.rollNo)
                        },
                        clickCallback = {
                            viewStudentCallback(student.rollNo)
                        },
                    )
                }
            )
        }

        FloatingActionButton(
            onClick = {
                addStudentCallback()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
        ) {
            Icon(Icons.Filled.Add, "Add Student")
        }
    }
}

@Composable
fun GetMentors(
    mentorListViewModel: MentorListViewModel,
    viewMentorCallback: (username: String) -> Unit,
    backCallback: () -> Unit,
    addMentorCallback: () -> Unit
) {
    BackHandler(enabled = true) {
        backCallback()
    }
    val myContext = LocalContext.current
    mentorListViewModel.getMentors(myContext)
    val mentors = mentorListViewModel.mentorList.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
        ) {
            items(
                count = mentors.value.size,
                itemContent = { index: Int ->
                    val mentor = mentors.value[index]
                    MentorCard(mentor = mentor, deleteCallback = {
                        mentorListViewModel.deleteMentor(myContext, mentor.userName)
                    },
                        clickCallback = {
                            viewMentorCallback(mentor.userName)
                        },
                    )
                }
            )
        }
        FloatingActionButton(
            onClick = {
                addMentorCallback()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
        ) {
            Icon(Icons.Filled.Add, "Add Mentor")
        }
    }
}
