package com.example.readerapp.screens.detail.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.readerapp.components.BasicLoading
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.components.ResultSnackbar
import com.example.readerapp.components.RoundedButton
import com.example.readerapp.model.Item
import com.example.readerapp.model.MBook
import com.example.readerapp.navigation.ReaderScreens
import com.example.readerapp.screens.detail.viewModel.BookDetailViewModel
import com.example.readerapp.utils.Constants.BOOK_NO_PHOTO
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ReaderBookDetailsScreen(
    navController: NavController,
    bookId: String,
    viewModel: BookDetailViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var isSuccess by remember { mutableStateOf(false) }
    var showResultSnackbar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getBookInfo(bookId)
    }

    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Book Details",
                navController = navController,
                isBackActivate = true
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                ResultSnackbar(
                    isSuccess = isSuccess,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(3.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.isLoading) {
                BasicLoading()
            } else {
                ShowBookDetails(
                    bookInfo = viewModel.bookInfo,
                    navController = navController,
                    viewModel = viewModel,
                    scope = scope,
                    onSaveResult = { success ->
                        isSuccess = success

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "show",
                                duration = SnackbarDuration.Short
                            )
                            if (success) {
                                navController.navigate(ReaderScreens.HomeNavigator.name)
                            }
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun ShowBookDetails(
    bookInfo: Item?,
    navController: NavController,
    viewModel: BookDetailViewModel,
    scope: CoroutineScope,
    onSaveResult: (Boolean) -> Unit
) {
    val bookData = bookInfo?.volumeInfo
    val googleBookId = bookInfo?.id

    Image(
        painter = rememberAsyncImagePainter(
            bookData?.imageLinks?.thumbnail ?: BOOK_NO_PHOTO
        ),
        contentDescription = "Book Image",
        modifier = Modifier
            .width(150.dp)
            .height(150.dp)
            .padding(bottom = 10.dp)
    )

    Text(
        text = bookData?.title ?: "Veri Yüklenemedi",
        style = MaterialTheme.typography.titleLarge,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        maxLines = 19,
        modifier = Modifier.padding(bottom = 5.dp)
    )

    DetailInfoCard(
        title = "Authors",
        content = bookData?.authors.toString()
    )

    DetailInfoCard(
        title = "Page Count",
        content = bookData?.pageCount.toString()
    )

    DetailInfoCard(
        title = "Categories",
        content = bookData?.categories.toString()
    )

    DetailInfoCard(
        title = "Published",
        content = bookData?.publishedDate.toString()
    )

    val cleanDescription = HtmlCompat.fromHtml(bookData?.description ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

    DetailInfoCard(
        title = "Description",
        content = cleanDescription,
        isScrollable = true
    )

    Row(
        modifier = Modifier.padding(top = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        RoundedButton(
            label = "Save"
        ) {
            val book = MBook(
                title = bookData?.title ?: "",
                authors = bookData?.authors.toString(),
                description = bookData?.description,
                categories = bookData?.categories.toString(),
                notes = "",
                photoUrl = bookData?.imageLinks?.thumbnail ?: BOOK_NO_PHOTO,
                publishedDate = bookData?.publishedDate,
                pageCount = bookData?.pageCount.toString(),
                rating = 0.0,
                googleBookId = googleBookId ?: "",
                userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
            )

            scope.launch {
                val result = viewModel.saveBookToFirestore(book)
                onSaveResult(result)
            }
        }

        Spacer(modifier = Modifier.width(25.dp))

        RoundedButton(
            label = "Cancel"
        ) {
            navController.popBackStack()
        }
    }
}

@Composable
fun DetailInfoCard(title: String, content: String, isScrollable: Boolean = false) {
    val cardModifier = if (isScrollable) {
        Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 15.dp, vertical = 7.dp)
    } else {
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 7.dp)
    }

    val columnModifier = if (isScrollable) {
        Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    } else {
        Modifier.padding(10.dp)
    }

    Card(
        modifier = cardModifier,
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = columnModifier) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Text(
                content,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
