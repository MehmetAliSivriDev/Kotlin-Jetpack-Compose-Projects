package com.example.readerapp.screens.profile.view

import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.readerapp.components.BasicLoading
import com.example.readerapp.model.Item
import com.example.readerapp.model.MBook
import com.example.readerapp.navigation.ReaderScreens
import com.example.readerapp.screens.home.viewModel.HomeViewModel
import com.example.readerapp.utils.Constants.BOOK_NO_PHOTO
import com.example.readerapp.utils.formatDate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.Locale

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, navController: NavController, viewModel: HomeViewModel = hiltViewModel()){

    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(
        topBar = {
            ProfileAppBar(title = "Profile")
        }
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding).fillMaxSize()
        ) {

            books = if(!viewModel.data.value.data.isNullOrEmpty()){
                viewModel.data.value.data!!.filter { mBook ->
                    (mBook.userId == currentUser?.uid)
                }
            }else{
                emptyList()
            }

            ReaderStats(
                books = books,
                viewModel = viewModel,
                currentUser = currentUser,
                navController = navController
            )

            Spacer(modifier =  Modifier.height(25.dp))

            LogOutRow(){
                FirebaseAuth.getInstance().signOut().run {
                    navController.navigate(ReaderScreens.LoginScreen.name)
                }
            }
        }
    }
}

@Composable
fun ReaderStats(books: List<MBook>, viewModel: HomeViewModel, currentUser: FirebaseUser?, navController: NavController){
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 10.dp).fillMaxWidth()
    ) {

        Icon(
            imageVector = Icons.Sharp.Person,
            contentDescription = "icon"
        )

        Text(text = "HI ! ${currentUser?.email.toString().split("@")[0].uppercase(Locale.getDefault())}",
            style = MaterialTheme.typography.headlineMedium)

    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        val readBookList: List<MBook> = if(!viewModel.data.value.data.isNullOrEmpty()){
            books.filter { mBook ->
                (mBook.userId == currentUser?.uid ) && (mBook.finishedReading != null)
            }
        }else{
            emptyList()
        }

        val readingBooks = books.filter { mBook ->
            mBook.startedReading != null && mBook.finishedReading == null
        }

        Column(
            modifier = Modifier
                .padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Your Stats",style = MaterialTheme.typography.titleLarge)
            HorizontalDivider()
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "You're reading: ${readingBooks.size}",style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "You've read: ${readBookList.size}",style = MaterialTheme.typography.bodyLarge)
        }
    }

    if (viewModel.data.value.loading == true){
        BasicLoading()
    }else{
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85f),
            contentPadding = PaddingValues(16.dp)
        ) {
           val readBooks: List<MBook>
           = if(!viewModel.data.value.data.isNullOrEmpty()){
               viewModel.data.value.data!!.filter { mBook ->
                   (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
               }
           }else{
               emptyList()
           }

            items(readBooks){ book ->
                BookRowStats(
                    book = book
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAppBar(
    title: String,
){
    TopAppBar(
        title = { Text(text = title) },
    )
}

@Preview
@Composable
fun LogOutRow(onClick: () -> Unit = {}){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 10.dp)
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(15.dp),
        tonalElevation = 1.dp,
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)
                .clip(RoundedCornerShape(15.dp)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,

        ) {
            Text(text = "Log Out",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold)
            Icon(
                imageVector = Icons.AutoMirrored.Default.Logout,
                contentDescription = "log out icon",
                tint = MaterialTheme.colorScheme.onSurface
            )

        }
    }

}


@Composable
fun BookRowStats(book: MBook){
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier =  Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(7.dp)
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top
        ){
            val imageUrl: String = if(book.photoUrl.toString().isEmpty())
                BOOK_NO_PHOTO
            else {
                book.photoUrl.toString()
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
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = book.title.toString(),overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium)
                    if((book.rating ?: 0.0) >= 4){
                        Spacer(modifier = Modifier.fillMaxWidth(0.8f))
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "",
                            tint = Color.Green.copy(alpha = 0.5f)
                        )
                    }
                }
                Text(text = "Author: ${book.authors}",overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium)
                Text(text = "Started: ${formatDate(book.startedReading!!)}",overflow = TextOverflow.Clip,
                    softWrap = true,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium)
                Text(text = "Finished: ${formatDate(book.finishedReading!!)}",overflow = TextOverflow.Clip,
                    softWrap = true,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}