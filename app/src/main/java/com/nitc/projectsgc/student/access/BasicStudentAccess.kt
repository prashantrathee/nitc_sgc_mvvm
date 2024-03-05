package com.nitc.projectsgc.student.access

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nitc.projectsgc.SharedViewModel
import com.nitc.projectsgc.models.Student
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BasicStudentAccess(
    var context: Context,
    var sharedViewModel: SharedViewModel
) {

    suspend fun getStudent(studentID:String): Student?{
        return suspendCoroutine { continuation ->
            var isResumed = false
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val reference: DatabaseReference =
                database.reference.child(sharedViewModel.currentInstitution.username!!)
                    .child("students")
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var student = snapshot.child(studentID).getValue(Student::class.java)!!
                    if(!isResumed) continuation.resume(student)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error : $error", Toast.LENGTH_LONG).show()

                }

            })
        }
    }

    suspend fun updateStudent(
        student: Student,
        oldPassword:String
    ): Boolean {
        return suspendCoroutine { continuation ->
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val reference: DatabaseReference = database.reference.child(sharedViewModel.currentInstitution.username!!).child("students")
            val auth: FirebaseAuth = FirebaseAuth.getInstance()
            reference.child(student.rollNo).setValue(student).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("accessAddMentor", "here in addMentor access")
                    if(student.password != oldPassword) {
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        currentUser?.updatePassword(student.password)?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Show success message to the user
                                Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
                                continuation.resume(true)
                            } else {
                                // Password update failed, show error message to the user
                                Toast.makeText(context, "Password update failed", Toast.LENGTH_SHORT).show()
                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(true)
                    }
                } else {
                    continuation.resume(false)
                    Log.d("accessAddMentor", "not success")
                }

            }
        }
    }
}
