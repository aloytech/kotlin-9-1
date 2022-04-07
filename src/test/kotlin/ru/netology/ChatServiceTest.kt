package ru.netology

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ChatServiceTest {

    @Before
    fun prepare(){
        ChatService.clear()
        ChatService.newMessage(1, 2, "First message from 1 to 2")
        ChatService.newMessage(1, 2, "Second message from 1 to 2")
        ChatService.newMessage(1, 2, "3 message from 1 to 2")
        ChatService.newMessage(2, 1, "4 message from 2 to 1")
        ChatService.newMessage(2, 1, "5 message from 2 to 1")
        ChatService.newMessage(1, 3, "6 message from 1 to 3")
        ChatService.newMessage(3, 1, "7 message from 3 to 1")
    }

    @Test
    fun newMessage() {
        ChatService.newMessage(3, 1, "8 message from 3 to 1")
    }

    @Test
    fun getUnreadChatsCount() {
        val expected = 2
        val actual = ChatService.getUnreadChatsCount()
        assertEquals(expected,actual)
    }

    @Test (expected = ChatNotExistException::class)
    fun getChatMessagesNotExist() {
        ChatService.getChatMessages(10, 0, 10)
    }

    @Test
    fun getChatMessagesAll() {
        val expected = 5
        val actual = ChatService.getChatMessages(0, 0, 10).size
        assertEquals(expected,actual)
    }
    @Test
    fun getChatMessagesTwo() {
        val expected = 2
        val actual = ChatService.getChatMessages(0, 0, 2).size
        assertEquals(expected,actual)
    }
    @Test
    fun getChatMessagesOneFrom() {
        val expected = 1
        val actual = ChatService.getChatMessages(0, 4, 2).size
        assertEquals(expected,actual)
    }

    @Test (expected = MessageNotExistException::class)
    fun deleteMessageNotExist() {
        ChatService.deleteMessage(20)
    }

    @Test
    fun deleteMessageNotLast() {
        val expected = 6
        ChatService.deleteMessage(2)
        val actual = ChatService.messageCount()
        assertEquals(expected,actual)
    }
    @Test
    fun deleteMessageLast() {
        val expected = 1
        ChatService.deleteMessage(5)
        ChatService.deleteMessage(6)
        val actual = ChatService.getUnreadChatsCount()
        assertEquals(expected,actual)
    }

    @Test (expected = ChatNotExistException::class)
    fun deleteChatNotExist() {
        ChatService.deleteChat(20)
    }

    @Test
    fun deleteChatOne() {
        val expected = 1
        ChatService.deleteChat(0)
        val actual = ChatService.getUnreadChatsCount()
        assertEquals(expected,actual)
    }
}