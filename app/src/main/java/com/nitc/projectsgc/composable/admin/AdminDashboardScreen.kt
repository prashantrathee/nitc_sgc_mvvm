package com.nitc.projectsgc.composable.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.nitc.projectsgc.composable.admin.viewmodels.MentorListViewModel
import com.nitc.projectsgc.composable.admin.viewmodels.StudentListViewModel
import com.nitc.projectsgc.composable.components.MentorCard
import com.nitc.projectsgc.composable.components.StudentCard
import com.nitc.projectsgc.composable.components.TabLayout
import com.nitc.projectsgc.composable.login.LoginViewModel


@Composable
fun AdminDashboardScreen(
    adminViewModel: AdminViewModel,
    mentorListViewModel: MentorListViewModel,
    studentListViewModel: StudentListViewModel,
    viewStudentCallback: (rollNo: String) -> Unit,
    studentAppointmentsCallback: (rollNo: String) -> Unit,
    viewMentorCallback: (username: String) -> Unit,
    mentorAppointmentsCallback: (username: String) -> Unit,
) {
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
            selectedTabIndex = selectedTabIndex
        )
        when (selectedTabIndex.intValue) {
            0 -> {
                getStudents(
                    studentListViewModel = studentListViewModel,
                    viewStudentCallback = {
                                          viewStudentCallback(it)
                    },
                    appointmentsCallback = {
                        studentAppointmentsCallback(it)
                    }
                )
            }

            1 -> {
                getMentors(mentorListViewModel,
                    viewMentorCallback = {
                        viewMentorCallback(it)
                    },
                    appointmentsCallback = {
                        mentorAppointmentsCallback(it)
                    }
                )
            }
        }
    }
}

@Composable
fun getStudents(
    studentListViewModel: StudentListViewModel,
    viewStudentCallback: (rollNo: String) -> Unit,
    appointmentsCallback: (rollNo: String) -> Unit
) {
    val myContext = LocalContext.current
    studentListViewModel.getStudents(myContext)
    val students = studentListViewModel.studentList.collectAsState()
    LazyColumn(

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
                    appointmentsCallback = {
                        appointmentsCallback(student.rollNo)
                    }
                )
            }
        )
    }
}

@Composable
fun getMentors(
    mentorListViewModel: MentorListViewModel,
    viewMentorCallback: (username: String) -> Unit,
    appointmentsCallback: (username: String) -> Unit
) {
    val myContext = LocalContext.current
    mentorListViewModel.getMentors(myContext)
    val mentors = mentorListViewModel.mentorList.collectAsState()
    LazyColumn(

    ) {
        items(
            count = mentors.value.size,
            itemContent = { index: Int ->
                val mentor = mentors.value[index]
                MentorCard(mentor = mentor, deleteCallback = {
                    mentorListViewModel.deleteMentor(myContext, mentor.userName)
                }) {

                }
            }
        )
    }
}