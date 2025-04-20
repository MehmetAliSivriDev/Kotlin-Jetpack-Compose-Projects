package com.example.triviaapp.screen.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.triviaapp.component.Questions
import com.example.triviaapp.screen.viewModel.QuestionViewModel

@Composable
fun TriviaHome(modifier: Modifier = Modifier, viewModel: QuestionViewModel){
    Questions(viewModel = viewModel)
}