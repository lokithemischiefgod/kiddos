package com.example.kiddos.ViewModel

import androidx.lifecycle.ViewModel
import com.example.kiddos.data.ProductPurchased
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HistoryViewModel : ViewModel() {
    var _fetchPurchasedProduct = MutableStateFlow<List<ProductPurchased>>(emptyList())
    val fetchPurchasedProduct: StateFlow<List<ProductPurchased>> get() = _fetchPurchasedProduct
    val db = FirebaseFirestore.getInstance()

    fun getPurchasedProduct(email: String){
        val docRef = db.collection("purchased").whereEqualTo("email", email)
        docRef.addSnapshotListener{
            snapshot, e ->
            if(e != null){
                return@addSnapshotListener
            }

            if(snapshot != null && !snapshot.isEmpty){
                val allPurchased = snapshot.documents.mapNotNull {
                    documentSnapshot -> documentSnapshot.toObject(ProductPurchased::class.java)
                }
                _fetchPurchasedProduct.value = allPurchased
            } else {
                println("No documents found")
            }
        }
    }
}