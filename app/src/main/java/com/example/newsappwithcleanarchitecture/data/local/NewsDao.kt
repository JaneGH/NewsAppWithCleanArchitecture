package com.example.newsappwithcleanarchitecture.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNews(news: List<NewsEntity>)

    @Query("SELECT * FROM news")
    fun getAllNews(): LiveData<List<NewsEntity>>

    @Query("SELECT * FROM news")
    fun getAllNewsFlow(): Flow<List<NewsEntity>>

    @Query("DELETE FROM news")
    suspend fun clearNews()
}