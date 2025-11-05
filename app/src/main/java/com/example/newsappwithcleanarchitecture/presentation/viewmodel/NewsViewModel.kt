package com.example.newsappwithcleanarchitecture.presentation.viewmodel

import android.util.Log
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
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
        loadNews()
    }

    fun onIntent(action: NewsIntent) {
        when (action) {
            NewsIntent.LoadNews,
            NewsIntent.RefreshNews -> loadNews()
            is NewsIntent.SearchNews -> {
                _state.value = _state.value.copy(searchQuery = action.query)
            }
        }
    }

    private fun loadNews() = viewModelScope.launch {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)

        networkMonitor.isConnectedFlow().collectLatest {
            isConnected ->
            Log.i ("isConnected", "$isConnected")
            if (isConnected) {
                fetchAndCacheNewsUseCase().collect { result ->
                    when (result) {
                        is ResultState.Loading -> _state.value = _state.value.copy(isLoading = true)
                        is ResultState.Success -> _state.value =
                            _state.value.copy(isLoading = false)

                        is ResultState.Error -> _state.value = _state.value.copy(
                            isLoading = false,
                            errorMessage = result.errorMessage
                        )
                    }
                }
            } else {
                observeLatestNews()
            }
        }


    }

    private fun observeLatestNews() {
        viewModelScope.launch {
            try {
                getLatestNewsUseCase().collectLatest { newsList ->
                    _state.value = _state.value.copy(
                        newsList = newsList,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = e.message ?: "An unexpected error occurred",
                    newsList = emptyList(),
                    isLoading = false
                )
            }
        }
    }
}