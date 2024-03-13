package com.nitc.projectsgc.models

data class Appointment(
    var date:String = "",
    var timeSlot:String = "",
    var mentorType:String = "",
    var rescheduled:Boolean = false,
    var mentorID:String = "",
    var studentID:String = "",
    var mentorName:String = "",
    var studentName:String = "",
    var completed:Boolean = false,
    var status:String = "",
    var remarks:String = "",
    var cancelled:Boolean = false,
    var expanded:Boolean = false,
    var id:String = "",
    var problemDescription:String? = ""
)
