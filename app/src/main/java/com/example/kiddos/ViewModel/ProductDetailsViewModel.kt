package com.example.kiddos.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime

class ProductDetailsViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> get() = _isSuccess
    val navigateFrom = mutableStateOf("home")
    var _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    suspend fun buy(
        email: String,
        productName: String,
        quantity: Int,
        size: String,
        address: String,
        phoneNumber: String,
        status: String,
        image: String,
        price: Int,
        date: String
    ) {
        val productRef = db.collection("products").whereEqualTo("name", productName).get().await()
        _isLoading.value = true

        if (productRef.isEmpty) {
            _isSuccess.value = false
            _isLoading.value = false
            return
        }

        val productDoc = productRef.documents[0]
        val availableStock = productDoc.get("size")?.let { (it as Map<String, Any>)[size] as? Long }?.toInt() ?: 0

        if (availableStock < quantity) {
            _isSuccess.value = false
            _isLoading.value = false
            return
        }


        db.runTransaction { transaction ->

            val currentStock = productDoc.get("size")?.let { (it as Map<String, Any>)[size] as? Long }?.toInt() ?: 0

            if (currentStock < quantity) {
                throw Exception("Not enough stock available")
            }


            val updatedStock = currentStock - quantity
            transaction.update(productDoc.reference, "size.$size", updatedStock)


            val purchaseData = hashMapOf(
                "email" to email,
                "productName" to productName,
                "quantity" to quantity,
                "size" to size,
                "address" to address,
                "phoneNumber" to phoneNumber,
                "status" to status,
                "image" to image,
                "totalPrice" to price,
                "date" to date
            )
            transaction.set(db.collection("purchased").document(), purchaseData)
        }.addOnSuccessListener {
            _isSuccess.value = true
            _isLoading.value = false
            println("Purchase successful")
        }.addOnFailureListener {
            _isSuccess.value = false
            _isLoading.value = false
            println("Purchase failed: ${it.message}")
        }
    }

    fun setStatusBackToFalse(){
        _isSuccess.value = false
        _isLoading.value = false
    }
}