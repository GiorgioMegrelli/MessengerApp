package ge.gmegrelishvili.messengerapp.model.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import ge.gmegrelishvili.messengerapp.model.entity.Message
import ge.gmegrelishvili.messengerapp.model.repository.strategy.MillisTimeStrategy
import ge.gmegrelishvili.messengerapp.model.repository.strategy.TimeStrategy

class FirebaseMessageRepository(
    private var timeStrategy: TimeStrategy = MillisTimeStrategy()
) : MessageRepository {

    /*
    * sorted(<key-1><key-2>): {
    *   {
    *       "from": <key-1>,
    *       "to": <key-2>,
    *       "timestamp": time,
    *       "value": message
    *   }
    * }
    */

    private val database = Firebase.database

    private fun mergeKeys(key0: String, key1: String): String {
        return listOf(key0, key1).sorted().joinToString("")
    }

    override fun insertMessage(
        fromUserId: String,
        toUserId: String,
        value: String,
        insertMessageResult: MessageRepository.Companion.InsertMessageResult
    ) {
        val mergedKeys = mergeKeys(fromUserId, toUserId)
        val childRef = database.getReference(ReferenceName).child(mergedKeys)
        childRef.push().key?.let { key ->
            val message = Message(fromUserId, toUserId, value, timeStrategy.getTimeValue())
            childRef.child(key).setValue(message)
            insertMessageResult.insertMessageFinished(message, null)
        }
    }

    override fun getMessages(
        userId0: String,
        userId1: String,
        getMessagesResult: MessageRepository.Companion.GetMessagesResult
    ) {
        val mergedKeys = mergeKeys(userId0, userId1)
        val childRef = database.getReference(ReferenceName).child(mergedKeys)
        childRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.getValue<HashMap<String, Message?>>()
                if (result == null) {
                    getMessagesResult.getMessagesFinished(null, Exception(UnknownExceptionString))
                    return
                }
                val list = ArrayList(result.values.filterNotNull())
                Comparator<Message> { m0, m1 ->
                    (m0.timestamp - m1.timestamp).toInt()
                }.let { comp -> list.sortedWith(comp) }
                getMessagesResult.getMessagesFinished(list.toList(), null)
            }

            override fun onCancelled(error: DatabaseError) {
                getMessagesResult.getMessagesFinished(null, error)
            }
        })
    }

    override fun getLastMessage(
        userId0: String,
        userId1: String,
        getLastMessageResult: MessageRepository.Companion.GetLastMessageResult
    ) {
        getMessages(userId0, userId1, object : MessageRepository.Companion.GetMessagesResult {
            override fun <T> getMessagesFinished(messages: List<Message>?, error: T?) {
                when {
                    error != null -> {
                        getLastMessageResult.getLastMessageFinished(null, error)
                    }
                    messages == null -> {
                        val e = Exception(UnknownExceptionString)
                        getLastMessageResult.getLastMessageFinished(null, e)
                    }
                    messages.isEmpty() -> {
                        getLastMessageResult.getLastMessageFinished(Message(), null)
                    }
                    else -> {
                        getLastMessageResult.getLastMessageFinished(messages.last(), null)
                    }
                }
            }
        })
    }

    override fun setTimeStrategy(timeStrategy: TimeStrategy) {
        this.timeStrategy = timeStrategy
    }

    companion object {
        private const val ReferenceName = "messages"

        private const val UnknownExceptionString = "Unknown Exception"
    }

}