package ru.netology

data class Message(
    val mid: Int,
    val fromId: Int,
    val toId: Int,
    val chatId: Int,
    val text: String,
    var isRead: Boolean = false,
    val date: Int
) {
}