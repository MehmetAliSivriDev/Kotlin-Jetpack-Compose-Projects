package com.example.readerapp.screens.home.view

import android.widget.HorizontalScrollView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.readerapp.components.BasicLoading
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.components.ListCard
import com.example.readerapp.components.TitleSection
import com.example.readerapp.model.MBook
import com.example.readerapp.navigation.ReaderScreens
import com.example.readerapp.screens.home.viewModel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ReaderHomeScreen(modifier: Modifier = Modifier , navController: NavController,
                     viewModel: HomeViewModel = hiltViewModel()
){
   Scaffold(
       topBar = {
           ReaderAppBar(
               title = "A.Reader",
               navController = navController
           )
       },
   ) { innerPading ->
       HomeContent(modifier = Modifier.padding(innerPading), navController = navController, viewModel = viewModel)
   }
}

@Composable
fun HomeContent(modifier: Modifier = Modifier ,navController: NavController, viewModel: HomeViewModel){
    val email = FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
    val currentUserName = if(!email)
        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)
    else "N/A"

    var listOfBooks = emptyList<MBook>()
    val currentUser = FirebaseAuth.getInstance().currentUser

    if(!viewModel.data.value.data.isNullOrEmpty()){
        listOfBooks = viewModel.data.value.data!!.toList().filter { mBook ->
            mBook.userId == currentUser?.uid.toString()
        }
    }


//    val listOfBooks = listOf(
//        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null),
//        MBook(id = "dadfa", title = " Again", authors = "All of us", notes = null),
//        MBook(id = "dadfa", title = "Hello ", authors = "The world us", notes = null),
//        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null),
//        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null)
//                            )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 2.dp, end = 2.dp, top = 5.dp, bottom = 2.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            border = BorderStroke(1.dp, color = Color.Gray),
            shape = RoundedCornerShape(15.dp),
        ) {
            Row(
                modifier = Modifier.align(alignment = Alignment.Start).padding(5.dp)
            ) {
                TitleSection(label = "Your Reading\nActivity Right Now")
                Spacer(modifier = Modifier.fillMaxWidth(0.7f))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "profile",
                        modifier = Modifier
                            .size(45.dp)
                            .clickable { },
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = currentUserName!!,
                        modifier = Modifier.padding(2.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                }
            }
        }
        ReadingRightNowArea(
            books = listOfBooks,
            navController = navController
        )
        BookListArea(listOfBooks = listOfBooks,
            navController = navController)
    }
}


@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController){
    val readingNowList = books.filter { mBook ->
        mBook.startedReading != null && mBook.finishedReading == null
    }

    HorizontalScrollableComponent(readingNowList){
        navController.navigate(ReaderScreens.UpdateScreen.name +"/$it")

    }
}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavController){
    val addedBooks = listOfBooks.filter { mBook ->
        mBook.startedReading == null && mBook.finishedReading == null
    }

    TitleSection(label = "Reading List")

    HorizontalScrollableComponent(addedBooks){
        navController.navigate(ReaderScreens.UpdateScreen.name +"/$it")

    }
}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<MBook>, viewModel: HomeViewModel = hiltViewModel(),onCardPress: (String) -> Unit = {}){
    val scrollState = rememberScrollState()

    Row(
      modifier = Modifier
          .fillMaxWidth()
          .heightIn(280.dp)
          .horizontalScroll(scrollState)
    ) {

        if(viewModel.data.value.loading == true){
            BasicLoading()
        }else{

            if(listOfBooks.isNullOrEmpty()){
                Surface(
                    modifier = Modifier.padding(23.dp)
                ) {
                    Text(text = "No books found. Add a Book",
                        style = TextStyle(
                            color = Color.Red.copy(alpha = 0.4f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp

                        )
                    )
                }
            }else{
                for(book in listOfBooks){
                    ListCard(book){
                        onCardPress(book.googleBookId.toString())
                    }
                }
            }
        }
    }
}