package com.nitc.projectsgc.composable.util.storage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    private val storageManager: StorageManager
) : ViewModel(){


    fun storeUserData(userType: Int, username: String,password:String) {
        storageManager.saveUsername(userType, username,password)
    }

    fun deleteData():Boolean{
        return storageManager.deleteData()
    }

    fun getUserType():Int{
        return storageManager.getUserType()
    }
}