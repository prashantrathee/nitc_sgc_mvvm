package com.nitc.projectsgc.composable.student.repo

import android.util.Log
import android.widget.Toast
import arrow.core.Either
import arrow.core.right
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nitc.projectsgc.models.Appointment
import com.nitc.projectsgc.models.Student
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class StudentRepo @Inject constructor()  {



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


    suspend fun updateProfile(
        student: Student,
        oldPassword: String
    ): Boolean {
        return suspendCoroutine { continuation ->
            Log.d("updateStudent", "In repo")
            var isResumed = false
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val reference: DatabaseReference = database.reference.child("students")
            val auth: FirebaseAuth = FirebaseAuth.getInstance()
            reference.child(student.rollNo).setValue(student).addOnSuccessListener { task ->
                Log.d("updateStudent", "in add on success")
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
                                                if(!isResumed){
                                                    isResumed = true
                                                    continuation.resume(true)
                                                }
                                            } else {
                                                // Password update failed, show error message to the user
                                                Log.d(
                                                    "updateStudent",
                                                    "Error in updating password of current user"
                                                )
                                                if(!isResumed){
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
                                            if(!isResumed){
                                                isResumed = true
                                                continuation.resume(false)
                                            }
                                        }
                                } else {
                                    Log.d(
                                        "updateStudent",
                                        "current user is null after logging in with old password"
                                    )
                                    if(!isResumed){
                                        isResumed = true
                                        continuation.resume(false)
                                    }
                                }
                            } else {
                                Log.d("updateStudent", "Error in logging in with old password")
                                if(!isResumed){
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
                            if(!isResumed){
                                isResumed = true
                                continuation.resume(false)
                            }
                        }
                } else {
                    if(!isResumed){
                        isResumed = true
                        continuation.resume(true)
                    }
                }
            }.addOnFailureListener { excUpdate ->
                Log.d("updateStudent", "Error in updating in firebase : $excUpdate")
                if(!isResumed){
                    isResumed = true
                    continuation.resume(false)
                }
            }
        }
    }


}