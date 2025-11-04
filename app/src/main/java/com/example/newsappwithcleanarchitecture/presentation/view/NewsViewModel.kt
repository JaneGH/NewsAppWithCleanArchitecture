package com.example.newsappwithcleanarchitecture.presentation.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsappwithcleanarchitecture.domain.model.News
import com.example.newsappwithcleanarchitecture.domain.repository.INewsRepository
import com.example.newsappwithcleanarchitecture.util.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: INewsRepository
) : ViewModel() {

    private val _newsState = MutableStateFlow<ResultState<List<News>>>(ResultState.Loading)
    val newsState: StateFlow<ResultState<List<News>>> = _newsState

    init {
        fetchNews()
    }

    fun fetchNews() {
        viewModelScope.launch {
            _newsState.value = ResultState.Loading
            try {
                repository.fetchAndCacheNews()

                repository.getLatestNews().collect { newsList ->
                    _newsState.value = ResultState.Success(newsList)
                }
            } catch (e: Exception) {
                _newsState.value = ResultState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}
