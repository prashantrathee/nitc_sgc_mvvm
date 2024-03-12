package com.nitc.projectsgc.composable.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.nitc.projectsgc.composable.admin.repo.StudentsRepo
import com.nitc.projectsgc.composable.student.repo.StudentRepo
import com.nitc.projectsgc.models.Student
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentViewModel @Inject constructor(private val studentRepo: StudentRepo):ViewModel() {


}