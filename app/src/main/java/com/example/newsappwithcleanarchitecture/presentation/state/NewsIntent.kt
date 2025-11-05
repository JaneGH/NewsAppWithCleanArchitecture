package com.example.newsappwithcleanarchitecture.presentation.state

sealed class NewsIntent {
    object LoadNews : NewsIntent()
    object RefreshNews : NewsIntent()
    data class SearchNews(val query: String) : NewsIntent()
}