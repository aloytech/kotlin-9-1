package ru.netology

fun main() {
    ChatService.newMessage(1, 2, "First message from 1 to 2")
    ChatService.newMessage(1, 2, "Second message from 1 to 2")
    ChatService.newMessage(1, 2, "3 message from 1 to 2")
    ChatService.newMessage(2, 1, "4 message from 2 to 1")
    ChatService.newMessage(2, 1, "5 message from 2 to 1")
    ChatService.newMessage(1, 3, "6 message from 1 to 3")
    ChatService.newMessage(3, 1, "7 message from 3 to 1")

    ChatService.printChats()
    println()
    ChatService.printMessages()
    println(ChatService.getUnreadChatsCount())
    println(ChatService.getChatMessages(0, 0, 10))
    println()
    ChatService.printMessages()
    println(ChatService.getUnreadChatsCount())
    //ChatService.readAll()
    ChatService.deleteMessage(6)
    ChatService.printMessages()
    ChatService.printChats()
    ChatService.deleteChat(0)
    ChatService.printMessages()
    ChatService.printChats()


}