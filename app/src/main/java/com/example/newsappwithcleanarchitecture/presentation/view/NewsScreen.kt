package com.example.newsappwithcleanarchitecture.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.newsappwithcleanarchitecture.domain.model.News
import com.example.newsappwithcleanarchitecture.presentation.state.NewsIntent
import com.example.newsappwithcleanarchitecture.presentation.state.NewsUiState
import com.example.newsappwithcleanarchitecture.presentation.viewmodel.NewsViewModel
import com.example.newsappwithcleanarchitecture.ui.theme.LightGrayBackground


@Composable
fun NewsScreen() {
    val newsViewModel: NewsViewModel = hiltViewModel()
    val uiState by newsViewModel.state.collectAsStateWithLifecycle()

        NewsContent(
            filteredNews = newsViewModel.filteredNews,
            uiState = uiState,
            onRefreshClick = { newsViewModel.onIntent(NewsIntent.RefreshNews) },
            onSearchQueryChange = { query -> newsViewModel.onIntent(NewsIntent.SearchNews(query)) }
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
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .padding(6.dp)
            .wrapContentSize()
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
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search icon"
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = LightGrayBackground,
                    unfocusedContainerColor = LightGrayBackground,
                    focusedBorderColor = LightGrayBackground,
                    unfocusedBorderColor = LightGrayBackground,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onRefreshClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Refresh News")
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.errorMessage != null -> {
                    ErrorScreen(
                        message =  uiState.errorMessage ?: "Unknown error",
                    )
                }

                filteredNews.isNotEmpty() -> {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(filteredNews) { news ->
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
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


            }



        }
    }
}