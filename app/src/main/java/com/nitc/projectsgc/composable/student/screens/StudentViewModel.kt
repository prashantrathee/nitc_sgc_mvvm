package com.nitc.projectsgc.composable.student.screens

import androidx.lifecycle.ViewModel
import com.nitc.projectsgc.models.Student
import kotlinx.coroutines.flow.MutableStateFlow

class StudentViewModel:ViewModel() {

    private val _student = MutableStateFlow<Student?>(null)


}