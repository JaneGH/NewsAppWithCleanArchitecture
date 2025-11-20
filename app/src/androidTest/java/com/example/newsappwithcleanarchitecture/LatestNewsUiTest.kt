package com.example.newsappwithcleanarchitecture

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
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
            composeRule.onAllNodesWithText("Fake Title 1")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("Fake Title 1").assertExists()
        composeRule.onNodeWithText("Fake Description 1").assertExists()
        composeRule.onNodeWithText("Fake Title 2").assertExists()
        composeRule.onNodeWithText("Fake Description 2").assertExists()
    }
}