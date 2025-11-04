package com.example.newsappwithcleanarchitecture.domain.model

data class News (
    val id: String,
    val title: String,
    val description: String,
    val image: String?,
)
