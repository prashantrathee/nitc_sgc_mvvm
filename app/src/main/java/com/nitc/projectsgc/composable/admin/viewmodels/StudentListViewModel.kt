package com.nitc.projectsgc.composable.admin.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import arrow.core.Either
import com.nitc.projectsgc.composable.admin.repo.MentorsRepo
import com.nitc.projectsgc.composable.admin.repo.StudentsRepo
import com.nitc.projectsgc.models.Appointment
import com.nitc.projectsgc.models.Mentor
import com.nitc.projectsgc.models.Student
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentListViewModel @Inject constructor(
    private val studentsRepo: StudentsRepo
):ViewModel() {

    private val _studentList = MutableStateFlow(emptyList<Student>())
    val studentList: StateFlow<List<Student>> = _studentList

    private val _studentAppointments = MutableStateFlow<Either<String, List<Appointment>>?>(null)
    val studentAppointments : StateFlow<Either<String,List<Appointment>>?> = _studentAppointments.asStateFlow()

    fun getStudents(context:Context){
        try{
            viewModelScope.launch {
                val students = studentsRepo.getStudents()
                if(students != null) _studentList.value = students
            }
        }catch(exc:Exception){
            Toast.makeText(context,"Error in getting students : $exc",Toast.LENGTH_LONG).show()
            Log.d("getStudents","Error in getting students : $exc")
        }
    }

    fun deleteStudentAppointments(){
        _studentAppointments.value = null
    }
    fun getStudentAppointments(rollNo: String){
        viewModelScope.launch {
            _studentAppointments.value = studentsRepo.getAppointments(rollNo)
        }
    }

    fun deleteStudent(context: Context,rollNo:String) {
        try{
            viewModelScope.launch {
                val deleted = studentsRepo.deleteStudent(rollNo)
                if(deleted){
                    val studentList = _studentList.value.filter{
                        it.rollNo != rollNo
                    }
                    _studentList.value = studentList
                }
            }
        }catch(exc:Exception) {
            Toast.makeText(context, "Error in deleting mentor : $exc", Toast.LENGTH_LONG).show()
            Log.d("getStudents", "Error in deleting mentor : $exc")
        }
    }


}