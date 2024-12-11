package com.example.kiddos.data

data class User(
    val UserName: String = "",
    val address: String = "",
    val email: String = "",
    val wishList: List<String> = emptyList()
)

