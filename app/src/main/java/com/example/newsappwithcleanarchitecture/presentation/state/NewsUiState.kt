package com.example.newsappwithcleanarchitecture.presentation.state

import com.example.newsappwithcleanarchitecture.domain.model.News

data class NewsUiState(
    val isLoading: Boolean = false,
    val newsList: List<News> = emptyList(),
    val errorMessage: String? = null,
    val searchQuery: String = "",
)