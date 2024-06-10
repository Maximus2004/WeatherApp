package com.example.weatherapp.di

import com.example.weatherapp.network.CitiesApiService
import com.example.weatherapp.network.WeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.brotli.BrotliInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideCitiesApiService(): CitiesApiService {
        val BASE_URL = "https://gist.githubusercontent.com/Stronger197/"

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(BrotliInterceptor)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()

        val retrofitService: CitiesApiService by lazy {
            retrofit.create(CitiesApiService::class.java)
        }

        return retrofitService
    }

    @Provides
    @Singleton
    fun provideWeatherApiService(): WeatherApiService {
        val BASE_URL = "https://api.openweathermap.org/"

        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService: WeatherApiService by lazy {
            retrofit.create(WeatherApiService::class.java)
        }

        return retrofitService
    }
}