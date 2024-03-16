package com.nitc.projectsgc.composable.login

data class LoginCredential(
    val userType:Int = -1,
    val username:String = "",
    val password:String = ""
)