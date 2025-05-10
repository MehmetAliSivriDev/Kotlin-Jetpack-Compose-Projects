package com.example.weatherforecastapp.screens.main.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.data.DataOrException
import com.example.weatherforecastapp.model.Weather
import com.example.weatherforecastapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel(){

    suspend fun getWeather(city: String, units: String): DataOrException<Weather, Boolean, Exception>{
        return repository.getWeather(cityQuery = city, units = units)
    }



//    val data: MutableState<DataOrException<Weather,Boolean,Exception>>
//    = mutableStateOf(DataOrException(null,true,Exception("")))
//
//    init {
//        loadWeather()
//    }
//
//
//
//    private fun loadWeather(){
//        getWeather("seatle")
//    }
//
//    private fun getWeather(city: String){
//        viewModelScope.launch {
//
//            if(city.isEmpty()) return@launch
//
//            data.value.loading = true
//            data.value = repository.getWeather(city)
//
//            if(data.value.data.toString().isNotEmpty()) data.value.loading = false
//
//        }
//    }
}