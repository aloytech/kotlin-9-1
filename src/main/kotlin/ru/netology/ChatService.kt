package ru.netology

object ChatService {
    private var users = mutableListOf<User>()
    private var messages = mutableListOf<Message>()
    private var chatsMap = mutableMapOf<Int, Chat>()
    private var currentChatId = 0
    private var currentMessageId = 0

    fun newMessage(fromId: Int, toId: Int, text: String) {

        val firstFilterChat = chatsMap.filterValues { it.firstId == fromId || it.secondId == fromId }
        val fullFilterChat = firstFilterChat.filterValues { it.firstId == toId || it.secondId == toId }
        var thisChatId = currentChatId
        if (fullFilterChat.isEmpty()) {
            chatsMap[thisChatId] = Chat(currentChatId, fromId, toId)
            currentChatId++
        } else {
            thisChatId = fullFilterChat.keys.first()
            chatsMap[thisChatId]?.isRead = false
        }
        messages.add(
            Message(
                currentMessageId,
                fromId,
                toId,
                thisChatId,
                text,
                date = System.currentTimeMillis().toInt()
            )
        )
        currentMessageId++
    }

    private fun getIndexById(mid: Int): Int? {
        val messageList = messages.filter { it.mid == mid }
        if (messageList.isEmpty()) {
            return null
        }
        return messages.indexOf(messageList[0])
    }

    fun getUnreadChatsCount(): Int {
        val unreadChats = chatsMap.filterValues { !it.isRead }
        return unreadChats.size
    }

    fun getChatMessages(cid: Int, offset: Int = 0, count: Int): List<Message> {
        if (chatsMap.containsKey(cid)) {
            val messageList = messages.filter { it.chatId == cid }
            var lastIndex = messageList.size
            if (offset + count < messageList.size) {
                lastIndex = offset + count
            }
            val outMessageList = messageList.subList(offset, lastIndex)
            messages.removeAll(outMessageList)
            var unreadMessages = outMessageList.filter { !it.isRead }
            val readMessages = unreadMessages.map { readMsg(it) }
            messages.addAll(readMessages)
            unreadMessages = messages.filter { it.chatId == cid && !it.isRead }
            if (unreadMessages.isEmpty()) {
                chatsMap[cid]?.isRead = true
            }
            return outMessageList
        } else {
            throw ChatNotExistException("Chat with $cid not exists")
        }
    }

    private fun readMsg(message: Message): Message {
        message.isRead = true
        return message
    }

    fun deleteMessage(mid: Int) {
        val message = messages.filter { it.mid == mid }
        if (message.isEmpty()) {
            throw MessageNotExistException("Message with $mid not exists")
        }
        val messageChatId = message[0].chatId
        messages.removeAll(message)
        if (messages.none { it.chatId == messageChatId }) {
            chatsMap.remove(messageChatId)
        }
    }

    fun deleteChat(cid: Int) {
        val chat = chatsMap.filterValues { it.chatId == cid }
        if (chat.isEmpty()) {
            throw ChatNotExistException("Chat with $cid not exists")
        }
        messages.removeAll { m: Message -> m.chatId == cid }
        chatsMap.remove(cid)
    }

    fun readMsgById(mid: Int) {
        val msgIndex = getIndexById(mid) ?: throw MessageNotExistException("Message with $mid not exists")
        messages[msgIndex].isRead = true
        val chatId = messages[msgIndex].chatId
        val unreadMessages = messages.filter { it.chatId == chatId && !it.isRead }
        if (unreadMessages.isEmpty()) {
            chatsMap[chatId]?.isRead = true
        }
    }

    fun printMessages() {
        println(messages)
    }

    fun printChats() {
        println(chatsMap)
    }

    fun clear(){
        messages.clear()
        chatsMap.clear()
        users.clear()
        currentChatId = 0
        currentMessageId = 0
    }

    fun messageCount():Int{
        return messages.size
    }
}