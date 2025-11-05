package com.example.newsappwithcleanarchitecture.di

import com.example.newsappwithcleanarchitecture.data.network.NetworkMonitor
import com.example.newsappwithcleanarchitecture.domain.network.INetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindsModule {

    @Binds
    @Singleton
    abstract fun bindNetworkMonitor(
        networkMonitor: NetworkMonitor
    ): INetworkMonitor
}