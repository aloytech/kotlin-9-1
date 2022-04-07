package ru.netology

class ChatNotExistException(override val message: String?) : RuntimeException(message)
class MessageNotExistException(override val message: String?) : RuntimeException(message)