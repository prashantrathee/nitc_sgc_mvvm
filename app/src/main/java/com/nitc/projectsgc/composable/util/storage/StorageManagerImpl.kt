package com.nitc.projectsgc.composable.util.storage

import android.content.Context
import com.nitc.projectsgc.composable.login.LoginCredential
import javax.inject.Inject


class StorageManagerImpl @Inject constructor(
    private val context: Context
) : StorageManager {

    override
    fun getUserType():Int{
        var sharedPreferences = context.getSharedPreferences(
            "sgcLogin",
            Context.MODE_PRIVATE
        )
        if(sharedPreferences != null){
            val userType = sharedPreferences.getInt("userType",-1)
            return userType
        }else return -1
    }

    override fun saveUsername(
        userType: Int,
        username: String,
        password: String,
    ):Boolean{
        var saved = false
        var sharedPreferences = context.getSharedPreferences(
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

    override fun getUserInfo(): LoginCredential {
        var sharedPreferences = context.getSharedPreferences(
            "sgcLogin",
            Context.MODE_PRIVATE
        )
        if(sharedPreferences != null){
            val userType = sharedPreferences.getInt("userType",-1)
            val username = sharedPreferences.getString("username","")
            val password = sharedPreferences.getString("password","")
            return LoginCredential(userType,username!!,password!!)
        }else return LoginCredential()
    }
    override fun deleteData(): Boolean {
        val sharedPreferences = context.getSharedPreferences(
            "sgcLogin",
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        return if(editor != null){
            editor.remove("password")
            editor.remove("userType")
            editor.remove("username")
            editor.apply()
            true
        }else false
    }
}