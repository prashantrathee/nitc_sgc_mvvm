package com.nitc.projectsgc

import androidx.lifecycle.ViewModel
import com.nitc.projectsgc.models.Admin
import com.nitc.projectsgc.models.Appointment
import com.nitc.projectsgc.models.Event
import com.nitc.projectsgc.models.Institution
import com.nitc.projectsgc.models.Mentor
import com.nitc.projectsgc.models.Student

class SharedViewModel:ViewModel() {

    lateinit var currentInstitution: Institution
//    var institutionUsername:String = "NA"
    lateinit var currentMentor: Mentor
    lateinit var currentAdmin: Admin
    lateinit var currentStudent: Student
    lateinit var idForStudentProfile:String
    lateinit var mentorIDForProfile:String
    lateinit var mentorTypeForProfile:String
    var currentUserID = "NA"
    var userType = "Student"
    var rescheduling:Boolean = false
    lateinit var viewAppointmentStudentID:String
    var reschedulingMentorName = "NA"
    lateinit var pastRecordStudentID:String
    lateinit var reschedulingAppointment: Appointment
    var isUpdatingEvent:Boolean = false
    lateinit var updatingOldEvent: Event
}