package com.example.newsappwithcleanarchitecture

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.newsappwithcleanarchitecture.domain.model.News
import com.example.newsappwithcleanarchitecture.presentation.state.NewsUiState
import com.example.newsappwithcleanarchitecture.presentation.view.NewsContent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LatestNewsUiTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val fakeState = NewsUiState(
        isLoading = false,
        searchQuery = "",
        errorMessage = null
    )

    private val fakeNews = listOf(
        News("1", "Fake Title 1", "Fake Description 1", "img1"),
        News("2", "Fake Title 2", "Fake Description 2", "img2")
    )

    @Test
    fun latestNewsScreen_isDisplayed() {
        composeRule.setContent {
            NewsContent(
                filteredNews = fakeNews,
                uiState = fakeState,
                onRefreshClick = {},
                onSearchQueryChange = {}
            )
        }

        composeRule.onNodeWithTag("searchField").assertExists()
        composeRule.onNodeWithTag("refreshButton").assertExists()
        composeRule.onNodeWithTag("newsList").assertExists()
    }

    @Test
    fun news_isDisplayed() {
        composeRule.setContent {
            NewsContent(
                filteredNews = fakeNews,
                uiState = fakeState,
                onRefreshClick = {},
                onSearchQueryChange = {}
            )
        }

        composeRule.waitUntil(3_000) {
            composeRule.onAllNodesWithTag("newsItem_1")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("newsItem_1").assertExists()
        composeRule.onNodeWithTag("newsItem_2").assertExists()
    }

    @Test
    fun news_search_isWorked() {

        val currentQuery = mutableStateOf("")
        val currentFiltered = mutableStateOf(fakeNews)

        composeRule.setContent {
            NewsContent(
                filteredNews = currentFiltered.value,
                uiState = fakeState.copy(searchQuery = currentQuery.value),
                onRefreshClick = {},
                onSearchQueryChange = { query ->
                    currentQuery.value = query
                    currentFiltered.value = fakeNews.filter {
                        it.title.contains(query, ignoreCase = true) ||
                                it.description.contains(query, ignoreCase = true)
                    }
                }
            )
        }

        composeRule.onNodeWithTag("searchField").performTextInput("1")
        composeRule.waitUntil(3000) {
            composeRule.onAllNodesWithTag("newsItem_1").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("newsItem_1").assertExists()
        composeRule.onNodeWithTag("newsItem_2").assertDoesNotExist()
    }

}