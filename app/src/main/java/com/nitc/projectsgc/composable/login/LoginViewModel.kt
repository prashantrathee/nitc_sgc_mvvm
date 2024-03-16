package com.nitc.projectsgc.composable.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.right
import com.nitc.projectsgc.composable.util.PathUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepo: LoginRepo
) : ViewModel() {


    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _userType = MutableStateFlow(0)
    val userType: StateFlow<Int> = _userType

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _loginCredential = MutableStateFlow(LoginCredential())
    val loginCredential: StateFlow<LoginCredential> = _loginCredential

    private val _isAuthenticated = MutableStateFlow<Either<String, Boolean>?>(null)
    val isAuthenticated: StateFlow<Either<String, Boolean>?> = _isAuthenticated

    fun setUsername(username: String) {
        _username.value = username
    }

    fun setUserType(userType: Int) {
        _userType.value = userType
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun authenticate() {
        viewModelScope.launch {
            _isAuthenticated.value = loginRepo.login(username.value, userType.value, password.value)
            _loginCredential.value = LoginCredential(
                userType.value,
                PathUtils.getUsernameFromEmailSure(userType.value,username.value),
                password.value
            )
            Log.d("loginSuccess","Authenticated value updated")
        }
    }

}