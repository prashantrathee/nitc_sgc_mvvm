package com.nitc.projectsgc.composable.admin.repo

import android.content.Context
import android.util.Log
import android.widget.Toast
import arrow.core.Either
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.nitc.projectsgc.models.Appointment
import com.nitc.projectsgc.models.Student
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class StudentsRepo @Inject constructor() {

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

    suspend fun updateStudent(
        student: Student,
        oldPassword: String
    ): Boolean {
        return suspendCoroutine { continuation ->
            Log.d("updateStudent", "In repo")
            var isResumed = false
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val reference: DatabaseReference = database.reference.child("students")
            val auth: FirebaseAuth = FirebaseAuth.getInstance()
            reference.child(student.rollNo).runTransaction(object : Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    try{
//                        if(!currentData.hasChild("appointments")) currentData.child("appointments").value = student.appointments
//                        if(currentData.hasChild("appointments")){
//                            val appointments = currentData.child("appointments")
//                        }
//                        val studentFound = currentData.getValue(Student::class.java)
//                        student.appointments = studentFound!!.appointments
                        currentData.child("name").value = student.name
                        currentData.child("userName").value = student.userName
//                        currentData.child("appointments").value = student.appointments
                        currentData.child("rollNo").value = student.rollNo
                        currentData.child("emailId").value = student.emailId
                        currentData.child("phoneNumber").value = student.phoneNumber
                        currentData.child("password").value = student.password
                        currentData.child("gender").value = student.gender
                        currentData.child("dateOfBirth").value = student.dateOfBirth
                        return Transaction.success(currentData)
                    }catch(excCasting:Exception){
                        if(!isResumed){
                            isResumed = true
                            continuation.resume(false)
                        }
                        return Transaction.abort()
                    }
                }

                override fun onComplete(
                    errorDatabase: DatabaseError?,
                    committed: Boolean,
                    currentData: DataSnapshot?
                ) {
                    if(errorDatabase != null){
                        Log.d("updateStudent","Error in updating student : $errorDatabase")
                        if (!isResumed) {
                            isResumed = true
                            continuation.resume(false)
                        }
                    }else if(committed){

                        if (student.password != oldPassword) {
                            Log.d("updateStudent", "old password is not same")
                            auth.signInWithEmailAndPassword(student.emailId, oldPassword)
                                .addOnSuccessListener { loggedIn ->
                                    if (loggedIn != null) {
                                        val currentUser = FirebaseAuth.getInstance().currentUser
                                        if (currentUser != null) {
                                            currentUser.updatePassword(student.password)
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        // Show success message to the user
                                                        auth.signOut()
                                                        Log.d("updateStudent", "Updated student")
                                                        if (!isResumed) {
                                                            isResumed = true
                                                            continuation.resume(true)
                                                        }
                                                    } else {
                                                        // Password update failed, show error message to the user
                                                        Log.d(
                                                            "updateStudent",
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
                                                        "updateStudent",
                                                        "Error in updating student : $excUpdating"
                                                    )
                                                    if (!isResumed) {
                                                        isResumed = true
                                                        continuation.resume(false)
                                                    }
                                                }
                                        } else {
                                            Log.d(
                                                "updateStudent",
                                                "current user is null after logging in with old password"
                                            )
                                            if (!isResumed) {
                                                isResumed = true
                                                continuation.resume(false)
                                            }
                                        }
                                    } else {
                                        Log.d("updateStudent", "Error in logging in with old password")
                                        if (!isResumed) {
                                            isResumed = true
                                            continuation.resume(false)
                                        }
                                    }
                                }
                                .addOnFailureListener { excLogin ->
                                    Log.d(
                                        "updateStudent",
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
                    }else{
                        Log.d("updateStudent","Transaction not committed")
                        if (!isResumed) {
                            isResumed = true
                            continuation.resume(false)
                        }
                    }
                }
            })
        }
    }

    suspend fun getStudents(): ArrayList<Student>? {
        return suspendCoroutine { continuation ->
            var database: FirebaseDatabase = FirebaseDatabase.getInstance()
            var reference: DatabaseReference = database.reference.child("students")
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var studentList = arrayListOf<Student>()
                    for (student in snapshot.children) {
                        var thisStudent = student.getValue(Student::class.java)
                        if (thisStudent != null) {
                            studentList.add(thisStudent)
                        }
                    }
                    continuation.resume(studentList)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resume(null)
                }

            })
        }
    }

    suspend fun deleteStudent(rollNo: String): Boolean {
        return suspendCoroutine { continuation ->
            var isResumed = false
            var database: FirebaseDatabase = FirebaseDatabase.getInstance()
            var typeReference = database.reference.child("types")
            var studentReference: DatabaseReference = database.reference.child("students")
//        Log.d("child",reference.child(rollNo).toString())

            studentReference.child(rollNo)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(studentSnapshot: DataSnapshot) {
                        if (studentSnapshot.hasChild("appointments")) {
                            for (appointment in studentSnapshot.child("appointments").children) {
                                var studentAppointment =
                                    appointment.getValue(Appointment::class.java)!!
                                var mentorAppointmentPath =
                                    "${studentAppointment.mentorType}/${studentAppointment.mentorID}/${studentAppointment.date}/${studentAppointment.timeSlot}"
                                studentAppointment.status = "Student deleted"
                                studentAppointment.cancelled = true
                                typeReference.child(mentorAppointmentPath)
                                    .setValue(studentAppointment)
                                    .addOnCompleteListener { mentorDeleted ->
                                        if (mentorDeleted.isSuccessful) {
                                            if (!isResumed) continuation.resume(true)
                                        } else {
                                            if (!isResumed) continuation.resume(false)
                                        }
                                    }
                            }
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        if (!isResumed) continuation.resume(false)
                    }

                })


            studentReference.child(rollNo).removeValue().addOnSuccessListener {
                continuation.resume(true)
            }
                .addOnFailureListener { error ->
                    if (!isResumed) continuation.resume(false)
                }

        }
    }


    suspend fun addStudent(
        student: Student
    ): Either<String, Boolean> {
        return suspendCoroutine { continuation ->
            Log.d("addStudent", "In repo")
            var isResumed = false
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val reference: DatabaseReference = database.reference.child("students")
            Log.d("addStudent","adding student : $student")
            val auth: FirebaseAuth = FirebaseAuth.getInstance()
            reference.child(student.rollNo).setValue(student).addOnSuccessListener { task ->
                Log.d("addStudent", "in add on success")
                Log.d("addStudent", "old password is not same")
                auth.createUserWithEmailAndPassword(student.emailId, student.password)
                    .addOnSuccessListener { loggedIn ->
                        if (loggedIn != null) {
                            Log.d("addStudent", "Updated student")
                            if (!isResumed) {
                                isResumed = true
                                continuation.resume(Either.Right(true))
                            }
                        } else {
                            // Password add failed, show error message to the user
                            Log.d(
                                "addStudent",
                                "Error in adding password of current user"
                            )
                            if(!isResumed){
                                isResumed = true
                                continuation.resume(Either.Left("Error in adding student"))
                            }
                        }
                    }
                    .addOnFailureListener { excAdding ->
                        Log.d(
                            "addStudent",
                            "Error in adding student : $excAdding"
                        )
                        if(!isResumed){
                            isResumed = true
                            continuation.resume(Either.Left("Error in adding student : $excAdding"))
                        }
            }
        }.addOnFailureListener { excAdd ->
            Log.d("addStudent", "Error in adding in firebase : $excAdd")
            if(!isResumed){
                isResumed = true
                continuation.resume(Either.Left("Error in adding student : $excAdd"))
            }
        }
    }
}


}