package com.example.triviaapp.network

import com.example.triviaapp.model.Question
import retrofit2.http.GET

interface QuestionApi {

    @GET("world.json")
    suspend fun getAllQuestions() : Question

}