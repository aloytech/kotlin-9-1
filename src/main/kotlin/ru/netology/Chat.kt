package ru.netology

data class Chat(
    val chatId: Int,
    val firstId: Int,
    val secondId: Int,
    var isRead: Boolean = false
)