package com.example.newsappwithcleanarchitecture.data.repository

import android.util.Log
import com.example.newsappwithcleanarchitecture.data.mapper.NewsMapper
import com.example.newsappwithcleanarchitecture.data.remote.ApiService
import com.example.newsappwithcleanarchitecture.domain.model.News
import com.example.newsappwithcleanarchitecture.util.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsRemoteRepository @Inject constructor(
    private val api: ApiService
) {

    fun fetchNews(): Flow<ResultState<List<News>>> = flow {
        emit(ResultState.Loading)
        try {
            val response = api.getAllNews()
            val newsList = NewsMapper.mapDtoListToDomainList(response.news)
            emit(ResultState.Success(newsList))
        } catch (e: IOException) {
            Log.e("NewsRemoteRepository", "No internet", e)
            emit(ResultState.Error("No internet connection"))
        } catch (e: HttpException) {
            Log.e("NewsRemoteRepository", "Server error", e)
            emit(ResultState.Error("Server error: ${e.code()}"))
        } catch (e: Exception) {
            Log.e("NewsRemoteRepository", "Unknown error", e)
            emit(ResultState.Error(e.localizedMessage ?: "Unknown error"))
        }
    }
}