package com.example.readerapp.screens.search.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readerapp.data.DataOrException
import com.example.readerapp.data.Resource
import com.example.readerapp.model.Item
import com.example.readerapp.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(private val repository: BookRepository): ViewModel() {

    var list: List<Item> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(false)
    init {
        loadBooks()
    }

    private fun changeLoading(){
        isLoading = !isLoading
    }

    private fun loadBooks() {
        searchBooks("flutter")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch(Dispatchers.Default) {

            if (query.isEmpty()){
                return@launch
            }
            try {
                changeLoading()
                when(val response = repository.getAllBooks(query)) {
                    is Resource.Success -> {
                        response.data?.let {
                            list = it
                        }
                        if (list.isNotEmpty()) changeLoading()
                    }
                    is Resource.Error -> {
                        changeLoading()
                        Log.e("Network", "searchBooks: Failed getting books", )
                    }
                    else -> {changeLoading()}
                }

            }catch (exception: Exception){
                changeLoading()
                Log.d("Network", "searchBooks: ${exception.message.toString()}")
            }

        }
    }
}