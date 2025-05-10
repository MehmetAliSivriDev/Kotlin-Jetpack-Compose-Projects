package com.example.weatherforecastapp.screens.favorites.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.model.Favorite
import com.example.weatherforecastapp.repository.WeatherDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: WeatherDbRepository): ViewModel() {

    private val _favList = MutableStateFlow<List<Favorite>>(emptyList())
    val favList = _favList.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    init {
        getFavorites()
    }

    fun getFavorites(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavorites().distinctUntilChanged().collect({
                 listOfFavs ->
                if(listOfFavs.isNotEmpty()) _favList.value = listOfFavs
            })
        }
    }

    fun insertFavorite(favorite: Favorite){
        viewModelScope.launch {
            repository.insertFavorite(favorite)
        }
    }

    fun updateFavorite(favorite: Favorite){
        viewModelScope.launch {
            repository.updateFavorite(favorite)
        }
    }

    fun deleteAllFavorites(){
        viewModelScope.launch {
            repository.deleteAllFavorites()
        }
    }

    fun deleteFavorite(favorite: Favorite){
        viewModelScope.launch {
            repository.deleteFavorite(favorite)
        }
    }

    suspend fun checkIfCityIsFavorite(city: String): Boolean {
        val favorite = repository.getFavById(city)

        return favorite != null
    }

    fun setFavoriteStatus(status: Boolean){
        _isFavorite.value = status
    }

    fun toggleFavorite(city: String, country: String) {
        if (_isFavorite.value) {
            deleteFavorite(Favorite(city = city, country = country))
        } else {
            insertFavorite(Favorite(city = city, country = country))
        }
        _isFavorite.value = !_isFavorite.value
    }

}