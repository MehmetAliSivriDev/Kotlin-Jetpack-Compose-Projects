package com.example.weatherforecastapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecastapp.data.WeatherDao
import com.example.weatherforecastapp.data.WeatherDatabase
import com.example.weatherforecastapp.network.WeatherAPI
import com.example.weatherforecastapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

     @Provides
     @Singleton
     fun provideOpenWeatherAPI(): WeatherAPI{
         return Retrofit.Builder()
             .baseUrl(Constants.BASE_URL)
             .addConverterFactory(GsonConverterFactory.create())
             .build()
             .create(WeatherAPI::class.java)
     }

    @Provides
    @Singleton
    fun provideWeatherDao(weatherDatabase: WeatherDatabase): WeatherDao = weatherDatabase.weatherDao()

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase{
        return Room.databaseBuilder(context,WeatherDatabase::class.java,"weather_database")
            .fallbackToDestructiveMigration(true)
            .build()
    }
}