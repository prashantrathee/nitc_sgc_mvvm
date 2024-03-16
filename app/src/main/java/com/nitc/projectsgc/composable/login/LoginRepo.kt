package com.nitc.projectsgc.composable.login

import android.util.Log
import arrow.core.Either
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nitc.projectsgc.composable.util.PathUtils
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class LoginRepo {

    suspend fun login(
        username: String,
        userType: Int,
        password: String
    ): Either<String,Boolean> {
        return suspendCoroutine { continuation ->
//            var dataAccess = DataAccess(context, parentFragment, sharedViewModel)
            var isResumed = false
            var database: FirebaseDatabase = FirebaseDatabase.getInstance()
            var reference: DatabaseReference = database.reference
            var auth: FirebaseAuth = FirebaseAuth.getInstance()
            Log.d(
                "loginCredentials",
                "Credentials : $username and password : $password and usertype = $userType"
            )
            var verified = 0
            auth.signInWithEmailAndPassword(username, password).addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    verified = 1
                    var verification = auth.currentUser?.isEmailVerified
                    if (userType == 0) verification = true
                    if (verification == true) {
                        Log.d("loginSuccess", "Login is successful with username and password")
                        when (userType) {
                            1 -> {
                                reference.child("students").child(
                                    username.substring(
                                        username.indexOf('_'),
                                        username.indexOf('@')
                                    )
                                )
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            Log.d(
                                                "loginSuccess",
                                                "Login is successful with database"
                                            )
                                            if (snapshot.hasChild(username)) {
                                                if (!isResumed) {
                                                    isResumed = true
                                                    continuation.resume(Either.Right(true))

                                                } else {
                                                    auth.currentUser!!.delete()
                                                        .addOnCompleteListener { deleteTask ->
                                                            if (!isResumed) {
                                                                isResumed = true
                                                                continuation.resume(Either.Left("User not found"))
                                                            }
                                                        }
                                                        .addOnFailureListener { excDelete ->
                                                            Log.d(
                                                                "deleteAccount",
                                                                "Exception - $excDelete"
                                                            )
                                                            if (!isResumed) {
                                                                isResumed = true
                                                                continuation.resume(Either.Left("User not found"))
                                                            }
                                                        }
                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Log.d("loginError", "Database error : $error")
                                            if (!isResumed) {
                                                isResumed = true
                                                continuation.resume(Either.Left("Database error : $error"))
                                            }
                                        }

                                    })
                            }

                            2 -> {
                                when (val mentorTypeEither = PathUtils.getMentorType(username)) {
                                    is Either.Left -> {
                                        Log.d(
                                            "login",
                                            "Error in mentor username : ${mentorTypeEither.value.message!!}"
                                        )
                                        if (!isResumed) {
                                            isResumed = true
                                            continuation.resume(Either.Left(mentorTypeEither.value.message!!))
                                        }
                                    }

                                    is Either.Right -> {
                                        reference.child("types").child(mentorTypeEither.value)
                                            .child(username)
                                            .addListenerForSingleValueEvent(object :
                                                ValueEventListener {
                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                    if (snapshot.exists()) {
                                                        Log.d(
                                                            "loginSuccess",
                                                            "Login is successful with database"
                                                        )
                                                        if (!isResumed) {
                                                            isResumed = true
                                                            continuation.resume(Either.Right(true))
                                                        }
                                                    } else {
                                                        if (!isResumed) {
                                                            isResumed = true
                                                            continuation.resume(Either.Left("User not found"))
                                                        }
                                                    }
                                                }

                                                override fun onCancelled(error: DatabaseError) {
                                                    Log.d("login", "Error in getting data : $error")
                                                    if (!isResumed) {
                                                        isResumed = true
                                                        continuation.resume(Either.Left("Error in getting data : $error"))
                                                    }
                                                }
                                            })
                                    }
                                }
                            }

                            0 -> {
                                reference.child("admin")
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.exists()) {
                                                Log.d(
                                                    "loginSuccess",
                                                    "Login is successful with database"
                                                )
                                                if (!isResumed) {
                                                    isResumed = true
                                                    continuation.resume(Either.Right(true))
                                                }
                                            } else {
                                                if (!isResumed) {
                                                    isResumed = true
                                                    continuation.resume(Either.Left("User not found"))
                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Log.d("login", "Error in getting data : $error")
                                            if (!isResumed) {
                                                isResumed = true
                                                continuation.resume(Either.Left("Error in getting data : $error"))
                                            }
                                        }
                                    })
                            }
                        }
                    }
                }


            }.addOnFailureListener { excAuth ->
                Log.d("login", "Error in login : $excAuth")
                if (!isResumed) {
                    isResumed = true
                    continuation.resume(Either.Left("Error in login : $excAuth"))
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