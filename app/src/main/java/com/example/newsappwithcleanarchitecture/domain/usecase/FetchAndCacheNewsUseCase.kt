package com.example.newsappwithcleanarchitecture.domain.usecase

import com.example.newsappwithcleanarchitecture.domain.repository.INewsRepository
import com.example.newsappwithcleanarchitecture.util.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchAndCacheNewsUseCase @Inject constructor (private val repository: INewsRepository) {
    suspend operator fun invoke(): Flow<ResultState<Unit>> = repository.fetchAndCacheNews()
}