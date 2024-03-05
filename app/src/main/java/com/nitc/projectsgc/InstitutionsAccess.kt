package com.nitc.projectsgc

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nitc.projectsgc.models.Institution
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class InstitutionsAccess(
    var context: Context,
    var sharedViewModel: SharedViewModel
) {

    suspend fun getInstitution(institutionUsername:String): Institution?{
        return suspendCoroutine {continuation ->

            val database = FirebaseDatabase.getInstance()
            val reference = database.reference.child(institutionUsername)
            reference.addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot!= null && snapshot.exists()){
                        val institution = snapshot.getValue(Institution::class.java)
                        continuation.resume(institution)
                    }else{
                        continuation.resume(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("getInstitution","Database error :$error")
                    Toast.makeText(context,"Error in getting the information of institution",Toast.LENGTH_SHORT).show()
                    continuation.resume(null)
                }
            })

        }
    }

    suspend fun getInstitutions(): ArrayList<Institution>? {
        return suspendCoroutine {continuation ->
            val database = FirebaseDatabase.getInstance()
            val reference = database.reference
            val institutions = arrayListOf<Institution>()
            reference.addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(child in snapshot.children){
                        institutions.add(child.getValue(Institution::class.java)!!)
                    }
                    continuation.resume(institutions)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context,"Error in accessing database",Toast.LENGTH_SHORT).show()
                    Log.d("getInstitutions","Database error : $error")
                    continuation.resume(null)
                }
            })

        }
    }
    suspend fun getInstitutionNames():ArrayList<String>?{
        return suspendCoroutine { continuation ->
            var database = FirebaseDatabase.getInstance()
            var reference = database.reference
            var names = arrayListOf<String>()
            var isResumed = false
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (child in snapshot.children) {
                            if(child.exists() && child.key != null) names.add(child.key!!.replace('_', ' '))
                        }
                        if (!isResumed) {
                            isResumed = true
                            continuation.resume(names)
                        }
                    }else{
                        Toast.makeText(context,"No Institutions registered yet",Toast.LENGTH_SHORT).show()
                        if (!isResumed) {
                            isResumed = true
                            continuation.resume(null)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context,"Error in accessing database. Try again",Toast.LENGTH_SHORT).show()
                    Log.d("getInstitutionNames","Database error : $error")
                    if (!isResumed) {
                        isResumed = true
                        continuation.resume(null)
                    }
                }
            })
        }
    }

    suspend fun deleteInstitution(
        username:String
    ):Boolean{
        return suspendCoroutine { continuation ->
            val database = FirebaseDatabase.getInstance()
            val reference = database.reference.child(username)
            var isResumed = false
            reference.removeValue().addOnSuccessListener {
                Toast.makeText(context,"Institution Deleted",Toast.LENGTH_SHORT).show()
                if(!isResumed){
                    isResumed = true
                    continuation.resume(true)
                }
            }
                .addOnFailureListener { exc->
                    Log.d("deleteInstitution","Database error")
                    Toast.makeText(context,"Error in deleting institution",Toast.LENGTH_SHORT).show()
                    if(!isResumed){
                        isResumed = true
                        continuation.resume(false)
                    }
                }
        }
    }

    suspend fun addInstitution(
        institution: Institution
    ):Boolean{
        return suspendCoroutine { continuation ->
            val database = FirebaseDatabase.getInstance()
            val reference = database.reference
            reference.addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.hasChild(institution.username!!)) {
                        Toast.makeText(context,"Institution already exists with the same name",Toast.LENGTH_SHORT).show()
                        continuation.resume(
                            false
                        )
                    }else{
                        reference.child(institution.username!!).setValue(institution).addOnCompleteListener { institutionAdded->
                            if(institutionAdded.isSuccessful){
                                continuation.resume(true)
                            }else continuation.resume(false)
                        }.addOnFailureListener { exc->
                            Log.d("addInstitution","Exception : $exc")
                            continuation.resume(false)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context,"Error in accessing database",Toast.LENGTH_SHORT).show()
                    Log.d("addInstitution","Error : $error")
                    continuation.resume(false)
                }
            })
        }
    }


}