package com.example.fikenastet.data.model

import androidx.annotation.DrawableRes

class DummyApiResponseModel : ArrayList<DummyApiItem>()

data class DummyApiItem(
    val id: Int, val imdbId: String, val posterURL: String, val title: String
)

data class DummyHomeCard(
    val id: Int, val tite: String, @DrawableRes val image: Int
)

data class DummySocialFeed(
    val username: String
)

data class DummyComment(
    val message: String

)

data class DummyAllLake(
    val name: String
)

data class SellDataList(
    val name: String, var isSelected: Boolean = false
)

data class ReplyModel(val title: String?, var isSelected: Boolean? = false)