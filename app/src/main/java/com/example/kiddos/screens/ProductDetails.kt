package com.example.kiddos.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.kiddos.ViewModel.HomeViewModel
import com.example.kiddos.ViewModel.LogInViewModel
import com.example.kiddos.ViewModel.ProductDetailsViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ProductDetails(
    homeViewModel: HomeViewModel = viewModel(),
    navController: NavHostController,
    productDetailsViewModel: ProductDetailsViewModel = viewModel(),
    logInViewModel: LogInViewModel = viewModel()
) {
    val productDetails by homeViewModel.fetchProductDetails.collectAsState()
    val loading by productDetailsViewModel.isLoading.collectAsState()
    var available = remember { mutableStateOf("") }
    val quantityMap = remember { mutableStateMapOf<String, Int>() }
    val availableStock = when (available.value) {
        "M" -> productDetails.size["M"] ?: 0
        "L" -> productDetails.size["L"] ?: 0
        "XL" -> productDetails.size["XL"] ?: 0
        else -> 0
    }
    var quantity by remember { mutableStateOf(quantityMap[available.value] ?: 0) }
    val status by productDetailsViewModel.isSuccess.collectAsState()

    LaunchedEffect(available.value) {
        quantity = quantityMap[available.value] ?: if (availableStock > 0) 1 else 0
    }

    LaunchedEffect(status) {
        if (status) {
            navController.navigate("home")
        }
    }

    val totalPrice = remember(quantity, productDetails.price) {
        quantity * productDetails.price
    }
    var showImage by remember { mutableStateOf("") }

    LaunchedEffect(productDetails) {
        showImage = if (productDetails.image.isNotEmpty()) productDetails.image[0] else ""
    }
    val coroutine = rememberCoroutineScope()
    val userData by logInViewModel.fetchData.collectAsState()
    var address by remember { mutableStateOf("") }
    var scrollState = rememberScrollState()
    var phoneNumber by remember { mutableStateOf("") }
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val localDate = LocalDate.now().format(formatter)

    BackHandler {
        if (productDetailsViewModel.navigateFrom.value == "home") {
            navController.navigate("home")
        } else if(productDetailsViewModel.navigateFrom.value == "wishList") {
            navController.navigate("wishList")
        } else {
            navController.navigate("history")
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color(0XFFae1c2c))
    ) {
        Column {
            Box(
                Modifier
                    .height(350.dp)
                    .fillMaxWidth()
                    .background(color = Color.Gray)
            ) {
                if (showImage.isNotEmpty()) {
                    Image(
                        painter = rememberImagePainter(data = showImage),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No Image Available", color = Color.White)
                    }
                }

                Row(
                    Modifier
                        .padding(top = 40.dp, start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier
                            .size(45.dp)
                            .clickable {
                                if (productDetailsViewModel.navigateFrom.value == "home") {
                                    navController.navigate("home")
                                } else {
                                    navController.navigate("wishList")
                                }
                            }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Back",
                        color = Color.White,
                        fontSize =  20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom) {
                    Column {
                        Box(
                            Modifier
                                .size(height = 80.dp, width = 135.dp)
                                .background(color = Color.White)
                        ) {
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 5.dp, top = 5.dp, bottom = 5.dp)) {
                                LazyRow {
                                    items(productDetails.image) { item ->
                                        Box(
                                            Modifier
                                                .size(60.dp)
                                                .clickable {
                                                    showImage = item
                                                }) {
                                            Image(
                                                painter = rememberImagePainter(data = item),
                                                contentDescription = "",
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(5.dp))
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Column(Modifier.verticalScroll(scrollState)) {
                    Text(
                        text = productDetails.name,
                        color = Color.White,
                        fontWeight = FontWeight.W900,
                        fontSize = 30.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Size",
                        color = Color(0XFFffc66d),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        listOf("M", "L", "XL").forEach { size ->
                            Box(
                                Modifier
                                    .border(
                                        width = 2.dp,
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .clickable {
                                        available.value = size
                                        quantity =
                                            quantityMap[size] ?: if (availableStock > 0) 1 else 0
                                    }
                                    .height(50.dp)
                                    .background(
                                        color = if (available.value == size) Color(0XFFffc66d) else Color.Transparent,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .width(60.dp), contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = size,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "available: $availableStock",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Box(
                        Modifier
                            .height(60.dp)
                            .width(130.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(40.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        color = Color(0XFFae1c2c),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .clickable {
                                        if (quantity > 0) {
                                            quantity--
                                            quantityMap[available.value] = quantity
                                        }
                                    }, contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Remove,
                                    contentDescription = "",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(
                                text = quantity.toString(),
                                color = Color(0XFFae1c2c),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        color = Color(0XFFae1c2c),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .clickable {
                                        if (quantity < availableStock) {
                                            quantity++
                                            quantityMap[available.value] = quantity
                                        }
                                    }, contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "Address",
                        color = Color(0XFFffc66d),
                        fontSize = 20.sp, fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextFieldBox(
                        value = address,
                        width = 400,
                        desc = "address",
                        onValueChange = { address = it }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Phone Number",
                        color = Color(0XFFffc66d),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextFielNumberdBox(
                        value = phoneNumber,
                        width = 400,
                        desc = "Phone Number",
                        onValueChange = { phoneNumber = it }
                    )
                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
        }
        Column(
            Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter), verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(color = Color(0XFFae1c2c))
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 20.dp)
                ) {
                    Box(
                        Modifier
                            .height(50.dp)
                            .border(
                                width = 1.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(20.dp)
                            ), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = homeViewModel.convertPriceToRupiah(totalPrice),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    if(loading){
                        Box(
                            Modifier
                                .height(50.dp)
                                .weight(1f)
                                .background(
                                    color = Color(0XFF332d26),
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(strokeWidth = 5.dp, color = Color.White, modifier = Modifier.size(30.dp))
                        }
                    } else {
                        Box(
                            Modifier
                                .height(50.dp)
                                .clickable {
                                    coroutine.launch {
                                        if (available.value.isNotEmpty() && quantity != 0) {
                                            productDetailsViewModel.buy(
                                                email = userData.email,
                                                productName = productDetails.name,
                                                size = available.value,
                                                quantity = quantity,
                                                address = address,
                                                image = productDetails.image[0],
                                                phoneNumber = phoneNumber,
                                                status = "On Progress",
                                                price = quantity * productDetails.price,
                                                date = localDate
                                            )
                                        } else {
                                            println("no product")
                                        }
                                    }
                                }
                                .weight(1f)
                                .background(
                                    color = Color(0XFF332d26),
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Purchase",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    }

                }
            }
        }
    }
}