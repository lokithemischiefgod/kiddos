package com.example.kiddos.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.kiddos.R
import com.example.kiddos.ViewModel.HistoryViewModel
import com.example.kiddos.ViewModel.HomeViewModel
import com.example.kiddos.ViewModel.LogInViewModel
import com.example.kiddos.ViewModel.ProductDetailsViewModel
import com.google.type.DateTime
import java.time.LocalDateTime

@Composable
fun History(logInViewModel: LogInViewModel = viewModel(), historyViewModel: HistoryViewModel = viewModel(), navController: NavHostController, homeViewModel: HomeViewModel = viewModel(), productDetailsViewModel: ProductDetailsViewModel = viewModel()){
    val purchasedData by historyViewModel.fetchPurchasedProduct.collectAsState()


    LaunchedEffect(Unit){
        historyViewModel.getPurchasedProduct(logInViewModel.fetchData.value.email)
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color(0XFFae1c2c))) {
        Column {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .shadow(elevation = 7.dp)
                .background(color = Color(0XFFae1c2c)), contentAlignment = Alignment.BottomStart){
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, bottom = 20.dp), verticalAlignment = Alignment.CenterVertically){
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "History", color = Color(0XFFffc66d), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)) {
                LazyColumn(){
                    items(purchasedData){
                        item -> purchasedBox(
                        name = item.productName,
                        quantity = item.quantity,
                        date = item.date,
                        status = item.status,
                        image = item.image,
                        total = item.totalPrice,
                            onRepurchasedClicked = { homeViewModel.getProductDetails(item.productName)
                            navController.navigate("productDetails")
                            productDetailsViewModel.navigateFrom.value = "history"
                            }
                    )
                        Spacer(modifier = Modifier.height(10.dp))
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
                        Icon(imageVector = Icons.Filled.ShoppingBag, contentDescription = "", tint = Color.White, modifier = Modifier.size(28.dp).clickable {
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
    }}

@Composable
fun purchasedBox(name: String, quantity: Int, date: String, status: String, image: String, total: Int, onRepurchasedClicked: () -> Unit){
    val homeViewModel: HomeViewModel = viewModel()
    Box(
        Modifier
            .fillMaxWidth()
            .background(color = Color(0XFFae1c2c), shape = RoundedCornerShape(20.dp))
            .border(
                width = 2.dp,
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            )) {
        Column(Modifier.padding(15.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Filled.Checkroom, contentDescription = "", tint = Color.White, modifier = Modifier.size(30.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = date, fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
                }
                if(status == "On Progress"){
                    Box(
                        Modifier
                            .background(
                                color = Color.Gray,
                                shape = RoundedCornerShape(10.dp)
                            ), contentAlignment = Alignment.Center) {
                        Text(text = status, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp))
                    }
                } else {
                    Box(
                        Modifier
                            .background(
                                color = Color(0XFFd1fada),
                                shape = RoundedCornerShape(10.dp)
                            ), contentAlignment = Alignment.Center) {
                        Text(text = status, fontWeight = FontWeight.Bold, color = Color(0XFF01a85b), modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp))
                    }
                }


            }
            Spacer(modifier = Modifier.height(15.dp))
            Divider(thickness = 2.dp, color = Color.White, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(70.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(
                            color = Color.Gray,
                            shape = RoundedCornerShape(10.dp)
                        )) {
                    Image(painter = rememberImagePainter(data = image), contentDescription = "", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                }
                Spacer(modifier = Modifier.width(7.dp))
                Column {
                    Text(text = name, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = quantity.toString(), color = Color.White, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Column(Modifier.padding(5.dp)) {
                Text(text = "Total", color = Color.White, fontSize = 16.sp,)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row {
                        Text(text = "Rp. ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = homeViewModel.convertPriceToRupiah(total), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                    Box(
                        Modifier
                            .background(
                                color = Color(0XFFd1fada),
                                shape = RoundedCornerShape(10.dp)
                            ).clickable { onRepurchasedClicked() }, contentAlignment = Alignment.Center) {
                        Text(text = "Repurchase?", fontWeight = FontWeight.Bold, color = Color(0XFF01a85b), modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp))
                    }
                }
            }
        }
    }
}