package com.example.kiddos.ViewModel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.kiddos.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class LogInViewModel(context: Context) : ViewModel(){
    private var _fetchData = MutableStateFlow(User())
    val fetchData: StateFlow<User> get() = _fetchData
    val authDb  = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    private var _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading
    private var _isLoggedin = MutableStateFlow(false)
    val isLoggedin: StateFlow<Boolean> get() = _isLoggedin

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object{
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_EMAIL = "email"
    }

    init {
        _isLoggedin.value = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
        val email = sharedPreferences.getString(KEY_EMAIL, null)
        if (email != null) {
            getUserDataByEmail(email)
        }
    }

    suspend fun logIn(email: String, password: String) {

        try {
            _isLoading.value = true
            val authResult = authDb.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid
            if (uid != null) {
                db.collection("user").document(uid).addSnapshotListener { docSnapshot, e ->
                    if (e != null) {
                        println("Failed to listen to document: ${e.message}")
                        _isLoggedin.value = false
                        return@addSnapshotListener
                    }

                    if (docSnapshot != null && docSnapshot.exists()) {
                        val profile = docSnapshot.toObject(User::class.java) ?: User()
                        _fetchData.value = profile
                        _isLoggedin.value = true

                        sharedPreferences.edit().apply {
                            putBoolean(KEY_IS_LOGGED_IN, true)
                            putString(KEY_EMAIL, profile.email)
                            apply()
                        }

                    } else {
                        println("Document does not exist")
                        _isLoggedin.value = false
                        _isLoading.value = false
                    }
                }
            }
        } catch (e: Exception) {
            _isLoggedin.value = false
            _isLoading.value = false
            println("Failed to login: ${e.message}")
        }
    }

    private fun getUserDataByEmail(email: String){
        db.collection("user").whereEqualTo("email", email).addSnapshotListener { snapshot, e ->
            if (e != null){
                println("Error fetching user data: ${e.message}")
                _isLoggedin.value = false
                return@addSnapshotListener
            }

            if(!snapshot!!.isEmpty && snapshot != null){
                val user = snapshot.documents[0].toObject(User::class.java)
                if(user != null){
                    _fetchData.value = user
                    _isLoggedin.value = true
                } else {
                    println("No user found with this email")
                    _isLoggedin.value = false
                }
            }
        }
    }

    fun setBackToFalse(){
        _isLoading.value = false
    }

    fun logout() {
        authDb.signOut()
        _isLoggedin.value = false
        sharedPreferences.edit().clear().apply()
    }
}