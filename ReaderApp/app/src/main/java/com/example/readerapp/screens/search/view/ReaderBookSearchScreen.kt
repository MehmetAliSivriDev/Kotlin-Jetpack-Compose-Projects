package com.example.readerapp.screens.search.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.readerapp.components.BasicLoading
import com.example.readerapp.components.InputField
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.model.Item
import com.example.readerapp.navigation.ReaderScreens
import com.example.readerapp.screens.search.viewModel.BookSearchViewModel
import com.example.readerapp.utils.Constants.BOOK_NO_PHOTO

@Composable
fun ReaderBookSearchScreen(navController: NavController, viewModel: BookSearchViewModel = hiltViewModel()){
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Search Books",
                navController = navController,
                isBackActivate = true
            )
        }
    ) {innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            SearchForm(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 2.dp)
            ){ query ->
                viewModel.searchBooks(query = query)
            }
            Spacer(modifier = Modifier.height(13.dp))

            BookList(navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
){
    Column(
        modifier = modifier
    ) {
        val searchQueryState = rememberSaveable {
            mutableStateOf("")
        }

        val keyboardController = LocalSoftwareKeyboardController.current

        val valid = remember(searchQueryState.value){
            searchQueryState.value.trim().isNotEmpty()
        }

        InputField(
            valueState = searchQueryState,
            labelId = "Search",
            enabled = true,
            imeAction = ImeAction.Done,
            onAction = KeyboardActions {
                if(!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            }
        )
    }
}

@Composable
fun BookList(navController: NavController, viewModel: BookSearchViewModel){
//    val listOfBooks = listOf(
//        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null),
//        MBook(id = "dadfa", title = " Again", authors = "All of us", notes = null),
//        MBook(id = "dadfa", title = "Hello ", authors = "The world us", notes = null),
//        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null),
//        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null)
//    )

    if(viewModel.isLoading){
        BasicLoading()
    }else{
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),

        ) {
            items(items = viewModel.list){book->
                BookRow(book = book, navController = navController)
            }
        }
    }
}

@Composable
fun BookRow(book: Item, navController: NavController){
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier =  Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .clickable {
                navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")
            },
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(7.dp)
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top
        ){
            val imageUrl: String = if(book.volumeInfo.imageLinks.smallThumbnail.isEmpty())
                BOOK_NO_PHOTO
            else {
                book.volumeInfo.imageLinks.smallThumbnail
            }
            Image(
                painter = rememberAsyncImagePainter(
                    imageUrl
                ),
                contentDescription = "Book Image",
//                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(80.dp)
                    .heightIn(100.dp)
                    .padding(end = 4.dp)
            )

            Column() {
                Text(text = book.volumeInfo.title,overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium)
                Text(text = "Author: ${book.volumeInfo.authors}",overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium)
                Text(text = "Date: ${book.volumeInfo.publishedDate}",overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium)
                Text(text = "${book.volumeInfo.categories}",overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}