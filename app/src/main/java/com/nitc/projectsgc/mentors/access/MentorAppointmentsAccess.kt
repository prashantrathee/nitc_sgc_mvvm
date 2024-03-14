package com.nitc.projectsgc.mentors.access

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nitc.projectsgc.models.Appointment
import com.nitc.projectsgc.SharedViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MentorAppointmentsAccess(
    var context: Context,
    var sharedViewModel: SharedViewModel
) {


    suspend fun getAppointments(today:String):ArrayList<Appointment>?{
        return suspendCoroutine { continuation ->
            var database = FirebaseDatabase.getInstance()
            var refString =
                "types/${sharedViewModel.currentMentor.type}/${sharedViewModel.currentUserID}/appointments/$today"
            Log.d("refString", refString)
            var reference = database.reference.child(sharedViewModel.currentInstitution.username!!).child(refString)
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var appointments = arrayListOf<Appointment>()
                    for (timeSlot in snapshot.children) {
                        Log.d("refString", timeSlot.key.toString())
                        appointments.add(timeSlot.getValue(Appointment::class.java)!!)
                    }
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    Log.d("appointmentsSize",appointments.size.toString())
                    val sortedAppointments =
                        appointments.sortedBy { LocalDate.parse(it.date, formatter) }
                            .toCollection(ArrayList<Appointment>())
                    Log.d("appointmentsSize",appointments.size.toString())
                    continuation.resume(sortedAppointments)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error : $error", Toast.LENGTH_LONG).show()
                    continuation.resume(null)
                }

            })
        }
    }


    suspend fun cancelAppointment(appointment: Appointment):Boolean{
        return suspendCoroutine { continuation ->
            var database = FirebaseDatabase.getInstance()
            var refString =
                "types/${sharedViewModel.currentMentor.type}/${sharedViewModel.currentUserID}/appointments/${appointment.date}/${appointment.timeSlot}"
            Log.d("refString", refString)
            var isResumed = false
            var mentorReference =
                database.reference.child(sharedViewModel.currentInstitution.username!!)
                    .child(refString)
            var reference =
                database.reference.child(sharedViewModel.currentInstitution.username!!)
            mentorReference.setValue(appointment).addOnCompleteListener { cancelTask ->
                if (cancelTask.isSuccessful) {
                    var studentRefString =
                        "students/${appointment.studentID}/appointments/${appointment.id}"
                    reference.child(studentRefString).setValue(appointment)
                        .addOnCompleteListener { studentTask ->
                            if (studentTask.isSuccessful) if (!isResumed) continuation.resume(true)
                            else {
                                Toast.makeText(
                                    context,
                                    "Some error occurred. Try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                                if(!isResumed) continuation.resume(false)
                            }
                        }
                } else {
                    Toast.makeText(context, "Some error occurred. Try again", Toast.LENGTH_SHORT)
                        .show()
                    if(!isResumed) continuation.resume(false)
                }
            }.addOnFailureListener { errCanceling->
                Log.d("cancelAppointment","Error in cancelling appointment : $errCanceling")
                Toast.makeText(context,"Error in cancelling appointment",Toast.LENGTH_SHORT).show()
                if(!isResumed) continuation.resume(false)
            }
        }
    }


    suspend fun getStudentRecord(studentID:String):ArrayList<Appointment>?{
        return suspendCoroutine { continuation ->
            var isResumed = false
            var appointments = arrayListOf<Appointment>()
            var database = FirebaseDatabase.getInstance()
            var refString = "students/${studentID}/appointments"
            var mentorReference =
                database.reference.child(sharedViewModel.currentInstitution.username!!)
                    .child(refString)
            mentorReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        var appointment = ds.getValue(Appointment::class.java)
                        if (appointment != null) {
                            if (appointment.mentorID != sharedViewModel.currentUserID && appointment.completed == true) {
                                appointments.add(appointment)
                            }
                        }
                    }
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    val sortedAppointments =
                        appointments.sortedBy { LocalDate.parse(it.date, formatter) }
                            .toCollection(ArrayList<Appointment>())
                    if(!isResumed) continuation.resume(sortedAppointments)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error : $error", Toast.LENGTH_LONG).show()
                    if(!isResumed) continuation.resume(null)
                }

            })
        }
    }


    suspend fun giveRemarks(appointment: Appointment):Boolean{
        return suspendCoroutine { continuation ->
            var isResumed = false
            var database = FirebaseDatabase.getInstance()
            var refString = "students/${appointment.studentID}/appointments"
            var studentReference =
                database.reference.child(sharedViewModel.currentInstitution.username!!)
                    .child(refString)
            studentReference.child(appointment.id.toString()).setValue(appointment)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var mentorRefString =
                            "types/${sharedViewModel.currentMentor.type}/${sharedViewModel.currentUserID}/appointments/${appointment.date}/${appointment.timeSlot}"
                        var mentorReference =
                            database.reference.child(sharedViewModel.currentInstitution.username!!)
                                .child(mentorRefString)
                        mentorReference.setValue(appointment).addOnCompleteListener { mentorTask ->
                            if (mentorTask.isSuccessful) {
                                Toast.makeText(context, "Submitted", Toast.LENGTH_SHORT).show()
                                if(!isResumed) continuation.resume(true)
                            } else {
                                Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT)
                                    .show()
                                if(!isResumed) continuation.resume(false)
                            }
                        }
                    } else {
                        Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show()
                        if(!isResumed) continuation.resume(false)
                    }
                }.addOnFailureListener { errGiveRemarks->
                    Log.d("giveRemarks","Error in giving remarks : $errGiveRemarks")
                    Toast.makeText(context,"Error in giving remarks",Toast.LENGTH_SHORT).show()
                    if(!isResumed) continuation.resume(false)
                }
        }
    }


}