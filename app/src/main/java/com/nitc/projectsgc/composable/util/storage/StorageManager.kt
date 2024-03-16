package com.nitc.projectsgc.composable.util.storage

interface StorageManager {
    fun getUserType():Int

    fun saveUsername(
        userType: Int,
        username: String,
        password: String,
    ):Boolean

    fun deleteData():Boolean
}