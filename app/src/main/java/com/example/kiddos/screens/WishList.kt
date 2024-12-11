package com.example.kiddos.screens

import android.media.Image
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.kiddos.R
import com.example.kiddos.ViewModel.HomeViewModel
import com.example.kiddos.ViewModel.LogInViewModel
import com.example.kiddos.ViewModel.ProductDetailsViewModel

@Composable
fun WishList(navController: NavHostController, homeViewModel: HomeViewModel = viewModel(), logInViewModel: LogInViewModel = viewModel(), productDetailsViewModel: ProductDetailsViewModel = viewModel()) {
    val dataProduct by homeViewModel.fetchProduct.collectAsState()
    val dataUser by logInViewModel.fetchData.collectAsState()

    val wishList = dataProduct.filter { it.name in dataUser.wishList}

    BackHandler {
        navController.navigate("home")
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color(0XFFae1c2c))) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .shadow(elevation = 7.dp)
                    .background(color = Color(0XFFae1c2c)), contentAlignment = Alignment.BottomStart
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "",
                        tint = Color(0XFFffc66d),
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                navController.navigate("home")
                            }
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "WishList",
                        color = Color(0XFFffc66d),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column(Modifier.padding(20.dp)) {
                Box(Modifier.fillMaxHeight()) {
                    LazyColumn(){
                        items(wishList){
                            item -> WishListBox(name = item.name, price = item.price, image = item.image[0], onIconClicked = { homeViewModel.deleteProductFromWishList(product = item.name, email = dataUser.email) }, onBuyClicked = {
                            homeViewModel.getProductDetails(item.name)
                            navController.navigate("productDetails")
                            productDetailsViewModel.navigateFrom.value = "wishList"
                            })
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WishListBox(name: String, price: Int, image: String, onIconClicked: () -> Unit, onBuyClicked: ()-> Unit){
    val homeViewModel: HomeViewModel = viewModel()

    Box(
        Modifier
            .fillMaxWidth()
            .height(120.dp)
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            ), contentAlignment = Alignment.CenterStart) {
        Row(Modifier.padding(10.dp)) {
            Box(modifier = Modifier
                .fillMaxHeight()
                .width(100.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .shadow(elevation = 7.dp)
                .background(color = Color.Yellow, shape = RoundedCornerShape(20.dp)), contentAlignment = Alignment.Center){
                Image(painter = rememberImagePainter(data = image), contentDescription = "", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = name, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Row {
                    Text(text = "Rp. ", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                    Text(text = homeViewModel.convertPriceToRupiah(price), color = Color.White, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(modifier = Modifier
                .fillMaxHeight()
                .padding(end = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween) {
                Row() {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(imageVector = Icons.Default.DeleteForever, contentDescription = "", tint = Color.White, modifier = Modifier.clickable { onIconClicked() })
                }

                Spacer(modifier = Modifier.weight(1f))
                Row() {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        Modifier
                            .width(60.dp)
                            .clickable {
                                onBuyClicked()
                            }
                            .height(30.dp)
                            .background(
                                color = Color(0XFFd7ffde),
                                shape = RoundedCornerShape(20.dp)
                            ), contentAlignment = Alignment.Center) {
                        Text(text = "Buy", color = Color(0XFF35b072), fontWeight = FontWeight.Bold)
                    }
                }

            }
        }
    }
}