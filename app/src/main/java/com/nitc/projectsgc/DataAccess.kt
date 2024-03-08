package com.nitc.projectsgc

import android.content.Context
import androidx.fragment.app.Fragment

class DataAccess(
    var context: Context,
    var parentFragment: Fragment,
    var sharedViewModel: SharedViewModel
) {
    fun saveUsername(
        password: String,
        userType: Int,
        mentorType: String,
        email:String,
        username: String,
        institutionUsername:String
    ):Boolean{
        var saved = false
        var sharedPreferences = parentFragment.activity?.getSharedPreferences(
            "sgcLogin",
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences?.edit()
        if (editor != null) {
            editor.putBoolean("loggedIn", true)
            editor.putString("password", password)
            editor.putInt("userType", userType)
            editor.putString("mentorType", mentorType)
            editor.putString("email", email)
            editor.putString("institutionUsername", institutionUsername)
            editor.putString("username", username)
            editor.apply()
            saved = true
        }
        return saved
    }


    fun deleteData():Boolean{

        var sharedPreferences = parentFragment.activity?.getSharedPreferences("sgcLogin",Context.MODE_PRIVATE)
        if(sharedPreferences == null) return false
        val editor = sharedPreferences.edit()
        if (editor != null) {
            editor.remove("password")
            editor.remove("mentorType")
            editor.remove("userType")
            editor.remove("username")
            editor.remove("emailSuffix")
            editor.remove("email")
            editor.putBoolean("loggedIn",false)
            editor.apply()
            sharedViewModel.rescheduling = false
            sharedViewModel.mentorTypeForProfile = "NA"
            sharedViewModel.viewAppointmentStudentID = "NA"
            sharedViewModel.pastRecordStudentID = "NA"
            sharedViewModel.userType = 1
            sharedViewModel.currentUserID = "NA"
            return true
        }else return false
    }

}