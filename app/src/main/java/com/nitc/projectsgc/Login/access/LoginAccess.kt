package com.nitc.projectsgc.Login.access

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nitc.projectsgc.DataAccess
import com.nitc.projectsgc.models.Admin
import com.nitc.projectsgc.models.Institution
import com.nitc.projectsgc.models.Mentor
import com.nitc.projectsgc.SharedViewModel
import com.nitc.projectsgc.models.Student
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoginAccess(
    var context: Context,
    var parentFragment: Fragment,
    var sharedViewModel: SharedViewModel
) {


    suspend fun login(
        email: String,
        password: String,
        userType: Int,
        username: String,
        mentorType: String,
        emailSuffix: String
    ): Boolean {
        return suspendCoroutine { continuation ->
            var dataAccess = DataAccess(context, parentFragment, sharedViewModel)
            val institutionUsername = emailSuffix.replace(Regex("[^a-zA-Z0-9]+"), "_")
            var isResumed = false
            var database: FirebaseDatabase = FirebaseDatabase.getInstance()
            var reference: DatabaseReference = database.reference.child(institutionUsername)
            var auth: FirebaseAuth = FirebaseAuth.getInstance()
            var verified = 0
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    verified = 1
                    var verification = auth.currentUser?.isEmailVerified
                    if (userType == 2) verification = true
                    if (verification == true) {
                        var sharedPreferences = parentFragment.activity?.getSharedPreferences(
                            "sgcLogin",
                            Context.MODE_PRIVATE
                        )
                        val editor = sharedPreferences?.edit()
                        sharedViewModel.currentUserID = username
                        sharedViewModel.userType = userType
                        when (userType) {
                            1 -> {
                                reference.child("students")
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.hasChild(username)) {
                                                sharedViewModel.currentStudent =
                                                    snapshot.child(username)
                                                        .getValue(Student::class.java)!!
                                                var saved = dataAccess.saveUsername(
                                                    password,
                                                    userType,
                                                    mentorType,
                                                    email,
                                                    username,
                                                    institutionUsername
                                                )
                                                if (saved == true) {
                                                    reference.addListenerForSingleValueEvent(object :
                                                        ValueEventListener {
                                                        override fun onDataChange(snapshot: DataSnapshot) {
                                                            sharedViewModel.currentInstitution =
                                                                snapshot.getValue(
                                                                    Institution::class.java
                                                                )!!
                                                            continuation.resume(true)
                                                        }

                                                        override fun onCancelled(error: DatabaseError) {
                                                            Toast.makeText(
                                                                context,
                                                                "Could not get institution details",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            Log.d(
                                                                "login",
                                                                "Error in getting institution : $error"
                                                            )
                                                            if (!isResumed) {
                                                                isResumed = true
                                                                continuation.resume(false)
                                                            }
                                                        }
                                                    })
                                                } else if (!isResumed) {
                                                    isResumed = true
                                                    continuation.resume(false)
                                                }
                                            } else {
                                                auth.currentUser!!.delete()
                                                    .addOnCompleteListener { deleteTask ->
                                                        if (deleteTask.isSuccessful) {
                                                            Toast.makeText(
                                                                context,
                                                                "Your account has been removed by admin",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            if (editor != null) {
                                                                editor.remove("loggedIn")
                                                                editor.remove("password")
                                                                editor.remove("userType")
                                                                editor.remove("mentorType")
                                                                editor.remove("institutionUsername")
                                                                editor.remove("email")
                                                                editor.remove("username")
                                                                editor.apply()
                                                            }
                                                            if (!isResumed) {
                                                                isResumed = true
                                                                continuation.resume(false)
                                                            }
                                                        } else {
                                                            if (!isResumed) {
                                                                isResumed = true
                                                                continuation.resume(false)
                                                            }
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

                                        override fun onCancelled(error: DatabaseError) {
                                            Toast.makeText(
                                                context,
                                                "Some error : $error",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            Log.d("loginError", "Database error : $error")
                                            if (!isResumed) {
                                                isResumed = true
                                                continuation.resume(false)
                                            }
                                        }

                                    })
                            }

                            2 -> {
                                reference.child("types")
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.hasChild(mentorType)) {
                                                var mentorPath = "$mentorType/$username"
                                                if (snapshot.hasChild(mentorPath)) {
                                                    sharedViewModel.currentMentor =
                                                        snapshot.child(mentorPath)
                                                            .getValue(Mentor::class.java)!!
                                                    var saved = dataAccess.saveUsername(
                                                        password,
                                                        userType,
                                                        mentorType,
                                                        email,
                                                        username,
                                                        institutionUsername
                                                    )
                                                    if (saved) {
                                                        reference.addListenerForSingleValueEvent(
                                                            object : ValueEventListener {
                                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                                    sharedViewModel.currentInstitution =
                                                                        snapshot.getValue(
                                                                            Institution::class.java
                                                                        )!!
                                                                    if (!isResumed) {
                                                                        isResumed = true
                                                                        continuation.resume(true)
                                                                    }
                                                                }

                                                                override fun onCancelled(error: DatabaseError) {
                                                                    Toast.makeText(
                                                                        context,
                                                                        "Could not get institution details",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                    Log.d(
                                                                        "login",
                                                                        "Error in getting institution : $error"
                                                                    )
                                                                    if (!isResumed) {
                                                                        isResumed = true
                                                                        continuation.resume(false)
                                                                    }
                                                                }
                                                            })
                                                    } else if (!isResumed) {
                                                        isResumed = true
                                                        continuation.resume(false)
                                                    }
                                                } else {
                                                    auth.currentUser!!.delete()
                                                        .addOnCompleteListener { deleteTask ->
                                                            if (deleteTask.isSuccessful) {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Your account has been removed by admin",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                                if (editor != null) {
                                                                    editor.remove("loggedIn")
                                                                    editor.remove("password")
                                                                    editor.remove("userType")
                                                                    editor.remove("mentorType")
                                                                    editor.remove("institutionUsername")
                                                                    editor.remove("email")
                                                                    editor.remove("username")
                                                                    editor.apply()
                                                                }
                                                                if (!isResumed) {
                                                                    isResumed = true
                                                                    continuation.resume(false)
                                                                }
                                                            } else {
                                                                if (!isResumed) {
                                                                    isResumed = true
                                                                    continuation.resume(false)
                                                                }
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
//                                                continuation.resume(false)
                                                }
                                            } else {
                                                auth.currentUser!!.delete()
                                                    .addOnCompleteListener { deleteTask ->
                                                        if (deleteTask.isSuccessful) {
                                                            Toast.makeText(
                                                                context,
                                                                "Your account has been removed by admin",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            if (editor != null) {
                                                                editor.remove("loggedIn")
                                                                editor.remove("password")
                                                                editor.remove("userType")
                                                                editor.remove("mentorType")
                                                                editor.remove("institutionUsername")
                                                                editor.remove("email")
                                                                editor.remove("username")
                                                                editor.apply()
                                                            }
                                                            if (!isResumed) {
                                                                isResumed = true
                                                                continuation.resume(false)
                                                            }
                                                        } else {
                                                            Toast.makeText(
                                                                context,
                                                                "Some error occurred. Try again",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            if (!isResumed) {
                                                                isResumed = true
                                                                continuation.resume(false)
                                                            }
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
//                                            continuation.resume(false)
                                            }

                                        }


                                        override fun onCancelled(error: DatabaseError) {
                                            Toast.makeText(
                                                context,
                                                "Some error : $error",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            if (!isResumed) {
                                                isResumed = true
                                                continuation.resume(false)
                                            }
                                        }

                                    })
                            }

                            0 -> {
                                val userName = email.substring(0, email.indexOf('@'))
                                reference.child("admins")
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.hasChild(userName)) {
                                                sharedViewModel.currentUserID = userName
                                                sharedViewModel.currentAdmin =
                                                    snapshot.child(userName)
                                                        .getValue(Admin::class.java)!!
                                                var saved = dataAccess.saveUsername(
                                                    password,
                                                    userType,
                                                    mentorType,
                                                    email,
                                                    userName,
                                                    institutionUsername
                                                )
                                                if (saved) {
                                                    reference.addListenerForSingleValueEvent(object :
                                                        ValueEventListener {
                                                        override fun onDataChange(snapshot: DataSnapshot) {
                                                            sharedViewModel.currentInstitution =
                                                                snapshot.getValue(
                                                                    Institution::class.java
                                                                )!!
                                                            if (!isResumed) {
                                                                isResumed = true
                                                                continuation.resume(true)
                                                            }
                                                        }

                                                        override fun onCancelled(error: DatabaseError) {
                                                            Toast.makeText(
                                                                context,
                                                                "Could not get institution details",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            Log.d(
                                                                "login",
                                                                "Error in getting institution : $error"
                                                            )
                                                            if (!isResumed) {
                                                                isResumed = true
                                                                continuation.resume(false)
                                                            }
                                                        }
                                                    })
                                                } else if (!isResumed) {
                                                    isResumed = true
                                                    continuation.resume(false)
                                                }
                                            } else {
                                                if (!isResumed) {
                                                    isResumed = true
                                                    continuation.resume(false)
                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Toast.makeText(
                                                context,
                                                "Error in accessing database",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            Log.d("loginError", "Error : $error")
                                            if (!isResumed) {
                                                isResumed = true
                                                continuation.resume(false)
                                            }
                                        }
                                    })
                            }
                        }
                        if (editor != null) {
                            editor.putBoolean("loggedIn", true)
                            editor.putString("password", password)
                            editor.putInt("userType", userType)
                            editor.putString("mentorType", mentorType)
                            editor.putString("email", email)
                            editor.putString("institutionUsername", institutionUsername)
                            editor.putString("username", username)
                            editor.apply()
                        }
                    } else {
                        auth.currentUser!!.sendEmailVerification()
                            .addOnCompleteListener { emailSent ->
                                Toast.makeText(
                                    context,
                                    "Email sent. Please verify your email",
                                    Toast.LENGTH_SHORT
                                ).show()
                                if (!isResumed) {
                                    isResumed = true
                                    continuation.resume(false)
                                }
                            }
                            .addOnFailureListener { excSend ->
                                Log.d("deleteAccount", "Exception - $excSend")
                                if (!isResumed) {
                                    isResumed = true
                                    continuation.resume(false)
                                }
                            }
//                    continuation.resume(false)
                    }

                } else {
//                val errorCode = (task.exception as FirebaseException).
//                if (errorCode == "ERROR_USER_NOT_FOUND") {
//                    // Show error message to the user
//                    Toast.makeText(context, "Email address not found", Toast.LENGTH_SHORT).show()
//                    continuation.resume(false)
//                } else {
//                if(verified == 0){
                    Log.d("login", "this is executed")
                    Toast.makeText(
                        context,
                        "Wrong credentials entered. Try again",
                        Toast.LENGTH_SHORT
                    ).show()
//                    verified = -1
                    if (!isResumed) {
                        isResumed = true
                        continuation.resume(false)
                    }
//                }
//                }

                }
            }
                .addOnFailureListener { errAuth ->
//                if(verified == 0){
                    Toast.makeText(context, "Wrong credentials entered", Toast.LENGTH_SHORT).show()
                    if (!isResumed) {
                        isResumed = true
                        continuation.resume(false)
                    }
//                }
                    Log.d("loginError", "here     Database error : ${errAuth.message}")
                }
        }
    }

    fun logout(): Boolean {
//        auth.signOut()

        var deleted = DataAccess(
            context,
            parentFragment,
            sharedViewModel
        ).deleteData()
        return deleted
    }

}