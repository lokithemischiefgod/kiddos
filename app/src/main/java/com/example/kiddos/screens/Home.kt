package com.example.kiddos.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.kiddos.R
import com.example.kiddos.ViewModel.HomeViewModel
import com.example.kiddos.ViewModel.LogInViewModel
import com.example.kiddos.ViewModel.ProductDetailsViewModel

@Composable
fun Home(homeViewModel: HomeViewModel = viewModel(), logInViewModel: LogInViewModel = viewModel(), navController: NavHostController, productDetailsViewModel: ProductDetailsViewModel = viewModel()){
    val products by homeViewModel.fetchProduct.collectAsState()
    val user by logInViewModel.fetchData.collectAsState()

    LaunchedEffect(Unit){
        homeViewModel.getProductFromDb()
        productDetailsViewModel.setStatusBackToFalse()
        logInViewModel.setBackToFalse()
    }

    BackHandler {

    }


    val topSellingProduct = products.maxByOrNull { it.totalSelled }
    val scrollState = rememberScrollState()
    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color(0XFFae1c2c))) {
        Column(
            Modifier
                .padding(top = 60.dp, start = 20.dp, end = 20.dp)
                .fillMaxHeight()
                .verticalScroll(scrollState)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Hi, Kid", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 35.sp)
                Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = "", tint = Color.White, modifier = Modifier
                    .size(40.dp)
                    .clickable { navController.navigate("wishList") })
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .shadow(elevation = 7.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(color = Color.Black, shape = RoundedCornerShape(20.dp))
                    .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(20.dp))) {
                    Image(painter = painterResource(id = R.drawable.world_tour), contentDescription = "", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Fit)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Top Rated", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0XFFffc66d))
            Spacer(modifier = Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth()) {
                if (topSellingProduct != null) {
                    ProductBox(
                        name = topSellingProduct.name,
                        image = topSellingProduct.image.getOrNull(0) ?: "default_image_url",
                        price =  topSellingProduct.price ,
                         onBoxClicked = { homeViewModel.getProductDetails(topSellingProduct.name)
                                            navController.navigate("productDetails")
                             productDetailsViewModel.navigateFrom.value = "home"
                                        }, onIconClicked = { homeViewModel.addProductToWishList(email = user.email, product = topSellingProduct.name) }
                    )
                } else {
                    Text(text = "Loading data...")
                }

            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "other products", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0XFFffc66d))
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(250.dp)) {
                LazyVerticalGrid(columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)){
                    items(products){
                        product -> ProductBox(name = product.name, price = product.price, image = product.image[0],
                        onBoxClicked = {  homeViewModel.getProductDetails(product.name)
                            navController.navigate("productDetails")
                            productDetailsViewModel.navigateFrom.value = "home"
                                       }, onIconClicked = { homeViewModel.addProductToWishList(email = user.email, product = product.name ) })
                    }
                }

            }
            Spacer(modifier = Modifier.height(100.dp))
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
                            println("tes ini ${user.email}")
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

@Composable
fun ProductBox(name: String, price: Int, image: String, onBoxClicked: () -> Unit, onIconClicked: () -> Unit){
    val homeViewModel : HomeViewModel = viewModel()

    Box(
        Modifier
            .width(180.dp)
            .clickable { onBoxClicked() }
            .height(230.dp)
            .shadow(elevation = 7.dp)
            .background(color = Color(0XFFae1c2c), shape = RoundedCornerShape(20.dp))
            .border(
                width = 2.dp,
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            )) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(color = Color.Yellow, shape = RoundedCornerShape(20.dp))) {
                Image(painter = rememberImagePainter(data = image), contentDescription = "", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 120.dp, top = 5.dp)) {
                    Box(
                        Modifier
                            .size(35.dp)
                            .background(
                                color = Color(0XFFe2ddd6),
                                shape = RoundedCornerShape(20.dp)
                            ), contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FavoriteBorder,
                            contentDescription = "",
                            modifier = Modifier.clickable {
                                onIconClicked()
                            }
                        )
                    }
                }

            }
            Column(Modifier.padding(5.dp)) {
                Text(text = name, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = "Kiddos", color = Color.Black, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Row {
                    Text(text = "Rp. ", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                    Text(text = homeViewModel.convertPriceToRupiah(price), color = Color.White, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                }
            }

        }
    }
}