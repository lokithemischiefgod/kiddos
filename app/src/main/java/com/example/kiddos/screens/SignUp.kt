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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.kiddos.R
import com.example.kiddos.ViewModel.SignUpViewModel
import kotlinx.coroutines.launch

@Composable
fun SignUp(signUpViewModel: SignUpViewModel = viewModel(), navHostController: NavHostController){
    val coroutine = rememberCoroutineScope()
    val signUpState by signUpViewModel.isRegisterSuccess.collectAsState()
    val loading by signUpViewModel.isLoading.collectAsState()

    LaunchedEffect(signUpState){
        if (signUpState){
            navHostController.navigate("logIn")
        } else {
            println("Failed")
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color(0XFFae1c2c))) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Column(Modifier.padding(top = 100.dp)) {
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
                Spacer(modifier = Modifier.height(240.dp))
                TextFieldBox(value = signUpViewModel.signUpState.value.email, width = 500, desc = "Email", onValueChange = { signUpViewModel.email(it) })
                Spacer(modifier = Modifier.height(10.dp))
                TextFieldBox(value = signUpViewModel.signUpState.value.userName, width = 500, desc = "User Name", onValueChange = { signUpViewModel.userName(it) })
                Spacer(modifier = Modifier.height(10.dp))
                TextFieldPasswordBox(value = signUpViewModel.signUpState.value.password, width = 260, desc = "Password", onValueChange = { signUpViewModel.Password(it) })
                Spacer(modifier = Modifier.height(10.dp))
                TextFieldPasswordBox(value = signUpViewModel.signUpState.value.confirmPassword, width = 260, desc = "Confirm Password", onValueChange = { signUpViewModel.ConfirmPassword(it) })
                Spacer(modifier = Modifier.height(20.dp))
                if(loading){
                    Box(
                        Modifier
                            .width(70.dp)
                            .height(40.dp)
                            .shadow(elevation = 7.dp, shape = RoundedCornerShape(20.dp))
                            .background(color = Color.White, shape = RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0XFFffc66d), strokeWidth = 4.dp, modifier = Modifier.size(25.dp))
                    }
                } else {
                    Box(
                        Modifier
                            .width(70.dp)
                            .clickable {
                                coroutine.launch {
                                    if (signUpViewModel.validateAllFields()) {
                                        signUpViewModel.signUp(
                                            name = signUpViewModel.signUpState.value.userName,
                                            email = signUpViewModel.signUpState.value.email,
                                            password = signUpViewModel.signUpState.value.confirmPassword
                                        )
                                    }
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
                Text(text = "Already have an account? ", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Text(text = "Log In", color = Color(0XFFffc66d), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldBox(value: String, width: Int, desc: String, onValueChange: (String)-> Unit){
    Box(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(shape = RoundedCornerShape(40.dp))
            .border(
                width = 2.dp,
                color = Color.White,
                shape = RoundedCornerShape(40.dp)
            ), contentAlignment = Alignment.CenterStart) {
        TextField(value = value, onValueChange = { onValueChange(it)},
            modifier = Modifier
                .width(width.dp)
                .height(50.dp)
                .padding(horizontal = 20.dp), colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
            ), textStyle = TextStyle(color = Color.White, fontSize = 16.sp), placeholder = {
                Text(text = desc, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFielNumberdBox(value: String, width: Int, desc: String, onValueChange: (String)-> Unit){
    Box(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(shape = RoundedCornerShape(40.dp))
            .border(
                width = 2.dp,
                color = Color.White,
                shape = RoundedCornerShape(40.dp)
            ), contentAlignment = Alignment.CenterStart) {
        TextField(value = value, onValueChange = { onValueChange(it)},
            modifier = Modifier
                .width(width.dp)
                .height(50.dp)
                .padding(horizontal = 20.dp), colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
            ), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), textStyle = TextStyle(color = Color.White, fontSize = 16.sp), placeholder = {
                Text(text = desc, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldPasswordBox(
    value: String,
    width: Int,
    desc: String,
    onValueChange: (String) -> Unit
) {
    var isPasswordVisible = remember { mutableStateOf(false) }
    Box(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(shape = RoundedCornerShape(40.dp))
            .border(
                width = 2.dp,
                color = Color.White,
                shape = RoundedCornerShape(40.dp)
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = value,
                onValueChange = { onValueChange(it) },
                modifier = Modifier
                    .padding(start = 20.dp)
                    .width(width.dp)
                    .height(50.dp),
                visualTransformation = if (isPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                placeholder = {
                    Text(
                        text = desc,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
            Spacer(modifier = Modifier.width(5.dp))
            IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
                Icon(
                    imageVector = if (isPasswordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = "",
                    tint = Color(0XFF0A4635)
                )
            }
        }
    }
}
