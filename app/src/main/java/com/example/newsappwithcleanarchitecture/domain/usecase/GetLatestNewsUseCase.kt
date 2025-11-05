package com.example.newsappwithcleanarchitecture.domain.usecase

import com.example.newsappwithcleanarchitecture.domain.model.News
import com.example.newsappwithcleanarchitecture.domain.repository.INewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLatestNewsUseCase @Inject constructor(private val repository: INewsRepository) {
    operator fun invoke(): Flow<List<News>> = repository.getLatestNews()
}
