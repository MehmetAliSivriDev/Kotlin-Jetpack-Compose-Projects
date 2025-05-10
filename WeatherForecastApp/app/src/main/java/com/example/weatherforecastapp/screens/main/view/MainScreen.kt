package com.example.weatherforecastapp.screens.main.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherforecastapp.data.DataOrException
import com.example.weatherforecastapp.model.Weather
import com.example.weatherforecastapp.model.WeatherItem
import com.example.weatherforecastapp.navigation.WeatherScreens
import com.example.weatherforecastapp.screens.main.viewModel.MainViewModel
import com.example.weatherforecastapp.screens.settings.viewModel.SettingsViewModel
import com.example.weatherforecastapp.utils.formatDate
import com.example.weatherforecastapp.utils.formatDecimals
import com.example.weatherforecastapp.widgets.HumidityWindPressureRow
import com.example.weatherforecastapp.widgets.LoadingStatus
import com.example.weatherforecastapp.widgets.SunRiseAndSetRow
import com.example.weatherforecastapp.widgets.WeatherAppBar
import com.example.weatherforecastapp.widgets.WeatherDetailRow
import com.example.weatherforecastapp.widgets.WeatherStateImage

@Composable
fun MainScreen(navController: NavController,
               viewModel: MainViewModel = hiltViewModel(),
               settingsViewModel: SettingsViewModel = hiltViewModel(),
               city: String?){

    val unitFromDb = settingsViewModel.unitList.collectAsState().value

    var unit by remember {
        mutableStateOf("imperial")
    }

    var isImperial by remember {
        mutableStateOf(false)
    }

    if(!unitFromDb.isNullOrEmpty()){
        unit = unitFromDb[0].unit.split(" ")[0].lowercase()
        isImperial = unit == "imperial"

        val weatherState = produceState<DataOrException<Weather,Boolean,Exception>>(
            initialValue = DataOrException(loading = true)
        ){
            value = viewModel.getWeather(city = city ?: "Istanbul", units = unit)
        }.value

        if(weatherState.loading == true){
            LoadingStatus()
        }else if (weatherState.data != null){
            MainScaffold(weather = weatherState.data!!, navController = navController, isImperial = isImperial)
        }
    }
}

@Composable
fun MainScaffold(weather: Weather, navController: NavController, isImperial: Boolean){

    Scaffold(
        topBar = {
            WeatherAppBar(
                
                navController = navController,
                onAddActionClicked = {
                    navController.navigate(WeatherScreens.SearchScreen.name)
                },
                title = "${weather.city.name}, ${weather.city.country}",
                elevation = 5.dp
            )
        }
    ) { innerPadding ->
        MainContent(modifier = Modifier.padding(innerPadding), data = weather, isImperial = isImperial)
    }

}



@Composable
fun MainContent(modifier: Modifier = Modifier, data: Weather, isImperial: Boolean){

    val imageUrl = ""

    Column(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = formatDate(data.list[0].dt),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(6.dp))

        Surface(
            modifier = Modifier.padding(4.dp).size(200.dp),
            shape = CircleShape,
            color = Color(0xFFFFC400)

        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

            ) {
                WeatherStateImage(imageUrl = imageUrl)
                Text(text = formatDecimals(data.list[0].temp.day) + "°",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.ExtraBold)
                Text(text = data.list[0].weather[0].main,
                    fontStyle = FontStyle.Italic)
            }
        }
        HumidityWindPressureRow(data.list[0], isImperial = isImperial)
        HorizontalDivider(color = Color.Gray)
        SunRiseAndSetRow(data.list[0])

        Text("This Week",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold)

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            color = Color(0xFFEEF1EF),
            shape = RoundedCornerShape(size = 14.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(2.dp),
                contentPadding = PaddingValues(1.dp)
            ) {
                items(data.list){item: WeatherItem ->
                    WeatherDetailRow(item)

                }

            }
        }
    }
}


