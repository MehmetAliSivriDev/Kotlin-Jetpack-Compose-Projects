package com.example.weatherforecastapp.widgets

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherforecastapp.model.Favorite
import com.example.weatherforecastapp.navigation.WeatherScreens
import com.example.weatherforecastapp.screens.favorites.viewModel.FavoriteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAppBar(
    navController: NavController,
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    title: String = "Title",
    icon: ImageVector? = null,
    isMainScreen: Boolean = true,
    elevation: Dp = 0.dp,
    onAddActionClicked: () -> Unit = {},
    onButtonClicked: () -> Unit = {}
){
    val showDialog = remember {
        mutableStateOf(false)
    }

    val expanded = remember {
        mutableStateOf(false)
    }

    if(showDialog.value){
       ShowSettingDropDownMenu(showDialog = showDialog, expanded = expanded  ,navController = navController)
    }


    TopAppBar(
        title = { Text(text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)
            ) },
        actions = {
            if (isMainScreen){
                IconButton(
                    onClick = {
                        onAddActionClicked.invoke()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                }
                IconButton(
                    onClick = {
                        showDialog.value = true
                        expanded.value = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = "More Icon"
                    )
                }
            }else{
                Box {  }
            }
        },
        navigationIcon = {
            if(icon != null){
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.clickable {
                        onButtonClicked.invoke()
                    }
                    )
            }


            FavoriteIcon(
                title = title,
                favoriteViewModel = favoriteViewModel,
                isMainScreen = isMainScreen
            )

        },
        colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
        modifier = Modifier.shadow(elevation)
    )
}

@Composable
fun ShowSettingDropDownMenu(showDialog: MutableState<Boolean>, expanded: MutableState<Boolean> ,navController: NavController) {

    val items = listOf("About" , "Favorites" , "Settings")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
            .absolutePadding(top = 45.dp, right = 20.dp )
    ) {
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            },
            modifier = Modifier
                .width(140.dp)
                .background(Color.White)
        ) {
            items.forEachIndexed { index, text ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.clickable {
                                navController.navigate(when(text){
                                    "About" -> WeatherScreens.AboutScreen.name
                                    "Favorites" -> WeatherScreens.FavoritesScreen.name
                                    else -> WeatherScreens.SettingsScreen.name
                                })
                            }
                        ) {
                            Icon(
                                imageVector = when(text){
                                    "About" -> Icons.Default.Info
                                    "Favorites" -> Icons.Default.FavoriteBorder
                                    else -> Icons.Default.Settings
                                },
                                contentDescription = null,
                                tint = Color.LightGray)
                            Text(text = text,
                                fontWeight = FontWeight.W300,
                            )
                        }
                    },
                    onClick = {
                        expanded.value = false
                        showDialog.value = false
                    }
                )
            }
        }
    }
}
