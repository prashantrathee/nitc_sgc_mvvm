package com.nitc.projectsgc.composable.mentor.repo

import android.util.Log
import arrow.core.Either
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.nitc.projectsgc.composable.util.PathUtils
import com.nitc.projectsgc.models.Appointment
import com.nitc.projectsgc.models.Mentor
import com.nitc.projectsgc.models.Student
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MentorRepo @Inject constructor() {


    suspend fun getStudent(studentID: String): Either<String, Student> {
        return suspendCoroutine { continuation ->
            var isResumed = false
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val reference: DatabaseReference =
                database.reference
                    .child("students")
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        var student = snapshot.child(studentID).getValue(Student::class.java)
                        if (student != null) {
                            if (!isResumed) {
                                isResumed = true
                                continuation.resume(Either.Right(student))
                            }
                        } else {
                            if (!isResumed) {
                                isResumed = true
                                continuation.resume(Either.Left("Could not resolve student"))
                            }
                        }
                    } catch (exc: Exception) {
                        if (!isResumed) {
                            isResumed = true
                            continuation.resume(Either.Left("Error in getting student : $exc"))
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (!isResumed) {
                        isResumed = true
                        continuation.resume(Either.Left("Error : $error"))
                    }
                }

            })
        }
    }

    suspend fun getProfile(username: String): Either<String, Mentor> {
        return suspendCoroutine { continuation ->
            var isResumed = false
            var database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val mentorTypeEither = PathUtils.getMentorType(username)
            when (mentorTypeEither) {
                is Either.Left -> {
                    if (!isResumed) {
                        isResumed = true
                        continuation.resume(Either.Left(mentorTypeEither.value.message!!))
                    }
                }

                is Either.Right -> {
                    var reference: DatabaseReference =
                        database.reference.child("types/${mentorTypeEither.value}/${username}")

                    reference.addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {
                            try {
                                val mentor = snapshot.getValue(Mentor::class.java)
                                if (!isResumed) {
                                    isResumed = true
                                    continuation.resume(Either.Right(mentor!!))
                                }
                            } catch (exc: Exception) {
                                Log.d("getMentor", "Error in getting mentor : $exc")
                                if (!isResumed) {
                                    isResumed = true
                                    continuation.resume(Either.Left("Error in getting mentor : $exc"))
                                }
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.d("getMentor", "Database Error : $error")
                            if (!isResumed) {
                                continuation.resume(Either.Left("Database error ; $error"))
                                isResumed = true
                            }
                        }
                    })

                }
            }
        }
    }



    suspend fun updateProfile(
        mentor: Mentor,
        oldPassword: String
    ): Boolean {
        return suspendCoroutine { continuation ->
            Log.d("updateMentor", "In repo")
            var isResumed = false
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val reference: DatabaseReference = database.reference.child("types")
            when (val mentorTypeEither = PathUtils.getMentorType(mentor.userName)) {
                is Either.Left -> {
                    if (!isResumed) {
                        isResumed = true
                        continuation.resume(false)
                    }
                }

                is Either.Right -> {
                    val mentorPath = "${mentorTypeEither.value}/${mentor.userName}"
                    val auth: FirebaseAuth = FirebaseAuth.getInstance()

                    reference.child(mentorPath).runTransaction(object : Transaction.Handler {
                        override fun doTransaction(currentData: MutableData): Transaction.Result {
                            return try {
                                currentData.child("name").value = mentor.name
                                currentData.child("userName").value = mentor.userName
                                currentData.child("type").value = mentor.type
                                currentData.child("email").value = mentor.email
                                currentData.child("phone").value = mentor.phone
                                currentData.child("password").value = mentor.password
                                Transaction.success(currentData)
                            } catch (excCasting: Exception) {
                                if (!isResumed) {
                                    isResumed = true
                                    continuation.resume(false)
                                }
                                Transaction.abort()
                            }
                        }

                        override fun onComplete(
                            errorDatabase: DatabaseError?,
                            committed: Boolean,
                            currentData: DataSnapshot?
                        ) {
                            if (errorDatabase != null) {
                                Log.d("updateStudent", "Error in updating student : $errorDatabase")
                                if (!isResumed) {
                                    isResumed = true
                                    continuation.resume(false)
                                }
                            } else if (committed) {

                                Log.d("updateMentor", "in add on success")
                                if (mentor.password != oldPassword) {
                                    Log.d("updateMentor", "old password is not same")
                                    auth.signInWithEmailAndPassword(mentor.email, oldPassword)
                                        .addOnSuccessListener { loggedIn ->
                                            if (loggedIn != null) {
                                                val currentUser =
                                                    FirebaseAuth.getInstance().currentUser
                                                if (currentUser != null) {
                                                    currentUser.updatePassword(mentor.password)
                                                        .addOnCompleteListener { task ->
                                                            if (task.isSuccessful) {
                                                                // Show success message to the user
                                                                auth.signOut()
                                                                Log.d(
                                                                    "updateMentor",
                                                                    "Updated mentor"
                                                                )
                                                                if (!isResumed) {
                                                                    isResumed = true
                                                                    continuation.resume(true)
                                                                }
                                                            } else {
                                                                // Password update failed, show error message to the user
                                                                Log.d(
                                                                    "updateMentor",
                                                                    "Error in updating password of current user"
                                                                )
                                                                if (!isResumed) {
                                                                    isResumed = true
                                                                    continuation.resume(false)
                                                                }
                                                            }
                                                        }
                                                        .addOnFailureListener { excUpdating ->
                                                            Log.d(
                                                                "updateMentor",
                                                                "Error in updating mentor : $excUpdating"
                                                            )
                                                            if (!isResumed) {
                                                                isResumed = true
                                                                continuation.resume(false)
                                                            }
                                                        }
                                                } else {
                                                    Log.d(
                                                        "updateMentor",
                                                        "current user is null after logging in with old password"
                                                    )
                                                    if (!isResumed) {
                                                        isResumed = true
                                                        continuation.resume(false)
                                                    }
                                                }
                                            } else {
                                                Log.d(
                                                    "updateMentor",
                                                    "Error in logging in with old password"
                                                )
                                                if (!isResumed) {
                                                    isResumed = true
                                                    continuation.resume(false)
                                                }
                                            }
                                        }
                                        .addOnFailureListener { excLogin ->
                                            Log.d(
                                                "updateMentor",
                                                "Error in logging in with old password : $excLogin"
                                            )
                                            if (!isResumed) {
                                                isResumed = true
                                                continuation.resume(false)
                                            }
                                        }
                                } else {
                                    if (!isResumed) {
                                        isResumed = true
                                        continuation.resume(true)
                                    }
                                }
                            } else {
                                Log.d("updateMentor", "Transaction not committed")
                                if (!isResumed) {
                                    isResumed = true
                                    continuation.resume(false)
                                }
                            }
                        }
                    })
                }
            }
        }
    }
    suspend fun getTodayAppointments(
        username: String,
        today: String
    ): Either<String, ArrayList<Appointment>>? {
        return suspendCoroutine { continuation ->
            var database = FirebaseDatabase.getInstance()
            var isResumed = false
            when(val mentorTypeEither = PathUtils.getMentorType(username)){
                is Either.Left->{
                    if(!isResumed){
                        isResumed = true
                        continuation.resume(Either.Left(mentorTypeEither.value.message!!))
                    }
                }
                is Either.Right->{

                    var refString = "types/${mentorTypeEither.value}/${username}/appointments/${today}"
                    Log.d("refString", refString)
                    var reference = database.reference.child(refString)
                    reference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var appointments = arrayListOf<Appointment>()
                            for (timeSlot in snapshot.children) {
                                Log.d("refString", timeSlot.key.toString())
                                try {
                                    appointments.add(timeSlot.getValue(Appointment::class.java)!!)
                                } catch (excCasting: Exception) {
                                    Log.d("getAppointments", "Error in casting : $excCasting")
                                    continue
                                }
                            }
                            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                            Log.d("appointmentsSize", appointments.size.toString())
                            val sortedAppointments =
                                appointments.sortedBy { LocalDate.parse(it.date, formatter) }
                                    .toCollection(ArrayList<Appointment>())
                            Log.d("getAppointments", "Size : ${appointments.size}")
                            if (!isResumed) {
                                isResumed = true
                                Log.d("getAppointments","Appointments : ${sortedAppointments.size}")
                                continuation.resume(Either.Right(sortedAppointments))
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.d("getAppointments", "Error in database ; $error")
                            if (!isResumed) {
                                isResumed = true
                                continuation.resume(Either.Left("Error in database ; $error"))
                            }
                        }

                    })
                }
            }
        }
    }

    suspend fun cancelAppointment(appointment: Appointment): Boolean {
        return suspendCoroutine { continuation ->
            var database = FirebaseDatabase.getInstance()
            var refString =
                "types/${appointment.mentorType}/${appointment.mentorID}/appointments/${appointment.date}/${appointment.timeSlot}"
            Log.d("refString", refString)
            var isResumed = false
            var mentorReference =
                database.reference
                    .child(refString)
            var reference =
                database.reference
            mentorReference.setValue(appointment).addOnCompleteListener { cancelTask ->
                if (cancelTask.isSuccessful) {
                    var studentRefString =
                        "students/${appointment.studentID}/appointments/${appointment.id}"
                    reference.child(studentRefString).setValue(appointment)
                        .addOnCompleteListener { studentTask ->
                            if (studentTask.isSuccessful) if (!isResumed) continuation.resume(true)
                            else {
                                if (!isResumed) {
                                    isResumed = true
                                    continuation.resume(false)
                                }
                            }
                        }
                } else {
                    if (!isResumed) {
                        isResumed = true
                        continuation.resume(false)
                    }
                }
            }.addOnFailureListener { errCanceling ->
                Log.d("cancelAppointment", "Error in cancelling appointment : $errCanceling")
                if (!isResumed) {
                    isResumed = true
                    continuation.resume(false)
                }
            }
        }
    }


    suspend fun getStudentRecord(
        mentorUsername: String,
        studentID: String
    ): ArrayList<Appointment>? {
        return suspendCoroutine { continuation ->
            var isResumed = false
            var appointments = arrayListOf<Appointment>()
            var database = FirebaseDatabase.getInstance()
            var refString = "students/${studentID}/appointments"
            var mentorReference =
                database.reference
                    .child(refString)
            mentorReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        var appointment = ds.getValue(Appointment::class.java)
                        if (appointment != null) {
                            if (appointment.mentorID != mentorUsername && appointment.completed) {
                                appointments.add(appointment)
                            }
                        }
                    }
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    val sortedAppointments =
                        appointments.sortedBy { LocalDate.parse(it.date, formatter) }
                            .toCollection(ArrayList<Appointment>())
                    if (!isResumed) continuation.resume(sortedAppointments)
                }

                override fun onCancelled(error: DatabaseError) {
                    if (!isResumed) continuation.resume(null)
                }

            })
        }
    }


    suspend fun giveRemarks(appointment: Appointment): Boolean {
        return suspendCoroutine { continuation ->
            var isResumed = false
            var database = FirebaseDatabase.getInstance()
            var refString = "students/${appointment.studentID}/appointments"
            var studentReference =
                database.reference
                    .child(refString)
            studentReference.child(appointment.id.toString()).setValue(appointment)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var mentorRefString =
                            "types/${appointment.mentorType}/${appointment.mentorID}/appointments/${appointment.date}/${appointment.timeSlot}"
                        var mentorReference =
                            database.reference
                                .child(mentorRefString)
                        mentorReference.setValue(appointment).addOnCompleteListener { mentorTask ->
                            if (mentorTask.isSuccessful) {
                                if (!isResumed) {
                                    isResumed = true
                                    continuation.resume(true)
                                }
                            } else {
                                if (!isResumed) {
                                    isResumed = true
                                    continuation.resume(false)
                                }
                            }
                        }
                    } else {
                        if (!isResumed) {
                            isResumed = true
                            continuation.resume(false)
                        }
                    }
                }.addOnFailureListener { errGiveRemarks ->
                    Log.d("giveRemarks", "Error in giving remarks : $errGiveRemarks")
                    if (!isResumed) {
                        isResumed = true
                        continuation.resume(false)
                    }
                }
        }
    }

}