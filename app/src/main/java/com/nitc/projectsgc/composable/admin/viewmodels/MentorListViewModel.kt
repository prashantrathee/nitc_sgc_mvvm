package com.nitc.projectsgc.composable.admin.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nitc.projectsgc.composable.admin.repo.MentorsRepo
import com.nitc.projectsgc.models.Mentor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MentorListViewModel @Inject constructor(
    private val mentorsRepo: MentorsRepo
):ViewModel() {



    private val _mentorList = MutableStateFlow(emptyList<Mentor>())
    val mentorList: StateFlow<List<Mentor>> = _mentorList



    fun getMentors(context: Context){
        try{
            viewModelScope.launch {
                val mentors = mentorsRepo.getMentors()
                if(mentors != null) _mentorList.value = mentors
            }
        }catch(exc:Exception){
            Toast.makeText(context,"Error in getting mentors : $exc", Toast.LENGTH_LONG).show()
            Log.d("getStudents","Error in getting mentors : $exc")
        }
    }


    fun deleteMentor(context: Context,username:String) {
        try{
            viewModelScope.launch {
                val deleted = mentorsRepo.deleteMentor(username)
                if(deleted){
                    val mentorList = _mentorList.value.filter{
                        it.userName != username
                    }
                    _mentorList.value = mentorList
                }
            }
        }catch(exc:Exception) {
            Toast.makeText(context, "Error in deleting mentor : $exc", Toast.LENGTH_LONG).show()
            Log.d("getStudents", "Error in deleting mentor : $exc")
        }
    }

}