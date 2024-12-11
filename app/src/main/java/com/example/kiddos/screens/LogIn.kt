package com.example.kiddos.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kiddos.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.kiddos.ViewModel.LogInViewModel
import com.example.kiddos.ViewModel.SignUpViewModel
import kotlinx.coroutines.launch

@Composable
fun LogIn(signUpViewModel: SignUpViewModel = viewModel(), logInViewModel: LogInViewModel = viewModel(), navController: NavHostController){
    val coroutine = rememberCoroutineScope()
    val status by logInViewModel.isLoggedin.collectAsState()
    val loading by logInViewModel.isLoading.collectAsState()

    LaunchedEffect(status){
        if(status){
            navController.navigate("home")
        } else {
            println("Failed")
        }
    }

    LaunchedEffect(Unit){
        signUpViewModel.setBackToFalse()
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color(0XFFae1c2c))) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Column(Modifier.padding(top = 200.dp)) {
                Box(
                    Modifier
                        .size(200.dp)) {
                    Image(painter = painterResource(id = R.drawable.logo), contentDescription = "", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Fit)
                }

            }
        }
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                Modifier
                    .padding(horizontal = 30.dp)
                    .align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(300.dp))
                TextFieldBox(value = signUpViewModel.signUpState.value.email, width = 500, desc = "Email", onValueChange = { signUpViewModel.email(it) })
                Spacer(modifier = Modifier.height(10.dp))
                TextFieldPasswordBox(value = signUpViewModel.signUpState.value.password, width = 260, desc = "Password", onValueChange = { signUpViewModel.Password(it) })
                Spacer(modifier = Modifier.height(20.dp))
                if(loading){
                    Box(
                        Modifier
                            .width(70.dp)
                            .height(40.dp)
                            .shadow(elevation = 7.dp, shape = RoundedCornerShape(20.dp))
                            .background(color = Color.White, shape = RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(strokeWidth = 4.dp,  color = Color(0XFFffc66d), modifier = Modifier.size(25.dp))
                    }
                } else {
                    Box(
                        Modifier
                            .width(70.dp)
                            .clickable {
                                if (signUpViewModel.signUpState.value.email.isNotEmpty() && signUpViewModel.signUpState.value.password.isNotEmpty()) {
                                    coroutine.launch {
                                        logInViewModel.logIn(
                                            email = signUpViewModel.signUpState.value.email,
                                            password = signUpViewModel.signUpState.value.password
                                        )
                                    }
                                } else {
                                    println("Please fill")
                                }
                            }
                            .height(40.dp)
                            .shadow(elevation = 7.dp, shape = RoundedCornerShape(20.dp))
                            .background(color = Color.White, shape = RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center) {
                        Text(
                            text = "Log In",
                            color = Color(0XFFae1c2c),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Row(Modifier.padding(bottom = 30.dp)) {
                Text(text = "Do not have an account? ", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Text(text = "Sign Up", color = Color(0XFFffc66d), fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.clickable {
                    navController.navigate("signUp")
                })
            }
        }
    }
}
