package com.example.weatherforecastapp.screens.splash.view

import android.view.animation.OvershootInterpolator
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weatherforecastapp.R
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import com.example.weatherforecastapp.navigation.WeatherScreens
import kotlinx.coroutines.delay


@Composable
fun WeatherSplashScreen(navController: NavController){

    val defaultCity = "Istanbul"

    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(
        key1 = true,
        block = {
            scale.animateTo(targetValue = 0.9f,
                tween(
                    durationMillis = 800,
                    easing = {
                        OvershootInterpolator(8f)
                            .getInterpolation(it)
                    }
                )
            )
            delay(2000L)
            navController.navigate(WeatherScreens.MainScreen.name + "/$defaultCity")
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier
                .padding(15.dp)
                .size(330.dp),
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(width = 2.dp,color = Color.LightGray)
        ) {
            Column(
                modifier = Modifier.padding(1.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(
                        id = R.drawable.sun
                    ),
                    contentDescription = "Sunny Icon",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(95.dp)
                )
                Text(text = "Find the Sun ?",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.LightGray)
            }
        }
    }

}