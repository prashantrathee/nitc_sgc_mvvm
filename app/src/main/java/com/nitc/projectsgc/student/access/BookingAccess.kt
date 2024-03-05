package com.nitc.projectsgc.student.access

import android.content.Context
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nitc.projectsgc.models.Appointment
import com.nitc.projectsgc.SharedViewModel
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BookingAccess(var context: Context,var sharedViewModel: SharedViewModel) {

    suspend fun bookAppointment(appointment: Appointment): Boolean {
        return suspendCoroutine { continuation ->
            var isResumed = false
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            var reference: DatabaseReference =
                database.reference.child(sharedViewModel.currentInstitution.username!!)
            var mentorReference =
                reference.child("types/${appointment.mentorType}/${appointment.mentorID}/appointments/${appointment.date}/${appointment.timeSlot}")
            var appointmentID = mentorReference.push().key.toString()
            appointment.id = appointmentID
            mentorReference.setValue(appointment).addOnCompleteListener { mentorTask ->
                if (mentorTask.isSuccessful) {
                    reference.child("students/${appointment.studentID}/appointments/$appointmentID")
                        .setValue(appointment).addOnCompleteListener { studentTask ->
                            if (studentTask.isSuccessful) {
                                if (!isResumed) continuation.resume(true)
                            } else {
                                mentorReference.child(appointment.timeSlot.toString()).removeValue()
                                if (!isResumed) continuation.resume(false)
                            }
                        }
                } else if (!isResumed) continuation.resume(false)
            }
        }
    }

    suspend fun rescheduleAppointment(appointment: Appointment): Boolean {
        return suspendCoroutine { continuation ->
            var isResumed = false
            var oldAppointment = sharedViewModel.reschedulingAppointment
            oldAppointment.status =
                "Rescheduled to " + appointment.date + " " + appointment.timeSlot
            oldAppointment.rescheduled = true
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            var reference: DatabaseReference =
                database.reference.child(sharedViewModel.currentInstitution.username!!)
            var mentorNewReference =
                reference.child("types/${appointment.mentorType}/${appointment.mentorID}/appointments/${appointment.date}/${appointment.timeSlot}")
            var appointmentID = mentorNewReference.push().key.toString()
            appointment.id = appointmentID
            var mentorOldReference =
                reference.child("types/${oldAppointment.mentorType}/${oldAppointment.mentorID}/appointments/${oldAppointment.date}/${oldAppointment.timeSlot}")
            mentorOldReference.removeValue().addOnCompleteListener { oldMentorTask ->
                if (oldMentorTask.isSuccessful) {
                    reference.child("students/${oldAppointment.studentID}/appointments/${oldAppointment.id}")
                        .removeValue().addOnCompleteListener { oldStudentTask ->
                            if (oldStudentTask.isSuccessful) {
                                mentorNewReference.setValue(appointment)
                                    .addOnCompleteListener { mentorTask ->
                                        if (mentorTask.isSuccessful) {
                                            reference.child("students/${appointment.studentID}/appointments/$appointmentID")
                                                .setValue(appointment)
                                                .addOnCompleteListener { studentTask ->
                                                    if (studentTask.isSuccessful) {
                                                        if (!isResumed) continuation.resume(true)
                                                    } else {
                                                        mentorNewReference.child(appointment.timeSlot.toString())
                                                            .removeValue()
                                                        if (!isResumed) continuation.resume(false)
                                                    }
                                                }
                                        } else if (!isResumed) continuation.resume(false)
                                    }
                            } else if (!isResumed) continuation.resume(false)
                        }
                } else if (!isResumed) continuation.resume(false)
            }.addOnFailureListener { errOldDeletion ->
                Log.d("reschedule", "Error in deleting old booking : $errOldDeletion")
                if (!isResumed) continuation.resume(false)
            }

        }
    }
}
