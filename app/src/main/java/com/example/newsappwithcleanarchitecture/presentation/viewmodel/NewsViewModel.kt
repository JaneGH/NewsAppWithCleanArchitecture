package com.example.newsappwithcleanarchitecture.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsappwithcleanarchitecture.domain.model.News
import com.example.newsappwithcleanarchitecture.domain.usecase.FetchAndCacheNewsUseCase
import com.example.newsappwithcleanarchitecture.domain.usecase.GetLatestNewsUseCase
import com.example.newsappwithcleanarchitecture.presentation.state.NewsIntent
import com.example.newsappwithcleanarchitecture.presentation.state.NewsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val fetchAndCacheNewsUseCase: FetchAndCacheNewsUseCase,
    private val getLatestNewsUseCase: GetLatestNewsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NewsUiState(isLoading = true))
    val state: StateFlow<NewsUiState> = _state

    val filteredNews: List<News>
        get() = if (_state.value.searchQuery.isBlank()) {
            _state.value.newsList
        } else {
            _state.value.newsList.filter { news ->
                news.title.contains(_state.value.searchQuery, ignoreCase = true) ||
                        news.description.contains(_state.value.searchQuery, ignoreCase = true)
            }
        }

    init {
        fetchNews()
        observeLatestNews()
    }

    fun onIntent(action: NewsIntent) {
        when (action) {
            NewsIntent.LoadNews  -> observeLatestNews()
            NewsIntent.RefreshNews -> observeLatestNews()
            is NewsIntent.SearchNews -> {
                _state.value = _state.value.copy(searchQuery = action.query)
            }
        }
    }

    private fun fetchNews() = viewModelScope.launch {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        try {
            fetchAndCacheNewsUseCase()
            _state.value = _state.value.copy(isLoading = false)
        } catch (e: Exception) {
            _state.value = _state.value.copy(isLoading = false, errorMessage = e.message)
        }
    }

    private fun observeLatestNews() = viewModelScope.launch {
        getLatestNewsUseCase().collectLatest { newsList ->
            _state.value = _state.value.copy(newsList = newsList)
        }
    }
}