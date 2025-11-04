package com.example.newsappwithcleanarchitecture.domain.usecase

import com.example.newsappwithcleanarchitecture.domain.repository.INewsRepository

class FetchAndCacheNewsUseCase(private val repository: INewsRepository) {
    suspend operator fun invoke() = repository.fetchAndCacheNews()
}