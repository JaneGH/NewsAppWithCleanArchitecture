package com.example.newsappwithcleanarchitecture.util

sealed class ResultState<out T> {
    object Loading : ResultState<Nothing>()
    data class Success<out T>(val body: T) : ResultState<T>()
    data class Error(val errorMessage: String) : ResultState<Nothing>()
}