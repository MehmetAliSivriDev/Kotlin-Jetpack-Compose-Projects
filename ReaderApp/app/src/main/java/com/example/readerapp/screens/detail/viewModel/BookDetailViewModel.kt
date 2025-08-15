package com.example.readerapp.screens.detail.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.readerapp.data.Resource
import com.example.readerapp.model.Item
import com.example.readerapp.model.MBook
import com.example.readerapp.repository.BookRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(private val repository: BookRepository): ViewModel() {

    var bookInfo: Item? by mutableStateOf(null)
    var isLoading: Boolean by mutableStateOf(false)

    val db = FirebaseFirestore.getInstance()

    fun changeLoading(){
        isLoading = !isLoading
    }

    fun getBookInfo(bookId: String){
        viewModelScope.launch {
            if (bookId.isEmpty()){
                return@launch
            }
            try {
                changeLoading()
                when(val response = repository.getBookInfo(bookId = bookId)) {
                    is Resource.Success -> {
                        response.data?.let {
                            bookInfo = it
                        }
                        if (bookInfo != null) changeLoading()
                    }
                    is Resource.Error -> {
                        changeLoading()
                        Log.e("Network", "searchBooks: Failed getting book info", )
                    }
                    else -> {changeLoading()}
                }

            }catch (exception: Exception){
                changeLoading()
                Log.d("Network", "bookInfo: ${exception.message.toString()}")
            }
        }
    }

    suspend fun saveBookToFirestore(book: MBook): Boolean = suspendCancellableCoroutine { cont ->
        val dbCollection = db.collection("books")
        if (book.toString().isEmpty()) {
            cont.resume(false) {}
            return@suspendCancellableCoroutine
        }
        dbCollection.add(book)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(mapOf("id" to docId))
                    .addOnCompleteListener { task ->
                        cont.resume(task.isSuccessful) {}
                    }
            }
            .addOnFailureListener {
                cont.resume(false) {}
            }
    }

}
