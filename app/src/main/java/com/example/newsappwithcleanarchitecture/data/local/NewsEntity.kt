package com.example.newsappwithcleanarchitecture.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "news")
data class NewsEntity(
    @SerializedName("id")
    @PrimaryKey
    val id: String,

    @SerializedName("title")
    val title: String = "",

    @SerializedName("description")
    val description: String = "",

//    @SerializedName("url")
//    val url: String,

//    @SerializedName("author")
//    val author: String?,

    @SerializedName("image")
    val image: String? = null

//    @SerializedName("language")
//    val language: String,

//    @SerializedName("category")
//    val category: List<String>,

//    @SerializedName("published")
//    val published: String
)
