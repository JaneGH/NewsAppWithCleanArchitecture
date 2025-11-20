package com.example.newsappwithcleanarchitecture.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.newsappwithcleanarchitecture.domain.model.News
import com.example.newsappwithcleanarchitecture.domain.network.INetworkMonitor
import com.example.newsappwithcleanarchitecture.domain.usecase.FetchAndCacheNewsUseCase
import com.example.newsappwithcleanarchitecture.domain.usecase.GetLatestNewsUseCase
import com.example.newsappwithcleanarchitecture.presentation.state.NewsUiState
import com.example.newsappwithcleanarchitecture.presentation.viewmodel.NewsViewModel
import com.example.newsappwithcleanarchitecture.util.ResultState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class NewsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fetchAndCacheNewsUseCase = mockk<FetchAndCacheNewsUseCase>()
    private val getLatestNewsUseCase = mockk<GetLatestNewsUseCase>()
    private val networkMonitor = mockk<INetworkMonitor>()

    private lateinit var viewModel: NewsViewModel


//    private val successObserver = mockk<Observer<DogResponse>>(relaxUnitFun = true)
//    private val errorObserver = mockk<Observer<String>>(relaxed = true)
//    private val progressObserver = mockk<Observer<Boolean>>(relaxed = true)


    @Before
    fun setUp() {
        coEvery { networkMonitor.isConnectedFlow() } returns flowOf(true)

        coEvery { fetchAndCacheNewsUseCase() } returns flow {
            emit(ResultState.Loading)
            emit(ResultState.Success(Unit))
        }

        coEvery { getLatestNewsUseCase() } returns flow { emit(fakeNews) }

        viewModel = NewsViewModel(
            fetchAndCacheNewsUseCase,
            getLatestNewsUseCase,
            networkMonitor
        )
    }


    @Test
    fun `GIVEN network connected WHEN loadNews THEN fetchAndCache and latestNews called and state updated`() = runTest {
        val collectedStates = mutableListOf<NewsUiState>()

        val job = launch {
            viewModel.state.collect { collectedStates.add(it) }
        }

        advanceUntilIdle()

        val finalState = collectedStates.find {
            !it.isLoading && it.newsList.isNotEmpty() && it.errorMessage == null
            it.newsList.isNotEmpty()
        }

        assert(finalState != null) { "Final state not reached" }
        assert(finalState?.newsList == fakeNews)

        coVerify(exactly = 1) { fetchAndCacheNewsUseCase() }
        coVerify(exactly = 1) { getLatestNewsUseCase() }

        job.cancel()
    }

    @After
    fun tearDown() {

    }

    private companion object {
        const val ERROR_MESSAGE = "Error"
        val fakeNews = listOf(
            News(
                id = "1",
                title = "Title1",
                description = "News 1",
                image = "img1"
            ),
            News(
                id = "2",
                title = "Title2",
                description = "News 2",
                image = "img2"
            )
        )
    }
}
