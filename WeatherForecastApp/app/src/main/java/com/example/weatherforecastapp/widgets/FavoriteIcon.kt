package com.example.weatherforecastapp.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.weatherforecastapp.model.Favorite
import com.example.weatherforecastapp.screens.favorites.viewModel.FavoriteViewModel

@Composable
fun FavoriteIcon(
    title: String,
    favoriteViewModel: FavoriteViewModel,
    isMainScreen: Boolean
) {
    val cityAndCountry = title.split(",")
    val city = cityAndCountry[0]

    val isFavorite by favoriteViewModel.isFavorite.collectAsState()

    LaunchedEffect(city) {
        val favorite = favoriteViewModel.checkIfCityIsFavorite(city)
        favoriteViewModel.setFavoriteStatus(favorite)
    }

    if (isMainScreen) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "favorite icon",
            tint = if (isFavorite) Color.Red.copy(alpha = 0.6f) else Color.Gray.copy(alpha = 0.6f),
            modifier = Modifier
                .scale(0.9f)
                .padding(horizontal = 5.dp)
                .clickable {
                    favoriteViewModel.toggleFavorite(city, cityAndCountry[1])
                }
        )
    }
}
