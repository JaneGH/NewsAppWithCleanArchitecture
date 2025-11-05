package com.example.newsappwithcleanarchitecture.domain.network

import kotlinx.coroutines.flow.Flow

interface INetworkMonitor {
    fun isConnectedFlow(): Flow<Boolean>
}