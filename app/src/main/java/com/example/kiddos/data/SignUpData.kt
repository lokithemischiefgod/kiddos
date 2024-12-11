package com.example.kiddos.data

data class SignUpData(
    val email: String = "",
    val userName: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String = "",
    val userNameError: String = "",
    val passwordError: String = "",
    val confirmPasswordError: String = "",
)
