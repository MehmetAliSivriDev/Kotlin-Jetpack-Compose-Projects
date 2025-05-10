package com.example.weatherforecastapp.screens.settings.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.model.Favorite
import com.example.weatherforecastapp.model.Unit
import com.example.weatherforecastapp.repository.WeatherDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: WeatherDbRepository): ViewModel() {

    private val _unitList = MutableStateFlow<List<Unit>>(emptyList())
    val unitList = _unitList.asStateFlow()

    init {
        getAllUnits()
    }

    fun getAllUnits() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUnits().distinctUntilChanged().collect { listOfUnits ->
                if (listOfUnits.isNotEmpty()) {
                    _unitList.value = listOfUnits
                } else {
                    _unitList.value = listOf(Unit(unit = "Imperial (F)"))
                }
            }
        }
    }

    fun insertUnit(unit: Unit){
        viewModelScope.launch {
            repository.insertUnit(unit)
        }
    }

    fun updateUnit(unit: Unit){
        viewModelScope.launch {
            repository.updateUnit(unit)
        }
    }

    fun deleteAllUnits(){
        viewModelScope.launch {
            repository.deleteAllUnits()
        }
    }

    fun deleteUnit(unit: Unit){
        viewModelScope.launch {
            repository.deleteUnit(unit)
        }
    }

}