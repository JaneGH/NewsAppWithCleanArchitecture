package com.example.newsappwithcleanarchitecture.di

import com.example.newsappwithcleanarchitecture.data.remote.APIKeyInterceptor
import com.example.newsappwithcleanarchitecture.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoginInterceptor() : HttpLoggingInterceptor{
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loginInterceptor : HttpLoggingInterceptor,
        apiClientInterceptor : APIKeyInterceptor
    ) : OkHttpClient {
        val okHttpclient = OkHttpClient.Builder()
            .addInterceptor(loginInterceptor)
            .addInterceptor(apiClientInterceptor)
            .build()
        return  okHttpclient
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.currentsapi.services/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

}