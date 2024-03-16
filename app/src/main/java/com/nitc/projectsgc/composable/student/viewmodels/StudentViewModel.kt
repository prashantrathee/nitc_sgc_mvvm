package com.nitc.projectsgc.composable.student.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.nitc.projectsgc.composable.student.repo.BookingRepo
import com.nitc.projectsgc.composable.student.repo.StudentRepo
import com.nitc.projectsgc.models.Appointment
import com.nitc.projectsgc.models.Student
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StudentViewModel @Inject constructor(
    private val studentRepo: StudentRepo,
    private val bookingRepo: BookingRepo
):ViewModel() {

    private val _profile = MutableStateFlow<Either<String, Student>?>(null)
    val profile:StateFlow<Either<String,Student>?> = _profile.asStateFlow()


    private val _appointments = MutableStateFlow<Either<String,List<Appointment>>?>(null)
    val appointments: StateFlow<Either<String, List<Appointment>>?> = _appointments.asStateFlow()


    fun getProfile(rollNo:String){
        viewModelScope.launch {
            _profile.value = studentRepo.getStudent(rollNo)
        }
    }

    suspend fun updateProfile(student: Student, oldPassword: String): Boolean {
        val updateSuccess = withContext(Dispatchers.Main) {
            Log.d("updateProfile", "In adminViewmodel")
            if (studentRepo.updateProfile(student, oldPassword)) {
                Log.d("updateProfile", "Old password = $oldPassword")
                Log.d("updateProfile", "New password = ${student.password}")
                _profile.value = Either.Right(student)
                true
            } else false
        }
        return updateSuccess
    }

    fun deleteProfileValue(){
        _profile.value = null
    }


    fun deleteAppointmentsValue(){
        _appointments.value = null
    }

    fun getAppointments(rollNo:String){
        viewModelScope.launch {
            _appointments.value = bookingRepo.getAppointments(rollNo)
        }
    }


}