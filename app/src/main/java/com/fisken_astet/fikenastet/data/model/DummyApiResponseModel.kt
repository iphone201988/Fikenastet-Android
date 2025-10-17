package com.fisken_astet.fikenastet.data.model

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

data class DummyPermitData(
    val name: String,var isSelected:Boolean=false
)

data class SellDataList(
    val name: String, var isSelected: Boolean = false
)

data class ReplyModel(val title: String?, var isSelected: Boolean? = false)

sealed class ThreadListItem {
    data class Header(val title: String) : ThreadListItem()
    data class ThreadData(val data: ThreadItemData) : ThreadListItem()
}

data class ThreadItemData( // your original model
    val header: String,
    val title: String,
    val author: String,
    val replies: Int,
    val timeAgo: String,
    val postedByMe:Boolean=false
)

data class MyLakesModel(val name :String?,val status : String?,val permitSold:String?,val revenue:String?)

sealed class NotificationsItem {
    data class NotificationDate(val date :String) : NotificationsItem()
    data class NotificationData(val notification: NotificationModelData) : NotificationsItem()
}

data class NotificationModelData( val name :String?,val title:String?,val createdAt:String?)

data class PermissionsModel(val permissionName:String?,var isGranted:Boolean?=false)