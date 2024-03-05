package com.nitc.projectsgc.composables.viewmodels

import androidx.lifecycle.ViewModel
import com.nitc.projectsgc.Login.access.LoginAccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginAccess: LoginAccess
) : ViewModel(){
}