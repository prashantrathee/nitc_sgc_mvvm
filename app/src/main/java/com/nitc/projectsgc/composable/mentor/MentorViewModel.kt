package com.nitc.projectsgc.composable.mentor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.nitc.projectsgc.composable.mentor.repo.MentorRepo
import com.nitc.projectsgc.models.Appointment
import com.nitc.projectsgc.models.Mentor
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
class MentorViewModel @Inject constructor(
    private val mentorRepo: MentorRepo,
) :ViewModel() {


    private val _appointments = MutableStateFlow<Either<String,List<Appointment>>?>(Either.Right(
        emptyList()
    ))
    val appointments: StateFlow<Either<String, List<Appointment>>?> = _appointments.asStateFlow()


    private val _mentor = MutableStateFlow<Either<String, Mentor>?>(null)
    val mentor: StateFlow<Either<String, Mentor>?> = _mentor.asStateFlow()


    private val _student = MutableStateFlow<Either<String, Student>?>(null)
    val student: StateFlow<Either<String, Student>?> = _student.asStateFlow()

    fun deleteMentorValue(){
        _mentor.value = null
    }

    fun deleteStudentValue(){
        _student.value = null
    }


    fun getStudent(rollNo:String){
        viewModelScope.launch {
            _student.value = mentorRepo.getStudent(rollNo)
        }
    }
    fun getProfile(username: String) {
        viewModelScope.launch {
            _mentor.value = mentorRepo.getProfile(username)
        }
    }

    suspend fun cancelAppointment(appointment: Appointment):Boolean{
        val cancelled = withContext(Dispatchers.Main){
            mentorRepo.cancelAppointment(appointment)
        }
        return cancelled
    }
    suspend fun completeAppointment(appointment: Appointment):Boolean{
        val completed = withContext(Dispatchers.Main){
            mentorRepo.giveRemarks(appointment)
        }
        return completed
    }
    suspend fun updateProfile(mentor: Mentor, oldPassword: String): Boolean {
        val updateSuccess = withContext(Dispatchers.Main) {
            Log.d("updateMentor", "In adminViewmodel")
            if (mentorRepo.updateProfile(mentor, oldPassword)) {
                Log.d("updateMentor", "Old password = $oldPassword")
                Log.d("updateMentor", "New password = ${mentor.password}")
                _mentor.value = Either.Right(mentor)
                true
            } else false
        }
        return updateSuccess
    }

    fun deleteMentorAppointments(){
        _appointments.value = null
    }
    fun getMentorAppointments(username:String,today: String){
        viewModelScope.launch {
            deleteMentorAppointments()
            _appointments.value = mentorRepo.getTodayAppointments(username, today)
        }
    }


    private val _pastRecord = MutableStateFlow<Either<String,List<Appointment>>?>(Either.Right(
        emptyList()
    ))
    val pastRecord: StateFlow<Either<String, List<Appointment>>?> = _pastRecord.asStateFlow()

    fun deleteStudentPastRecordValue(){
        _pastRecord.value = null
    }
    fun getStudentPastRecord(username:String,rollNo: String){
        viewModelScope.launch {
            _pastRecord.value = mentorRepo.getStudentRecord(username, rollNo)
        }
    }

}