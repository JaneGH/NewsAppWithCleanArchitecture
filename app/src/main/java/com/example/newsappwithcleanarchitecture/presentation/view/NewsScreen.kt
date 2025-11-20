package com.example.newsappwithcleanarchitecture.presentation.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.newsappwithcleanarchitecture.domain.model.News
import com.example.newsappwithcleanarchitecture.presentation.state.NewsIntent
import com.example.newsappwithcleanarchitecture.presentation.state.NewsUiState
import com.example.newsappwithcleanarchitecture.presentation.viewmodel.NewsViewModel
import com.example.newsappwithcleanarchitecture.ui.theme.LightGrayBackground

@SuppressLint("SuspiciousIndentation")
@Composable
fun NewsScreen(
    viewModel: NewsViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val filteredNews by viewModel.filteredNews.collectAsStateWithLifecycle()

    NewsContent(
        filteredNews = filteredNews,
        uiState = uiState,
        onRefreshClick = { viewModel.onIntent(NewsIntent.RefreshNews) },
        onSearchQueryChange = { viewModel.onIntent(NewsIntent.SearchNews(it)) }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsContent(
    filteredNews: List<News>,
    uiState: NewsUiState,
    onRefreshClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(6.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize()
                .statusBarsPadding()
        ) {

            Text(
                text = "Latest News",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChange,
                label = { Text("Search") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search icon")
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = LightGrayBackground,
                    unfocusedContainerColor = LightGrayBackground,
                    focusedBorderColor = LightGrayBackground,
                    unfocusedBorderColor = LightGrayBackground,
                ),
                modifier = Modifier
                    .testTag("searchField")
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onRefreshClick,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .testTag("refreshButton")
            ) {
                Text("Refresh News")
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading -> LoadingSection()
                uiState.errorMessage != null -> ErrorScreen(message = uiState.errorMessage)
                else -> NewsListSection(filteredNews)
            }
        }
    }
}

@Composable
private fun LoadingSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun NewsListSection(filteredNews: List<News>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("newsList")
    ) {
        items(filteredNews) { news ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .testTag("newsItem_${news.id}"),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {

                    AsyncImage(
                        model = news.image,
                        contentDescription = "News image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = news.title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = news.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
