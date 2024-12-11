package com.example.kiddos.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.kiddos.R
import com.example.kiddos.ViewModel.LogInViewModel

@Composable
fun Profile(navController: NavHostController, logInViewModel: LogInViewModel = viewModel()){
    val profile by logInViewModel.fetchData.collectAsState()
    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color.Black)) {
        Column(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .shadow(elevation = 7.dp)
                    .background(color = Color(0XFFae1c2c)), contentAlignment = Alignment.BottomStart) {
                Column {
                    Spacer(modifier = Modifier.height(35.dp))
                    Row(
                        Modifier
                            .fillMaxSize()
                            .padding(start = 20.dp, end = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Profile", color = Color(0XFFffc66d), fontWeight = FontWeight.W900, fontSize = 30.sp)
                        Icon(imageVector = Icons.Filled.Logout, contentDescription = "", tint = Color.White, modifier = Modifier.size(30.dp).clickable { logInViewModel.logout() },)
                    }
                }
               

            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)) {
                Image(painter = painterResource(id = R.drawable.bg), contentDescription = "", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            }
        }
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(635.dp)
                    .background(color = Color.Transparent)) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(565.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            color = Color(0XFFae1c2c),
                            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                        )) {
                    Column(Modifier.padding(top = 80.dp, start = 30.dp, end = 30.dp)) {
                        Text(text = "Name", color = Color(0XFFffc66d), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = profile.UserName, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(thickness = 2.dp, color = Color.Black, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = "Email", color = Color(0XFFffc66d), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = profile.email, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(thickness = 2.dp, color = Color.Black, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = "Address", color = Color(0XFFffc66d), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = profile.address, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(thickness = 2.dp, color = Color.Black, modifier = Modifier.fillMaxWidth())
                    }
                }
                Row(
                    Modifier
                        .fillMaxSize()
                        .align(Alignment.TopStart)) {
                    Spacer(modifier = Modifier.width(30.dp))
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .shadow(elevation = 7.dp, shape = RoundedCornerShape(70.dp))
                            .background(
                                color = Color(0XFFae1c2c),
                                shape = RoundedCornerShape(70.dp)
                            ), contentAlignment = Alignment.Center
                    ){
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .shadow(elevation = 7.dp, shape = RoundedCornerShape(70.dp))
                                .background(color = Color.Yellow, shape = RoundedCornerShape(70.dp))
                        ){
                            Image(painter = painterResource(id = R.drawable.profile), contentDescription = "", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                        }
                    }
                }


            }
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp), verticalArrangement = Arrangement.Bottom) {
            Box(
                Modifier
                    .width(240.dp)
                    .height(70.dp)
                    .shadow(elevation = 7.dp, shape = RoundedCornerShape(40.dp))
                    .background(
                        color = Color.White.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(40.dp)
                    )
                    .align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Box(
                        Modifier
                            .size(60.dp)
                            .background(color = Color.Black, shape = RoundedCornerShape(30.dp)), contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Outlined.Home, contentDescription = "", tint = Color.White, modifier = Modifier
                            .size(35.dp)
                            .clickable {
                                navController.navigate("home")
                            })
                    }
                    Box(
                        Modifier
                            .size(60.dp)
                            .background(color = Color.Black, shape = RoundedCornerShape(30.dp)), contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Filled.ShoppingBag, contentDescription = "", tint = Color.White, modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                navController.navigate("history")
                            })
                    }
                    Box(
                        Modifier
                            .size(60.dp)
                            .background(color = Color.Black, shape = RoundedCornerShape(30.dp))
                            .clickable { navController.navigate("profile") }, contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Outlined.Person, contentDescription = "", tint = Color.White, modifier = Modifier.size(30.dp))
                    }
                }
            }
        }
    }

}