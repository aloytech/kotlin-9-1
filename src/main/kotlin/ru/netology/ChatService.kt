package ru.netology

object ChatService {
    private var users = mutableListOf<User>()
    private var messages = mutableListOf<Message>()
    private var chatsMap = mutableMapOf<Int, Chat>()
    private var currentChatId = 0
    private var currentMessageId = 0

    fun newMessage(fromId: Int, toId: Int, text: String) {

        val filteredChat = chatsMap
            .filterValues { it.firstId == fromId || it.secondId == fromId }
            .filterValues { it.firstId == toId || it.secondId == toId }
        var thisChatId = currentChatId
        if (filteredChat.isEmpty()) {
            chatsMap[thisChatId] = Chat(currentChatId, fromId, toId)
            currentChatId++
        } else {
            thisChatId = filteredChat.keys.first()
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
        val index = messages
            .filter { it.mid == mid }
            .ifEmpty() {
                return null
            }
            .let { messages.indexOf(it[0]) }
        return index
    }

    fun getUnreadChatsCount(): Int {
        val unreadChats = chatsMap.filterValues { !it.isRead }
        return unreadChats.size
    }

    fun getChatMessages(cid: Int, offset: Int = 0, count: Int): List<Message> {
        if (chatsMap.containsKey(cid)) {
            val messageList = messages
                .filter { it.chatId == cid }
                .let {
                    var lastIndex = it.size
                    if (offset + count < it.size) {
                        lastIndex = offset + count
                    }
                    it.subList(offset, lastIndex)
                }
            messages.removeAll(messageList)
            var readMessages = messageList.map { readMsg(it) }
            messages.addAll(readMessages)
            messages.filter { it.chatId == cid && !it.isRead }
                .ifEmpty() {
                    chatsMap[cid]?.isRead = true
                }
            return readMessages
        } else {
            throw ChatNotExistException("Chat with $cid not exists")
        }
    }

    private fun readMsg(message: Message): Message {
        message.isRead = true
        return message
    }

    fun deleteMessage(mid: Int) {
        val message = messages
            .filter { it.mid == mid }
            .ifEmpty { throw MessageNotExistException("Message with $mid not exists") }
        val messageChatId = message[0].chatId
        messages.removeAll(message)
        if (messages.none { it.chatId == messageChatId }) {
            chatsMap.remove(messageChatId)
        }
    }

    fun deleteChat(cid: Int) {
        chatsMap
            .filterValues { it.chatId == cid }
            .ifEmpty() {
                throw ChatNotExistException("Chat with $cid not exists")
            }
        messages.removeAll { m: Message -> m.chatId == cid }
        chatsMap.remove(cid)
    }

    fun readMsgById(mid: Int) {
        val msgIndex = getIndexById(mid) ?: throw MessageNotExistException("Message with $mid not exists")
        messages[msgIndex].isRead = true
        val chatId = messages[msgIndex].chatId
        messages
            .filter { it.chatId == chatId && !it.isRead }
            .ifEmpty() {
                chatsMap[chatId]?.isRead = true
            }
    }

    fun printMessages() {
        println(messages)
    }

    fun printChats() {
        println(chatsMap)
    }

    fun clear() {
        messages.clear()
        chatsMap.clear()
        users.clear()
        currentChatId = 0
        currentMessageId = 0
    }

    fun messageCount(): Int {
        return messages.size
    }
}