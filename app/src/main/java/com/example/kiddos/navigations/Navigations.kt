package com.example.kiddos.navigations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kiddos.ViewModel.HomeViewModel
import com.example.kiddos.ViewModel.LogInViewModel
import com.example.kiddos.ViewModel.ProductDetailsViewModel
import com.example.kiddos.ViewModel.SignUpViewModel
import com.example.kiddos.factory.LogInViewModelFactory
import com.example.kiddos.screens.History
import com.example.kiddos.screens.Home
import com.example.kiddos.screens.LogIn
import com.example.kiddos.screens.ProductDetails
import com.example.kiddos.screens.Profile
import com.example.kiddos.screens.SignUp
import com.example.kiddos.screens.WishList

@Composable
fun navigation(navController: NavHostController = rememberNavController()){
    val context = LocalContext.current
    val logInViewModel: LogInViewModel = viewModel(factory = LogInViewModelFactory(context))
    val signUpViewModel: SignUpViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val productDetailsViewModel: ProductDetailsViewModel = viewModel()

    val isLoggedIn by logInViewModel.isLoggedin.collectAsState()

    NavHost(navController = navController, startDestination = if(isLoggedIn) "home" else "logIn"){
        composable("logIn") { LogIn(navController = navController, logInViewModel = logInViewModel, signUpViewModel = signUpViewModel) }
        composable("signUp") { SignUp(navHostController = navController, signUpViewModel = signUpViewModel,) }
        composable("home") { Home(homeViewModel = homeViewModel, navController = navController, logInViewModel = logInViewModel, productDetailsViewModel = productDetailsViewModel)}
        composable("productDetails") { ProductDetails(homeViewModel = homeViewModel, navController = navController, logInViewModel = logInViewModel, productDetailsViewModel = productDetailsViewModel) }
        composable("profile") { Profile(navController = navController, logInViewModel = logInViewModel) }
        composable("wishList") { WishList(navController = navController, homeViewModel = homeViewModel, logInViewModel = logInViewModel, productDetailsViewModel = productDetailsViewModel) }
        composable("history") { History(logInViewModel = logInViewModel, navController = navController, homeViewModel = homeViewModel, productDetailsViewModel = productDetailsViewModel) }
    }

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate("home") {
                popUpTo("logIn") { inclusive = true }
            }
        }
    }
}