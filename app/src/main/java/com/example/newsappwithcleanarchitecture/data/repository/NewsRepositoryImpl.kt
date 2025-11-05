package com.example.newsappwithcleanarchitecture.data.repository

import com.example.newsappwithcleanarchitecture.domain.model.News
import com.example.newsappwithcleanarchitecture.domain.repository.INewsRepository
import com.example.newsappwithcleanarchitecture.util.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val local: NewsLocalRepository,
    private val remote: NewsRemoteRepository
) : INewsRepository {

    override fun getLatestNews(): Flow<List<News>> = local.getAllNews()

    override suspend fun fetchAndCacheNews(): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)
        try {
            remote.fetchNews().collect { result ->
                when (result) {
                    is ResultState.Loading -> emit(ResultState.Loading)
                    is ResultState.Success -> {
                        local.saveNews(result.body)
                        emit(ResultState.Success(Unit))
                    }
                    is ResultState.Error -> emit(ResultState.Error(result.errorMessage))
                }
            }
        } catch (e: IOException) {
            emit(ResultState.Error("No internet connection"))
        } catch (e: HttpException) {
            emit(ResultState.Error("Server error: ${e.code()}"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.localizedMessage ?: "Unknown error"))
        }
    }
}
