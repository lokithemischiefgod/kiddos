package com.example.kiddos.data

data class Product(
    val name: String = "",
    val price: Int = 0,
    val size: Map<String, Int> = emptyMap(),
    val totalSelled: Int = 0,
    val image: List<String> = emptyList()
)
