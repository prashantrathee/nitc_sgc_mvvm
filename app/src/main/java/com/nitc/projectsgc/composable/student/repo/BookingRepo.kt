package com.nitc.projectsgc.composable.student.repo

import android.app.AlertDialog
import android.util.Log
import androidx.compose.ui.res.stringArrayResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import arrow.core.Either
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nitc.projectsgc.R
import com.nitc.projectsgc.models.Appointment
import com.nitc.projectsgc.models.Mentor
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BookingRepo @Inject constructor() {

    suspend fun getAppointments(rollNo: String): Either<String, List<Appointment>>? {
        return suspendCoroutine { continuation ->
            var isResumed = false
            var appointments = arrayListOf<Appointment>()
            var database = FirebaseDatabase.getInstance()
            var reference = database.reference.child("students")
            Log.d("studentDashboard", "outside snapshot")
            reference.child("$rollNo")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Log.d("studentDashboard", "In snapshot")
                        if (snapshot.hasChild("appointments")) {

                            for (ds in snapshot.child("appointments").children) {
                                try {
                                    val appointment = ds.getValue(Appointment::class.java)
                                    appointments.add(appointment!!)
                                } catch (excCasting: Exception) {
                                    Log.d(
                                        "getAppointments",
                                        "Error in casting appointment : $excCasting"
                                    )
                                    continue
                                }
                            }
                            if (!isResumed) {
                                isResumed = true
                                continuation.resume(Either.Right(appointments))
                            }
                        } else {
                            Log.d("studentDashboard", "No appointment found here")
                            if (!isResumed) {
                                isResumed = true
                                continuation.resume(Either.Right(appointments))
                            }
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        if (!isResumed) {
                            isResumed = true
                            Log.d("getAppointments", "Error in database : $error")
                            continuation.resume(Either.Left("Error in database : $error"))
                        }
                    }
                })
        }
    }

    suspend fun getCompletedAppointments(rollNo: String): ArrayList<Appointment>? {
        return suspendCoroutine { continuation ->
            var completedLive = MutableLiveData<ArrayList<Appointment>>(null)
            var appointments = arrayListOf<Appointment>()
            var database = FirebaseDatabase.getInstance()
            var studentReference = database.reference.child("students")
            studentReference.child(rollNo)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (ds in snapshot.child("appointments").children) {
                            var appointment = ds.getValue(Appointment::class.java)
                            if (appointment != null) {
                                if (appointment.completed) appointments.add(appointment)
                            }
                        }
                        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        val sortedAppointments =
                            appointments.sortedBy { LocalDate.parse(it.date, formatter) }
                                .toCollection(ArrayList<Appointment>())
                        continuation.resume(sortedAppointments)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resume(null)
                    }

                })
        }
    }


    suspend fun cancelAppointment(appointment: Appointment): Boolean {
        return suspendCoroutine { continuation ->
            var isResumed = false
            var database = FirebaseDatabase.getInstance()
            var studentReference =
                database.reference
                    .child("students")
            var typeReference =
                database.reference
                    .child("types")
            studentReference.child(appointment.studentID + "/appointments")
                .child(appointment.id.toString()).setValue(appointment)
                .addOnCompleteListener { studentTask ->
                    if (studentTask.isSuccessful) {
                        typeReference.child(appointment.mentorType.toString() + "/${appointment.mentorID}/appointments/${appointment.date}/${appointment.timeSlot}")
                            .setValue(appointment).addOnCompleteListener { mentorTask ->
                                if (mentorTask.isSuccessful) if (!isResumed) continuation.resume(
                                    true
                                )
                                else if (!isResumed) continuation.resume(false)
                            }
                    } else {
                        if (!isResumed) continuation.resume(false)
                    }
                }
        }
    }


    suspend fun getMentorNames(mentorType: String): Either<String, List<Mentor>>? {
        return suspendCoroutine { continuation ->
            var isResumed = false
            var mentors = arrayListOf<Mentor>()
            var database: FirebaseDatabase = FirebaseDatabase.getInstance()
            var reference: DatabaseReference = database.reference.child("types")

            reference.addListenerForSingleValueEvent(object : ValueEventListener {


                override fun onDataChange(snapshot: DataSnapshot) {

                    var mentorsSnapshot = snapshot.child(mentorType).children

                    for (mentor in mentorsSnapshot) {
                        try {
                            mentors.add(mentor.getValue(Mentor::class.java)!!)
                        } catch (excCasting: Exception) {
                            Log.d("getMentorNames", "Error in casting mentor : $excCasting")
                            continue
                        }
                    }
                    Log.d("getMentorNames", "Mentor names found : $mentors")
                    if (!isResumed) {
                        continuation.resume(Either.Right(mentors))
                        isResumed = true
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                    if (!isResumed) {
                        continuation.resume(Either.Left("Error in getting mentors : $error"))
                        isResumed = true
                    }
                }

            })
        }
    }

    suspend fun getMentorTypes(): Either<String, ArrayList<String>>? {
        return suspendCoroutine { continuation ->

//            var mentorTypeLive = MutableLiveData<ArrayList<String>>(null)
            var isResumed = false
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val reference: DatabaseReference = database.reference.child("types")
            var mentorTypes = arrayListOf<String>()
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (typeChild in snapshot.children) {
                        mentorTypes.add(typeChild.key.toString())
                    }
                    if (!isResumed) {
                        continuation.resume(Either.Right(mentorTypes))
                        isResumed = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (!isResumed) {
                        continuation.resume(Either.Left("Error in getting mentor types : $error"))
                        isResumed = true
                    }
                }

            })
        }
    }

    suspend fun bookAppointment(appointment: Appointment): Boolean {
        return suspendCoroutine { continuation ->
            var isResumed = false
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            var reference: DatabaseReference =
                database.reference
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
                                mentorReference.child(appointment.timeSlot.toString())
                                    .removeValue()
                                if (!isResumed) continuation.resume(false)
                            }
                        }
                } else if (!isResumed) continuation.resume(false)
            }
        }
    }

    suspend fun getAvailableTimeSlots(
        mentorTypeSelected: String,
        mentorID: String,
        selectedDate: String
    ): Either<String, List<String>>? {
        return suspendCoroutine { continuation ->
            var isResumed = false
            var totalTimeSlots =
                arrayListOf<String>("9-10", "10-11", "11-12", "1-2", "2-3", "3-4", "4-5")
            var mentorTimeSlots = arrayListOf<String>()
            val reference = FirebaseDatabase.getInstance().reference.child("types")
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var dateRef = "$mentorTypeSelected/$mentorID/appointments/$selectedDate"
                    if (snapshot.hasChild(dateRef)) {
                        Log.d("getSlots", "dateref : $dateRef")
                        for (timeSlots in snapshot.child(dateRef).children) {
                            mentorTimeSlots.add(timeSlots.key.toString())
                            totalTimeSlots.removeIf { it == timeSlots.key.toString() }
                            Log.d("getSlots", timeSlots.key.toString())
                        }
//                        for (timeslot in totalTimeSlots) {
//                            if (!mentorTimeSlots.contains(timeslot)) {
//                                availableTimeSlots.add(timeslot)
//                            }
//                        }
                    }
                    if (!isResumed) {
                        isResumed = true
                        continuation.resume(Either.Right(totalTimeSlots))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("getAvailableTimeSlots", "Error in database : $error")
                    if (!isResumed) {
                        isResumed = true
                        continuation.resume(Either.Left("Error in database : $error"))
                    }
                }

            })
        }
    }

    suspend fun rescheduleAppointment(
        oldAppointment: Appointment,
        appointment: Appointment
    ): Boolean {
        return suspendCoroutine { continuation ->
            var isResumed = false
            oldAppointment.status =
                "Rescheduled to " + appointment.date + " " + appointment.timeSlot
            oldAppointment.rescheduled = true
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            var reference: DatabaseReference =
                database.reference
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
                                                        if (!isResumed) continuation.resume(
                                                            false
                                                        )
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