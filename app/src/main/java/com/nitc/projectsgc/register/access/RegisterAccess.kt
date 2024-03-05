package com.nitc.projectsgc.register.access

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nitc.projectsgc.models.Admin
import com.nitc.projectsgc.models.Institution
import com.nitc.projectsgc.models.Student
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RegisterAccess(
    var context: Context
) {


    suspend fun addAdmin(admin: Admin, emailSuffix:String):Boolean{
        return suspendCoroutine {continuation ->
            val database = FirebaseDatabase.getInstance()
            val reference = database.reference.child(emailSuffix)
            val auth = FirebaseAuth.getInstance()
            reference.child("admins").addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.hasChild(admin.username!!)){
                        Toast.makeText(context,"Admin is already added with this email",Toast.LENGTH_SHORT).show()
                        continuation.resume(false)
                    }else{
                        reference.child("admins").child(admin.username!!).setValue(admin).addOnCompleteListener { adminAdded->
                            if(adminAdded.isSuccessful){
                                auth.createUserWithEmailAndPassword(admin.email!!,admin.password!!).addOnCompleteListener { authAdded->
                                    if(authAdded.isSuccessful){

                                        auth.currentUser!!.sendEmailVerification().addOnCompleteListener {emailSent->
                                            if(emailSent.isSuccessful){
                                                continuation.resume(true)
                                            }else{
                                                Toast.makeText(context,"Error in sending verification email",Toast.LENGTH_SHORT).show()
                                                continuation.resume(false)
                                            }
                                        }.addOnFailureListener{errorEmail->
                                            Toast.makeText(context,"Server error in sending email verification",Toast.LENGTH_SHORT).show()
                                            continuation.resume(false)
                                        }
                                    }else{
                                        Toast.makeText(context,"Could not add admin",Toast.LENGTH_SHORT).show()
                                        continuation.resume(false)
                                    }
                                }.addOnFailureListener {excAuth->
                                    Toast.makeText(context,"Could not setup email and password for admin, database error",Toast.LENGTH_SHORT).show()
                                    Log.d("addAdmin","Could not setup email and password for admin. Error : $excAuth")
                                    continuation.resume(false)
                                }
//                                continuation.resume(true)
                            }else{
                                Toast.makeText(context,"Database error",Toast.LENGTH_SHORT).show()
                                continuation.resume(false)
                            }
                        }.addOnFailureListener { excAdd->
                            Toast.makeText(context,"Could not add admin",Toast.LENGTH_SHORT).show()
                            Log.d("addAdmin","Error ; $excAdd")
                            continuation.resume(false)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context,"Error in accessing database",Toast.LENGTH_SHORT).show()
                    Log.d("addAdmin","Error : $error")
                    continuation.resume(false)
                }
            })
        }
    }

    suspend fun register(
        student: Student,
        institutionUsername: String
    ):Boolean {
//        val emailString = student.emailId.replace(Regex("[^a-zA-Z0-9]+"),"_")
        return suspendCoroutine { continuation ->
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val reference: DatabaseReference = database.reference.child(institutionUsername).child("students")
            val auth: FirebaseAuth = FirebaseAuth.getInstance()
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.hasChild(student.userName)) {
                        Toast.makeText(context,"Already registered with given mail ID",Toast.LENGTH_SHORT).show()
                        continuation.resume(false)
                    }else{
                        reference.child(student.userName).setValue(student).addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                Log.d("accessRegister", "here in register access")
                                auth.createUserWithEmailAndPassword(
                                    student.emailId,
                                    student.password
                                ).addOnCompleteListener { authTask ->
                                    if (authTask.isSuccessful) {
                                        auth.currentUser?.sendEmailVerification()
                                            ?.addOnSuccessListener {
                                                Toast.makeText(
                                                    context,
                                                    "Please verify your email",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                continuation.resume(true)
                                            }
                                            ?.addOnFailureListener {
                                                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT)
                                                    .show()
                                                reference.child(student.rollNo).removeValue()
                                                continuation.resume(false)
                                            }
                                    } else {
                                        reference.child(student.rollNo).removeValue()
                                        continuation.resume(false)
                                    }
                                }
                            } else {
                                continuation.resume(false)
                                Log.d("accessRegister", "not success")
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("databaseError","error : $error")
                }
            })

        }
    }
}