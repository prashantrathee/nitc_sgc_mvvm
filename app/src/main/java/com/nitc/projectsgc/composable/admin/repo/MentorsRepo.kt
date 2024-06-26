package com.nitc.projectsgc.composable.admin.repo

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
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MentorsRepo @Inject constructor() {


    suspend fun getMentors(): ArrayList<Mentor>? {
        return suspendCoroutine { continuation ->
            var database: FirebaseDatabase = FirebaseDatabase.getInstance()
            var reference: DatabaseReference = database.reference.child("types")
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var typeList = arrayListOf<String>()
                    var mentorList = arrayListOf<Mentor>()
                    for (typeOfMentor in snapshot.children) {
                        typeList.add(typeOfMentor.key.toString())
                    }
                    for (typeOfMentor in typeList) {
                        for (mentor in snapshot.child(typeOfMentor).children) {
                            Log.d("mentorCheck", mentor.toString())
                            var thisMentor = mentor.getValue(Mentor::class.java)
                            if (thisMentor != null) {
                                mentorList.add(thisMentor)
                            }
                        }
                    }
                    continuation.resume(mentorList)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resume(null)
                }

            })
        }
    }


    suspend fun addMentor(
        mentor: Mentor
    ): Either<String, Boolean> {
        return suspendCoroutine { continuation ->
            var isResumed = false
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val reference: DatabaseReference =
                database.reference.child("types").child(mentor.type)
            val auth: FirebaseAuth = FirebaseAuth.getInstance()
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.hasChild(mentor.userName)) {
                        reference.child(mentor.userName)
                            .setValue(mentor).addOnSuccessListener { task ->
                                Log.d("addMentor", "here in addMentor access")
//                                    continuation.resume(true)
                                auth.createUserWithEmailAndPassword(
                                    mentor.email,
                                    mentor.password
                                ).addOnSuccessListener { authTask ->
                                    if (authTask != null) {
                                        Log.d("addMentor", "auth task is successful")
                                        if (!isResumed) {
                                            isResumed = true
                                            continuation.resume(Either.Right(true))
                                        }
                                    } else {
                                        Log.d("addMentor", "auth task is not successful")
                                        reference.child(mentor.userName).removeValue()
                                        if (!isResumed) {
                                            isResumed = true
                                            continuation.resume(Either.Left("Error in adding mentor"))
                                        }
                                    }
                                }.addOnFailureListener { errAuth ->
                                    Log.d("addMentor", "Error in adding auth mentor : $errAuth")
                                    if (!isResumed) {
                                        isResumed = true
                                        continuation.resume(Either.Left("Error in adding mentor : $errAuth"))
                                    }
                                }
                            }
                            .addOnFailureListener { errAdd ->
                                Log.d("addMentor", "Error in adding mentor : $errAdd")
                                if (!isResumed) {
                                    isResumed = true
                                    continuation.resume(Either.Left("Error in adding mentor : $errAdd"))
                                }
                            }
                    } else {
                        if (!isResumed) {
                            isResumed = true
                            continuation.resume(Either.Left("Mentor already exists with given username"))
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("addMentor", "Database error : $error")
                    if (!isResumed) {
                        isResumed = true
                        continuation.resume(Either.Left("Error in adding mentor : $error"))
                    }
                }
            })

        }
    }

    suspend fun getMentor(username: String): Either<String, Mentor> {
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

    suspend fun getMentorNames(mentorType: String): ArrayList<Mentor>? {
        return suspendCoroutine { continuation ->
            var isResumed = false
            var mentors = arrayListOf<Mentor>()
            var database: FirebaseDatabase = FirebaseDatabase.getInstance()
            var reference: DatabaseReference = database.reference.child("types")

            reference.addListenerForSingleValueEvent(object : ValueEventListener {


                override fun onDataChange(snapshot: DataSnapshot) {

                    var mentorsSnapshot = snapshot.child(mentorType).children

                    for (mentor in mentorsSnapshot) {
                        mentors.add(mentor.getValue(Mentor::class.java)!!)
                    }
                    if (!isResumed) {
                        continuation.resume(mentors)
                        isResumed = true
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                    if (!isResumed) {
                        continuation.resume(null)
                        isResumed = true
                    }
                }

            })
        }
    }

    suspend fun getMentorTypes(): ArrayList<String>? {
        return suspendCoroutine { continuation ->

//            var mentorTypeLive = MutableLiveData<ArrayList<String>>(null)
            var isResumed = false
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val reference: DatabaseReference = database.reference.child("types")
            var mentorTypes = arrayListOf<String>()
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (typeChild in snapshot.children) {
                        mentorTypes.add(typeChild.key.toString())
                    }
                    if (!isResumed) {
                        continuation.resume(mentorTypes)
                        isResumed = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (!isResumed) {
                        continuation.resume(null)
                        isResumed = true
                    }
                }

            })
        }
    }

    suspend fun deleteMentor(userName: String): Boolean {
        return suspendCoroutine { continuation ->
            var database: FirebaseDatabase = FirebaseDatabase.getInstance()
            var typeReference: DatabaseReference = database.reference.child("types")
            var isResumed = false
            val mentorTypeEither = PathUtils.getMentorType(userName)
            when (mentorTypeEither) {
                is Either.Left -> {
                    if (!isResumed) {
                        isResumed = true
                        continuation.resume(false)
                    }
                }

                is Either.Right -> {
                    var mentorPath = "$mentorTypeEither.value/$userName"
                    Log.d("deleteMentor", mentorPath)
                    typeReference.child(mentorPath)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (ds in snapshot.child("appointments").children) {
                                    for (timeSlot in ds.children) {
                                        var appointment =
                                            timeSlot.getValue(Appointment::class.java)!!
                                        var studentReference =
                                            "students/${appointment.studentID}/appointments/${appointment.id}"
                                        Log.d("deleteMentor", studentReference)
                                        database.reference.child(studentReference).removeValue()
                                            .addOnSuccessListener {
                                            }.addOnFailureListener {
                                                if (!isResumed) {
                                                    isResumed = true
                                                    continuation.resume(false)
                                                }
                                            }
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                if (!isResumed) {
                                    isResumed = true
                                    continuation.resume(false)
                                }
                            }

                        })
                    typeReference.child(mentorPath).removeValue()
                        .addOnSuccessListener { deletedMentor ->
                            if (!isResumed) {
                                isResumed = true
                                continuation.resume(true)
                            }
                        }
                        .addOnFailureListener { error ->
                            if (!isResumed) {
                                isResumed = true
                                continuation.resume(false)
                            }
                        }
                }
            }
        }
    }


    suspend fun updateMentor(
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
}