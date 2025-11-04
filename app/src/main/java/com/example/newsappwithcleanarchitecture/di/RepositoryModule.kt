package com.example.newsappwithcleanarchitecture.di

import com.example.newsappwithcleanarchitecture.data.repository.NewsRepositoryImpl
import com.example.newsappwithcleanarchitecture.domain.repository.INewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        newsRepository: NewsRepositoryImpl
    ): INewsRepository
}