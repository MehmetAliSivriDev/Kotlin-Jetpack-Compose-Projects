package com.example.triviaapp.repository

import android.util.Log
import com.example.triviaapp.data.DataOrException
import com.example.triviaapp.model.Question
import com.example.triviaapp.model.QuestionItem
import com.example.triviaapp.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionApi) {

    private val dataOrException = DataOrException<ArrayList<QuestionItem>,Boolean,Exception>()

    suspend fun getAllQuestions() : DataOrException<ArrayList<QuestionItem>, Boolean, Exception>{
        try {

            dataOrException.loading = true
            dataOrException.data = api.getAllQuestions()

            if(dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false


        }catch (excpetion: Exception){
            dataOrException.e = excpetion
            Log.d("Exc","getAllQuestions: ${dataOrException.e?.localizedMessage}")
        }

        return dataOrException
    }

}