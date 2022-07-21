package ge.gmegrelishvili.messengerapp.model.repository

import ge.gmegrelishvili.messengerapp.model.entity.Message
import ge.gmegrelishvili.messengerapp.model.repository.strategy.TimeStrategy

interface MessageRepository {

    fun insertMessage(
        fromUserId: String,
        toUserId: String,
        value: String,
        insertMessageResult: InsertMessageResult
    )

    fun getMessages(
        userId0: String,
        userId1: String,
        getMessagesResult: GetMessagesResult
    )

    fun getLastMessage(
        userId0: String,
        userId1: String,
        getLastMessageResult: GetLastMessageResult
    )

    fun setTimeStrategy(timeStrategy: TimeStrategy)

    companion object {
        interface InsertMessageResult {
            fun <T> insertMessageFinished(message: Message?, error: T?)
        }

        interface GetMessagesResult {
            fun <T> getMessagesFinished(messages: List<Message>?, error: T?)
        }

        interface GetLastMessageResult {
            fun <T> getLastMessageFinished(message: Message?, error: T?)
        }
    }

}