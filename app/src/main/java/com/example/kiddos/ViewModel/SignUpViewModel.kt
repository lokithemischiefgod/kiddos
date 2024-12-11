package com.example.kiddos.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.kiddos.data.SignUpData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class SignUpViewModel: ViewModel() {
    var signUpState = mutableStateOf(SignUpData())
    private set
    val firebase = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    var allFieldvalid = mutableStateOf(false)
        private set
    var _isRegisterSuccess = MutableStateFlow(false)
    val isRegisterSuccess: StateFlow<Boolean> get() = _isRegisterSuccess
    var _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading


    suspend fun signUp(name: String, email: String, password: String) {
        try {
            _isLoading.value = true
            val snapShot = firebase.collection("user").whereEqualTo("email", email).limit(1).get().await()
            if (snapShot.isEmpty) {
                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val uid = authResult.user?.uid

                if (uid != null) {
                    val data = hashMapOf(
                        "UserName" to name,
                        "email" to email,
                        "address" to "",
                        "wishList" to emptyList<String>()
                    )
                    firebase.collection("user").document(uid).set(data).await()
                    _isRegisterSuccess.value = true
                } else {
                    println("UID null.")
                    _isLoading.value = false
                }
            } else {
                println("Email already exists.")
                _isLoading.value = false
            }
        } catch (e: Exception) {
            println("Error: ${e.localizedMessage}")
            _isLoading.value = false
        }
    }

    fun email(email: String){
        signUpState.value = signUpState.value.copy(email = email)
        validateEmail()
    }

    fun userName(userName: String){
        signUpState.value = signUpState.value.copy(userName = userName)
        validateUserName()
    }

    fun Password(password: String){
        signUpState.value = signUpState.value.copy(password = password)
        validatePassword()
    }

    fun ConfirmPassword(confirmPassword: String){
        signUpState.value = signUpState.value.copy(confirmPassword = confirmPassword)
        validateConfirmPassword()
    }

    fun validateEmail(){
        val email = signUpState.value.email.trim()
        signUpState.value = signUpState.value.copy(
            emailError = if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                "Invalid email error"
            } else {
                ""
            }
        )
    }

    fun setBackToFalse(){
        _isLoading.value = false
        _isRegisterSuccess.value = false
    }

    fun validateUserName(){
        val name = signUpState.value.userName.trim()
        signUpState.value = signUpState.value.copy(
            userNameError = if (name.isEmpty()){
                "Name can't be empty"
            } else {
                ""
            }
        )
    }

    fun validatePassword(){
        val password = signUpState.value.password
        signUpState.value = signUpState.value.copy(
            passwordError = if (password.length < 8){
                "Password must be at least 8 characters"
            } else {
                ""
            }
        )
    }

    fun validateConfirmPassword() {
        val confirmPassword = signUpState.value.confirmPassword
        signUpState.value = signUpState.value.copy(confirmPasswordError = if (confirmPassword != signUpState.value.password){
            "password does not match"
        } else {
            ""
        })
    }

    fun validateAllFields(): Boolean {
        validateUserName()
        validateEmail()
        validatePassword()
        validateConfirmPassword()


        val isUserNameValid= signUpState.value.userNameError.isEmpty()
        val isEmailValid = signUpState.value.emailError.isEmpty()
        val isPasswordValid = signUpState.value.passwordError.isEmpty()
        val isConfirmPasswordValid = signUpState.value.confirmPasswordError.isEmpty()

        if (isUserNameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid){
            allFieldvalid.value = true
        }
        return isUserNameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid
    }

}