package com.nitc.projectsgc.composable.admin.repo

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nitc.projectsgc.models.Appointment
import com.nitc.projectsgc.models.Mentor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MentorsRepo(
    var context: Context
) {
    suspend fun getMentors():ArrayList<Mentor>?{
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
                    Toast.makeText(context, "Error : $error", Toast.LENGTH_LONG).show()
                    continuation.resume(null)
                }

            })
        }
    }

    suspend fun getMentor(mentorType:String,mentorID:String): Mentor?{
        return suspendCoroutine { continuation ->
            var isResumed = false
            var database: FirebaseDatabase = FirebaseDatabase.getInstance()
            var reference: DatabaseReference =
                database.reference.child("types/${mentorType}/${mentorID}")

            reference.addListenerForSingleValueEvent(object : ValueEventListener {


                override fun onDataChange(snapshot: DataSnapshot) {

                    if(!isResumed) {
                        continuation.resume(snapshot.getValue(Mentor::class.java))
                        isResumed = true
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error occurred in getting Mentor", Toast.LENGTH_SHORT).show()
                    Log.d("getMentor","Database Error : $error")
                    if(!isResumed){
                        continuation.resume(null)
                        isResumed = true
                    }
                }
            })
        }
    }
    suspend fun getMentorNames(mentorType:String): ArrayList<Mentor>? {
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
                    if(!isResumed){
                        continuation.resume(mentors)
                        isResumed = true
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error : $error", Toast.LENGTH_SHORT).show()

                    if(!isResumed){
                        continuation.resume(null)
                        isResumed = true
                    }
                }

            })
        }
    }
    suspend fun getMentorTypes():ArrayList<String>?{
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
                    if(!isResumed) {
                        continuation.resume(mentorTypes)
                        isResumed = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error : $error", Toast.LENGTH_LONG).show()
                    if(!isResumed) {
                        continuation.resume(null)
                        isResumed = true
                    }
                }

            })
        }
    }
    suspend fun deleteMentor(userName: String):Boolean{
        return suspendCoroutine {continuation->
            var database: FirebaseDatabase = FirebaseDatabase.getInstance()
            var typeReference: DatabaseReference = database.reference.child("types")
            val mentorType = userName.substring(userName.indexOf('_')+1,userName.indexOfLast { it == '_' })
            var mentorPath = "$mentorType/$userName"
            Log.d("deleteMentor", mentorPath)
            typeReference.child(mentorPath)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (ds in snapshot.child("appointments").children) {
                            for (timeSlot in ds.children) {
                                var appointment = timeSlot.getValue(Appointment::class.java)!!
                                var studentReference =
                                    "students/${appointment.studentID}/appointments/${appointment.id}"
                                Log.d("deleteMentor", studentReference)
                                database.reference.child(studentReference).removeValue()
                                    .addOnSuccessListener {

                                    }.addOnFailureListener {
                                        continuation.resume(false)
                                    }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "Error : $error", Toast.LENGTH_LONG).show()
                        continuation.resume(false)
                    }

                })
            typeReference.child(mentorPath).removeValue().addOnSuccessListener { deletedMentor ->
                continuation.resume(true)
            }
                .addOnFailureListener { error ->
                    Toast.makeText(context, "Error : $error", Toast.LENGTH_LONG).show()
                    continuation.resume(false)
                }
        }
    }
}