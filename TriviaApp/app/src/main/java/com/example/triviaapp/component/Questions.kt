package com.example.triviaapp.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triviaapp.model.QuestionItem
import com.example.triviaapp.screen.viewModel.QuestionViewModel
import com.example.triviaapp.util.AppColors

@Composable
fun Questions(viewModel: QuestionViewModel){

    val questions = viewModel.data.value.data?.toMutableList()

    val questionIndex = remember {
        mutableStateOf(0)
    }

    if(viewModel.data.value.loading == true){

        LoadingDisplay()

    }else{

        val question = questions?.getOrNull(questionIndex.value)

        question?.let {
            QuestionDisplay(
                question = it
                ,questionIndex = questionIndex
                , viewModel = viewModel){
                questionIndex.value++
            }
        }
    }

}

@Composable
fun LoadingDisplay(){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppColors.mDarkPurple
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = Color.White,
                strokeWidth = 3.dp
            )
        }
    }
}

//@Preview
@Composable
fun QuestionDisplay(
    question: QuestionItem,
    questionIndex: MutableState<Int>,
    viewModel: QuestionViewModel,
    onNextClicked: (Int) -> Unit = {}
){

    val choicesState = remember(question) {
        question.choices.toMutableList()
    }

    val answerState = remember(question) {
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(question){
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswer: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == question.answer
        }
    }

    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f,10f),0f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
//            .padding(4.dp),
        color = AppColors.mDarkPurple
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ){
            ShowProgress(score = questionIndex.value)

            QuestionTracker(
                counter = (questionIndex.value + 1),
                outOf = viewModel.data.value.data?.size ?: 0
            )
            DrawDottedLine(pathEffect = pathEffect)

            Text(text = question.question,
                modifier = Modifier
                    .padding(6.dp)
                    .align(alignment = Alignment.Start)
                    .fillMaxHeight(0.3f),
                fontSize = 17.sp,
                color = AppColors.mOffWhite,
                fontWeight = FontWeight.Bold,
                lineHeight = 22.sp)

            choicesState.forEachIndexed {index, answerText ->
                Row (
                    modifier = Modifier
                        .padding(3.dp)
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 4.dp, brush = Brush.linearGradient(
                            colors = listOf(AppColors.mOffDarkPurple,AppColors.mOffDarkPurple)),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.Transparent)
                        .clickable { updateAnswer(index) },
                    verticalAlignment = Alignment.CenterVertically
                ){
                    RadioButton(
                        modifier = Modifier.padding(start = 16.dp),
                        colors = RadioButtonDefaults.colors(
                            selectedColor =
                            if (correctAnswerState.value == true && index == answerState.value){
                                Color.Green.copy(alpha = 0.75f)
                            }else{
                                Color.Red.copy(alpha = 0.75f)
                            }),
                        selected = (answerState.value == index),
                        onClick = {updateAnswer(index)},
                    )

                    val annotatedString = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Light,
                                color =
                                    if (correctAnswerState.value == true && index == answerState.value){
                                        Color.Green
                                    }else if (correctAnswerState.value == false && index == answerState.value){
                                        Color.Red
                                    }else{
                                        AppColors.mOffWhite
                                    },
                                fontSize = 17.sp)
                        ){
                            append(answerText)
                        }
                    }

                    Text(text = annotatedString)
                }
            }

            Button(
                modifier = Modifier
                    .width(225.dp)
                    .padding(15.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(34.dp),
                colors = ButtonDefaults.buttonColors(AppColors.mLightBlue),
                onClick = {onNextClicked(questionIndex.value)},
                enabled = correctAnswerState.value ?: false
            ) {
                Row (
                    modifier = Modifier.padding(2.dp),
                ){
                    Text(text = "Next",
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .align(Alignment.CenterVertically),
                        color = AppColors.mOffWhite,
                        fontSize = 17.sp)

                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Right Icon",
                        tint = Color.White
                    )
                }

            }
        }
    }
}

@Composable
fun QuestionTracker(counter: Int = 10, outOf: Int = 100){

    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)){
            withStyle(style = SpanStyle(color = AppColors.mLightGray,
                fontWeight = FontWeight.Bold,
                fontSize = 27.sp)){
                    append("Question $counter /")
                withStyle(style = SpanStyle(color = AppColors.mLightGray,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp)){
                    append("$outOf")
                }
            }
        }
    }, modifier = Modifier.padding(20.dp))
}

@Composable
fun DrawDottedLine(pathEffect: PathEffect){
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp),
    ) {
        drawLine(
            color = AppColors.mLightGray,
            start = Offset(0f,0f),
            end = Offset(size.width, y = 0f),
            pathEffect = pathEffect)
    }
}

@Composable
fun ShowProgress(score: Int){

    val gradient = Brush.linearGradient(listOf(
        Color(0xFFF95075),Color(0xFFBE6BE5)
    ))

    val progressFactor by remember(score) {
        mutableStateOf(score * 0.00005F)
    }

    Row (
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(45.dp)
            .border(width = 4.dp, brush = Brush.linearGradient(
                    colors = listOf(AppColors.mLightPurple,AppColors.mLightPurple)
                ), shape = RoundedCornerShape(34.dp))
            .clip(RoundedCornerShape(50.dp))
            .background(color = Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ){
        Button(
            onClick = {},
            contentPadding = PaddingValues(1.dp),
            modifier = Modifier
                .fillMaxWidth(progressFactor)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        ) {
            Text(text = (score*10).toString(),
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(23.dp))
                    .fillMaxHeight(0.87f)
                    .fillMaxWidth()
                    .padding(6.dp),
                color = AppColors.mOffWhite,
                textAlign = TextAlign.Center
            )

        }

    }
}