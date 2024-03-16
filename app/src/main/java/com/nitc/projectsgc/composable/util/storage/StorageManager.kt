package com.nitc.projectsgc.composable.util.storage

import com.nitc.projectsgc.composable.login.LoginCredential

interface StorageManager {
    fun getUserType():Int

    fun saveUsername(
        userType: Int,
        username: String,
        password: String,
    ):Boolean

    fun deleteData():Boolean
    fun getUserInfo(): LoginCredential
}