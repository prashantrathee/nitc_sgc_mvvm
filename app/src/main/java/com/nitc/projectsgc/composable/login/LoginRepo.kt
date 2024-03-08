package com.nitc.projectsgc.composable.login

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoginRepo {

    suspend fun login(
        username: String,
        userType: Int,
        password: String,
        mentorType: String
    ): Boolean {
        return suspendCoroutine { continuation ->
//            var dataAccess = DataAccess(context, parentFragment, sharedViewModel)
            var isResumed = false
            var database: FirebaseDatabase = FirebaseDatabase.getInstance()
            var reference: DatabaseReference = database.reference
            var auth: FirebaseAuth = FirebaseAuth.getInstance()
            Log.d("loginCredentials","Credentials : $username and password : $password and usertype = $userType")
            var verified = 0
            auth.signInWithEmailAndPassword(username, password).addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    verified = 1
                    var verification = auth.currentUser?.isEmailVerified
                    if (userType == 0) verification = true
                    if (verification == true) {
                        Log.d("loginSuccess","Login is successful with username and password")
                        when (userType) {
                            1 -> {
                                reference.child("students").child(username.substring(username.indexOf('_'),username.indexOf('@')))
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            Log.d("loginSuccess","Login is successful with database")
                                            if (snapshot.hasChild(username)) {
                                                if (!isResumed) {
                                                    isResumed = true
                                                    continuation.resume(true)

                                                } else {
                                                    auth.currentUser!!.delete()
                                                        .addOnCompleteListener { deleteTask ->
                                                            if (!isResumed) {
                                                                isResumed = true
                                                                continuation.resume(false)
                                                            }
                                                        }
                                                        .addOnFailureListener { excDelete ->
                                                            Log.d(
                                                                "deleteAccount",
                                                                "Exception - $excDelete"
                                                            )
                                                            if (!isResumed) {
                                                                isResumed = true
                                                                continuation.resume(false)
                                                            }
                                                        }
                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Log.d("loginError", "Database error : $error")
                                            if (!isResumed) {
                                                isResumed = true
                                                continuation.resume(false)
                                            }
                                        }

                                    })
                            }

                            2 -> {
                                reference.child("types").child(mentorType).child(username)
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.exists()) {
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

                                        override fun onCancelled(error: DatabaseError) {
                                            Log.d("login", "Error in getting data : $error")
                                            if (!isResumed) {
                                                isResumed = true
                                                continuation.resume(false)
                                            }
                                        }
                                    })
                            }
                            0->{
                                reference.child("admin").addListenerForSingleValueEvent(object:ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
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

                                    override fun onCancelled(error: DatabaseError) {
                                        Log.d("login", "Error in getting data : $error")
                                        if (!isResumed) {
                                            isResumed = true
                                            continuation.resume(false)
                                        }
                                    }
                                })
                            }
                        }
                    }
                }


            }.addOnFailureListener { excAuth->
                Log.d("login","Error in login : $excAuth")
                if(!isResumed){
                    isResumed = true
                    continuation.resume(false)
                }
            }
        }
    }
}

//fun logout(): Boolean {
////        auth.signOut()
//
//    var deleted = DataAccess(
//        context,
//        parentFragment,
//        sharedViewModel
//    ).deleteData()
//    return deleted
//}