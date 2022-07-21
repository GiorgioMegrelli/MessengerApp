package ge.gmegrelishvili.messengerapp.model.entity

data class Message(
    val fromUserId: String = EmptyId,
    val toUserId: String = EmptyId,
    val value: String = "",
    val timestamp: Long = EmptyTimestamp
) : SerializableToMap {

    override fun toMap(): Map<String, Any?> {
        return mapOf(
            "fromUserId" to fromUserId,
            "toUserId" to toUserId,
            "value" to value,
            "timestamp" to timestamp
        )
    }

    fun isEmpty(): Boolean {
        return fromUserId == EmptyId || toUserId == EmptyId || timestamp == EmptyTimestamp
    }

    companion object {
        const val EmptyId = ""
        const val EmptyTimestamp = 0L
    }

}
