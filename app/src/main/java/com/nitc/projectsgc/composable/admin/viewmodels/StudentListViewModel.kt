package com.nitc.projectsgc.composable.admin.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import com.nitc.projectsgc.composable.admin.repo.MentorsRepo
import com.nitc.projectsgc.composable.admin.repo.StudentsRepo
import com.nitc.projectsgc.models.Mentor
import com.nitc.projectsgc.models.Student
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StudentListViewModel:ViewModel() {

    private val _studentList = MutableStateFlow(emptyList<Student>())
    val studentList: StateFlow<List<Student>> = _studentList


    fun getStudents(context:Context){
        try{
            viewModelScope.launch {
                val students = StudentsRepo(context).getStudents()
                if(students != null) _studentList.value = students
            }
        }catch(exc:Exception){
            Toast.makeText(context,"Error in getting students : $exc",Toast.LENGTH_LONG).show()
            Log.d("getStudents","Error in getting students : $exc")
        }
    }


    fun deleteStudent(context: Context,rollNo:String) {
        try{
            viewModelScope.launch {
                val deleted = StudentsRepo(context).deleteStudent(rollNo)
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