package com.nitc.projectsgc.composable.util

import android.content.Context
import androidx.activity.ComponentActivity

class StorageAccess(
    var activity:ComponentActivity
) {


    fun saveUsername(
        userType: Int,
        username: String,
        password: String,
    ):Boolean{
        var saved = false
        var sharedPreferences = activity.getSharedPreferences(
            "sgcLogin",
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences?.edit()
        if (editor != null) {
            editor.putString("password", password)
            editor.putInt("userType", userType)
            editor.putString("username", username)
            editor.apply()
            saved = true
        }
        return saved
    }
}