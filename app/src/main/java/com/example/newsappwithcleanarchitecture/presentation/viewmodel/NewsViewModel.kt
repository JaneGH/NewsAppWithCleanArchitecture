package com.example.newsappwithcleanarchitecture.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsappwithcleanarchitecture.domain.model.News
import com.example.newsappwithcleanarchitecture.domain.network.INetworkMonitor
import com.example.newsappwithcleanarchitecture.domain.usecase.FetchAndCacheNewsUseCase
import com.example.newsappwithcleanarchitecture.domain.usecase.GetLatestNewsUseCase
import com.example.newsappwithcleanarchitecture.presentation.state.NewsIntent
import com.example.newsappwithcleanarchitecture.presentation.state.NewsUiState
import com.example.newsappwithcleanarchitecture.util.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val fetchAndCacheNewsUseCase: FetchAndCacheNewsUseCase,
    private val getLatestNewsUseCase: GetLatestNewsUseCase,
    private val networkMonitor: INetworkMonitor
) : ViewModel() {

    private val _state = MutableStateFlow(NewsUiState(isLoading = true))
    val state: StateFlow<NewsUiState> = _state

    private val _filteredNews = MutableStateFlow<List<News>>(emptyList())
    val filteredNews: StateFlow<List<News>> = _filteredNews

    init {
        loadNews()
    }

    fun onIntent(intent: NewsIntent) {
        when (intent) {
            NewsIntent.LoadNews,
            NewsIntent.RefreshNews -> loadNews()

            is NewsIntent.SearchNews -> {
                _state.update { it.copy(searchQuery = intent.query) }
                applySearch(intent.query)
            }
        }
    }

    private fun loadNews() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, errorMessage = null) }

        networkMonitor.isConnectedFlow().take(1).collect { isConnected ->
            if (isConnected) {
                fetchFromRemote()
            } else {
                observeLatestNews()
            }
        }
    }

    private suspend fun fetchFromRemote() {
        fetchAndCacheNewsUseCase().collect { result ->
            when (result) {
                is ResultState.Loading ->
                    _state.update { it.copy(isLoading = true) }

                is ResultState.Success ->
                    observeLatestNews()

                is ResultState.Error ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = result.errorMessage)
                    }
            }
        }
    }

    private fun observeLatestNews() {
        viewModelScope.launch {
            try {
                getLatestNewsUseCase().collectLatest { list ->
                    _state.update {
                        it.copy(
                            newsList = list,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    _filteredNews.value = list
                    if (_state.value.searchQuery.isNotBlank()) {
                        applySearch(_state.value.searchQuery)
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        errorMessage = e.message ?: "Unexpected error",
                        newsList = emptyList(),
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun applySearch(query: String) {
        val originalList = _state.value.newsList

        _filteredNews.value =
            if (query.isBlank()) {
                originalList
            } else {
                originalList.filter { news ->
                    news.title.contains(query, ignoreCase = true) ||
                            news.description.contains(query, ignoreCase = true)
                }
            }
    }
}
