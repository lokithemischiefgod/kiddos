package com.example.kiddos.data

data class ProductPurchased(
    val email: String = "",
    val address: String = "",
    val productName: String = "",
    val phoneNumber: String = "",
    val quantity: Int = 0,
    val totalPrice: Int =  0,
    val status: String = "",
    val size: String = "",
    val image: String = "",
    val date: String = ""
)
