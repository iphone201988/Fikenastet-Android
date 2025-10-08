package com.fisken_astet.fikenastet.ui.chat
data class ChatMessage(
    val message: String? = null,
    val isUser: Boolean = false,
    val isProductCard: Boolean = false,
    val productTitle: String? = null,
    val productPrice: String? = null,
    val productImage: Int? = null // Use drawable resource ID
)
