package com.example.newsappwithcleanarchitecture.di

import android.app.Application
import androidx.room.Room
import com.example.newsappwithcleanarchitecture.data.local.AppDatabase
import com.example.newsappwithcleanarchitecture.data.local.NewsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "NewsDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(db: AppDatabase): NewsDao = db.newsDao()
}
