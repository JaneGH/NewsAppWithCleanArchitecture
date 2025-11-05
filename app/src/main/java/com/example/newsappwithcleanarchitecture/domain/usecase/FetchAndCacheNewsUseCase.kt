package com.example.newsappwithcleanarchitecture.domain.usecase

import com.example.newsappwithcleanarchitecture.domain.repository.INewsRepository
import javax.inject.Inject

class FetchAndCacheNewsUseCase @Inject constructor(private val repository: INewsRepository) {
    suspend operator fun invoke() = repository.fetchAndCacheNews()
}