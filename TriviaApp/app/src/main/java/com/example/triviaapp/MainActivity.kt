package com.example.triviaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.triviaapp.screen.view.TriviaHome
import com.example.triviaapp.screen.viewModel.QuestionViewModel
import com.example.triviaapp.ui.theme.TriviaAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: QuestionViewModel by viewModels()

            MyApp(viewModel = viewModel)
        }
    }
}

@Composable
fun MyApp(viewModel: QuestionViewModel){
    TriviaAppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            TriviaHome(modifier = Modifier.padding(innerPadding), viewModel = viewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

//    MyApp()
}