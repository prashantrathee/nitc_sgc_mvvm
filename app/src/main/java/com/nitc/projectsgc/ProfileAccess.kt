package com.nitc.projectsgc

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nitc.projectsgc.models.Admin
import com.nitc.projectsgc.models.Mentor
import com.nitc.projectsgc.models.Student
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ProfileAccess(var context: Context,var sharedViewModel: SharedViewModel,var parentFragment: Fragment) {

    suspend fun getProfile():Boolean{


        return suspendCoroutine { continuation ->

//            var profileLive = MutableLiveData<Boolean>(false)
            val sharedPreferences =
                parentFragment.requireActivity()
                    ?.getSharedPreferences("sgcLogin", Context.MODE_PRIVATE)
            if (sharedPreferences != null) {


                val institutionUsername = sharedPreferences!!.getString("institutionUsername", null)

                if (institutionUsername != null) {
                    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
                    val reference: DatabaseReference = database.reference.child(institutionUsername)
                    var auth = FirebaseAuth.getInstance()
                    Log.d("sharedP", "not null")
                    var loggedIn = sharedPreferences.getBoolean("loggedIn", false)
                    if (loggedIn) {
                        Log.d("sharedP", "logged in")
                        var userType = sharedPreferences.getString("userType", null)
                        if (userType != null) {
                            Log.d("sharedP", "user type not null")
                            var email = sharedPreferences.getString("email", null)
                            var password = sharedPreferences.getString("password", null)
                            var username = sharedPreferences.getString("username", null)
                            if (email != null && password != null) {
                                Log.d("sharedP", "email and password not null")
                                auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { authTask ->
                                        if (authTask.isSuccessful) {
                                            Log.d("sharedP", "auth successful")
                                            when (userType) {
                                                "Student" -> {
                                                    if (username != null) {
                                                        Log.d("sharedP", "username not null")
                                                        reference.child("students")
                                                            .addListenerForSingleValueEvent(object :
                                                                ValueEventListener {
                                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                                    Log.d(
                                                                        "sharedP",
                                                                        "current student got"
                                                                    )

                                                                    if (snapshot.hasChild(username)) {
                                                                        sharedViewModel.userType =
                                                                            userType
                                                                        sharedViewModel.currentStudent =
                                                                            snapshot.child(username)
                                                                                .getValue(Student::class.java)!!
                                                                        sharedViewModel.currentUserID =
                                                                            username
//                                                                    profileLive.postValue(true)
                                                                        Log.d(
                                                                            "sharedP",
                                                                            "return statement"
                                                                        )
//                                                                callback(true)
                                                                        continuation.resume(true)
                                                                        return
                                                                    } else {
                                                                        auth.currentUser!!.delete()
                                                                            .addOnCompleteListener { deleteTask ->
                                                                                if (deleteTask.isSuccessful) {
                                                                                    Toast.makeText(
                                                                                        context,
                                                                                        "Your account has been removed by admin",
                                                                                        Toast.LENGTH_SHORT
                                                                                    ).show()
                                                                                    continuation.resume(
                                                                                        false
                                                                                    )
                                                                                } else {
                                                                                    continuation.resume(
                                                                                        false
                                                                                    )
                                                                                }
                                                                            }
                                                                    }
                                                                }

                                                                override fun onCancelled(error: DatabaseError) {
                                                                    Toast.makeText(
                                                                        context,
                                                                        "Error : $error",
                                                                        Toast.LENGTH_LONG
                                                                    ).show()
//                                                                profileLive.postValue(false)
//                                                                callback(false)
                                                                    continuation.resume(false)
                                                                    return
                                                                }

                                                            })
                                                    }
                                                }

                                                "Mentor" -> {
                                                    if (username != null) {
                                                        var mentorType =
                                                            sharedPreferences.getString(
                                                                "mentorType",
                                                                null
                                                            )
                                                        Log.d(
                                                            "username",
                                                            "mentor username = ${username.toString()}"
                                                        )
                                                        if (mentorType != null) {
                                                            reference.child("types")
                                                                .addListenerForSingleValueEvent(
                                                                    object :
                                                                        ValueEventListener {
                                                                        override fun onDataChange(
                                                                            snapshot: DataSnapshot
                                                                        ) {
                                                                            if (snapshot.hasChild(
                                                                                    mentorType
                                                                                )
                                                                            ) {
                                                                                var mentorPath =
                                                                                    "$mentorType/$username"
                                                                                if (snapshot.hasChild(
                                                                                        mentorPath
                                                                                    )
                                                                                ) {
                                                                                    sharedViewModel.currentMentor =
                                                                                        snapshot.child(
                                                                                            mentorPath
                                                                                        ).getValue(
                                                                                            Mentor::class.java
                                                                                        )!!
                                                                                    sharedViewModel.userType =
                                                                                        userType
//                                                                            profileLive.postValue(true)
                                                                                    sharedViewModel.currentUserID =
                                                                                        username
                                                                                    continuation.resume(
                                                                                        true
                                                                                    )
                                                                                    return
                                                                                } else {
                                                                                    auth.currentUser!!.delete()
                                                                                        .addOnCompleteListener { deleteTask ->
                                                                                            if (deleteTask.isSuccessful) {
                                                                                                Toast.makeText(
                                                                                                    context,
                                                                                                    "Your account has been removed by admin",
                                                                                                    Toast.LENGTH_SHORT
                                                                                                )
                                                                                                    .show()
                                                                                                continuation.resume(
                                                                                                    false
                                                                                                )
                                                                                            } else {
                                                                                                continuation.resume(
                                                                                                    false
                                                                                                )
                                                                                            }
                                                                                        }
                                                                                    continuation.resume(
                                                                                        false
                                                                                    )
                                                                                    return
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
                                                                                            continuation.resume(
                                                                                                false
                                                                                            )
                                                                                        } else {
                                                                                            continuation.resume(
                                                                                                false
                                                                                            )

                                                                                        }
                                                                                    }
                                                                                continuation.resume(
                                                                                    false
                                                                                )
                                                                                return
                                                                            }

                                                                        }

                                                                        override fun onCancelled(
                                                                            error: DatabaseError
                                                                        ) {
                                                                            Toast.makeText(
                                                                                context,
                                                                                "Error : $error",
                                                                                Toast.LENGTH_LONG
                                                                            ).show()
//                                                                    profileLive.postValue(false)
//                                                                    callback(false)
                                                                            continuation.resume(
                                                                                false
                                                                            )
                                                                            return
                                                                        }

                                                                    })
                                                        }
                                                    }
                                                }

                                                "Admin" -> {
                                                    Log.d(
                                                        "emailForProfile",
                                                        username!!.length.toString()
                                                    )
                                                    if (username != null) {
                                                        reference.child("admins").child(username)
                                                            .addListenerForSingleValueEvent(object :
                                                                ValueEventListener {
                                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                                    if (snapshot.exists()) {
                                                                        sharedViewModel.currentAdmin =
                                                                            snapshot.getValue(Admin::class.java)!!
                                                                        sharedViewModel.userType =
                                                                            "Admin"
                                                                        sharedViewModel.currentUserID =
                                                                            username
                                                                        Log.d(
                                                                            "getProfile",
                                                                            "Admin found"
                                                                        )
                                                                        continuation.resume(true)
                                                                    } else {
                                                                        Log.d(
                                                                            "getProfile",
                                                                            "Snapshot does not exist"
                                                                        )
                                                                        continuation.resume(false)
                                                                    }
                                                                }

                                                                override fun onCancelled(error: DatabaseError) {
                                                                    Toast.makeText(
                                                                        context,
                                                                        "Database error ",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                    Log.d(
                                                                        "getProfile",
                                                                        "Database error : $error"
                                                                    )
                                                                    continuation.resume(false)
                                                                }
                                                            })
                                                    } else {
                                                        continuation.resume(false)
                                                    }
                                                }

                                            }
                                        }
                                    }
                                    .addOnFailureListener { exc ->
                                        Toast.makeText(
                                            context,
                                            "Exception : $exc",
                                            Toast.LENGTH_LONG
                                        ).show()
//                                    profileLive.postValue(false)
                                        continuation.resume(false)
                                    }
                            } else continuation.resume(false)
                        } else continuation.resume(false)
                    } else {
                        continuation.resume(false)
                    }
                } else {
                    continuation.resume(false)
                }
            }
        }
    }

//    fun saveFCM(userType:String,mentorType:String,username:String){
//        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
//                return@addOnCompleteListener
//            }
//
//            // Get the FCM token
//            val token = task.result
//            val database = FirebaseDatabase.getInstance()
//            if(userType == "Mentor") {
//                val reference = database.reference.child(sharedViewModel.username).child("types/$mentorType/$username")
//                val updates = mapOf<String, String>(
//                    "fcmToken" to token
//                )
//                reference.updateChildren(updates).addOnCompleteListener {fcmTask->
//                    if(fcmTask.isSuccessful){
//                        Log.d("fcm","successfully stored")
//                    }else{
//                        Log.d("fcm","not stored")
//                    }
//                }
//            }
//            else{
//                val reference = database.reference.child(sharedViewModel.username).child("students/$username")
//                val updates = mapOf<String, String>(
//                    "fcmToken" to token
//                )
//                reference.updateChildren(updates).addOnCompleteListener {fcmTask->
//                    if(fcmTask.isSuccessful){
//                        Log.d("fcm","successfully stored")
//                    }else{
//                        Log.d("fcm","not stored")
//                    }
//                }
//            }
//
//            // Do something with the token (e.g., store it in a database, send it to a server, etc.)
//            Log.d("FCM", "FCM registration token: $token")
//        }
//    }

}