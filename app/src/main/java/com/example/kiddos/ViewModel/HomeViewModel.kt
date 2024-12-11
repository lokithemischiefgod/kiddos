package com.example.kiddos.ViewModel

import androidx.lifecycle.ViewModel
import com.example.kiddos.data.Product
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.text.NumberFormat
import java.util.Locale

class HomeViewModel : ViewModel() {
    private var _fetchProduct = MutableStateFlow<List<Product>>(emptyList())
    val fetchProduct: StateFlow<List<Product>> get() = _fetchProduct
    private var _fetchProductDetails = MutableStateFlow<Product>(Product())
    val fetchProductDetails: StateFlow<Product> get() = _fetchProductDetails
    var tempName = ""
    val db = FirebaseFirestore.getInstance()


    fun getProductFromDb() {
        db.collection("products")
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    println("Failed Fetching Products: ${exception.message}")
                    _fetchProduct.value = emptyList()
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    val productLists = querySnapshot.documents.mapNotNull { documentSnapshot ->
                        documentSnapshot.toObject(Product::class.java)
                    }
                    _fetchProduct.value = productLists
                } else {
                    _fetchProduct.value = emptyList()
                }
            }
    }

    fun getProductDetails(name: String) {
        db.collection("products").whereEqualTo("name", name)
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    println("Failed Fetching Products: ${exception.message}")
                    _fetchProductDetails.value = Product()
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    val productDetails = querySnapshot.documents.mapNotNull { documentSnapshot ->
                        documentSnapshot.toObject(Product::class.java)
                    }.firstOrNull()
                    _fetchProductDetails.value = productDetails ?: Product()
                } else {
                    _fetchProductDetails.value = Product()
                }
            }
    }

    fun addProductToWishList(email: String, product: String) {
        val userRef = db.collection("user").whereEqualTo("email", email)

        userRef.get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    println("No user found with email: $email")
                    return@addOnSuccessListener
                }

                for (document in documents) {
                    val docRef = document.reference
                    docRef.update("wishList", FieldValue.arrayUnion(product))
                        .addOnSuccessListener {
                            println("Product '$product' successfully added to wishlist for user: $email")
                        }
                        .addOnFailureListener { e ->
                            println("Error updating wishlist: $e")
                        }
                }
            }
            .addOnFailureListener { e ->
                println("Error fetching user: $e")
            }
    }

    fun deleteProductFromWishList(email: String, product: String) {
        println("test func")
        val userRef = db.collection("user").whereEqualTo("email", email)

        userRef.get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    println("No user found with email: $email")
                    return@addOnSuccessListener
                }

                for (document in documents) {
                    val docRef = document.reference
                    docRef.update("wishList", FieldValue.arrayRemove(product))
                        .addOnSuccessListener {
                            println("Product '$product' successfully deleted wishlist from user: $email")
                        }
                        .addOnFailureListener { e ->
                            println("Error deletingwishlist: $e")
                        }
                }
            }
            .addOnFailureListener { e ->
                println("Error fetching user: $e")
            }
    }




    fun getTopSellingProduct(): Product? {
        return _fetchProduct.value.maxByOrNull { it.totalSelled }
    }

    fun convertPriceToRupiah(price: Int): String{
        val formatter = NumberFormat.getNumberInstance(Locale("id", "ID"))
        return formatter.format(price)
    }

}