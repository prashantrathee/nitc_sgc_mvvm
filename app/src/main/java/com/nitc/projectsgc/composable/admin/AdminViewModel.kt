package com.nitc.projectsgc.composable.admin

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.nitc.projectsgc.composable.admin.repo.MentorsRepo
import com.nitc.projectsgc.composable.admin.repo.StudentsRepo
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
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class AdminViewModel @Inject constructor(
    val studentsRepo: StudentsRepo,
    val mentorsRepo: MentorsRepo
) : ViewModel() {


//    val studentsRepo = StudentsRepo()
//    val mentorsRepo = MentorsRepo()

    private val _student = MutableStateFlow<Either<String, Student>?>(null)
    val student: StateFlow<Either<String, Student>?> = _student.asStateFlow()


    private val _mentor = MutableStateFlow<Either<String, Mentor>?>(null)
    val mentor: StateFlow<Either<String, Mentor>?> = _mentor.asStateFlow()



    fun deleteStudentValue(){
        _student.value = null
    }
    fun deleteMentorValue(){
        _mentor.value = null
    }

    fun getStudent(rollNo: String) {
        viewModelScope.launch {
            _student.value = studentsRepo.getStudent(rollNo)
        }
    }

    suspend fun updateStudent(student: Student, oldPassword: String): Boolean {
        val updateSuccess = withContext(Dispatchers.Main) {
            Log.d("updateStudent", "In adminViewmodel")
            if (studentsRepo.updateStudent(student, oldPassword)) {
                Log.d("updateStudent", "Old password = $oldPassword")
                Log.d("updateStudent", "New password = ${student.password}")
                _student.value = Either.Right(student)
                true
            } else false
        }
        return updateSuccess
    }

    suspend fun addMentor(mentor: Mentor): Either<String,Boolean> {
        val updateSuccess = withContext(Dispatchers.Main) {
            Log.d("updateMentor", "In adminViewmodel")
            mentorsRepo.addMentor(mentor)
        }
        return updateSuccess
    }

    suspend fun updateMentor(mentor: Mentor, oldPassword: String): Boolean {
        val updateSuccess = withContext(Dispatchers.Main) {
            Log.d("updateMentor", "In adminViewmodel")
            if (mentorsRepo.updateMentor(mentor, oldPassword)) {
                Log.d("updateMentor", "Old password = $oldPassword")
                Log.d("updateMentor", "New password = ${mentor.password}")
                _mentor.value = Either.Right(mentor)
                true
            } else false
        }
        return updateSuccess
    }

    fun getMentor(username: String) {
        viewModelScope.launch {
            _mentor.value = mentorsRepo.getMentor(username)
        }
    }

    suspend fun addStudent(newStudent: Student): Either<String,Boolean> {
        val addSuccess = withContext(Dispatchers.Main) {
            Log.d("addStudent", "In adminViewmodel")
            studentsRepo.addStudent(newStudent)
        }
        return addSuccess
    }
//
//    private val _mentor = MutableStateFlow<Either<String, Mentor>?>(null)
//    val mentor:StateFlow<Either<String, Mentor>?> = _mentor.asStateFlow()
//
////    private val _updateSuccess = MutableStateFlow<Boolean>(false)
////    val updateSuccess: StateFlow<Boolean> = _updateSuccess.asStateFlow()
//    fun getMentor(username:String){
//        viewModelScope.launch {
//            _mentor.value = mentorsRepo.getMentor("health",username)
//        }
//    }
//    fun updateMentor(mentor: Mentor,oldPassword:String){
//        viewModelScope.launch {
//            if(mentorsRepo.updateMentor(mentor,oldPassword)){
//                _mentor.value = Either.Right(mentor)
//                _updateSuccess.value = true
//            }else{
//                _updateSuccess.value = false
//            }
//        }
//    }

}