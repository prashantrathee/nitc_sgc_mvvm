package com.nitc.projectsgc

import androidx.lifecycle.ViewModel

class SharedViewModel:ViewModel() {

    lateinit var currentMentor:Mentor
    lateinit var currentStudent: Student
    var currentUserID = "NA"
    var userType = "Student"
}