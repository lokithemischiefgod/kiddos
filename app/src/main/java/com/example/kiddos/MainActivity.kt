package com.example.kiddos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.Navigation
import com.example.kiddos.navigations.navigation
import com.example.kiddos.screens.History
import com.example.kiddos.screens.Home
import com.example.kiddos.screens.LogIn
import com.example.kiddos.screens.ProductDetails
import com.example.kiddos.screens.Profile
import com.example.kiddos.screens.SignUp
import com.example.kiddos.screens.WishList
import com.example.kiddos.ui.theme.KiddosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(), Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(), Color.Transparent.toArgb()
            ),
        )
        super.onCreate(savedInstanceState)
        setContent {
            navigation()
        }
    }
}
