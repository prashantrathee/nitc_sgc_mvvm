package com.nitc.projectsgc.composable.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel() : ViewModel() {


    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _userType = MutableStateFlow(0)
    val userType: StateFlow<Int> = _userType

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    fun setUsername(username: String) {
        _username.value = username
    }

    fun setUserType(userType: Int) {
        _userType.value = userType
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    private val loginRepo = LoginRepo()
    fun authenticate() {
        // Assuming you have a login repository
        viewModelScope.launch {
            val isAuthenticated = loginRepo.login(username.value, userType.value, password.value,"health")
            _isAuthenticated.value = isAuthenticated
        }
    }

}