package ge.gmegrelishvili.messengerapp.model.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val key: String = "",
    val username: String = "",
    val whatIDo: String = "",
) : SerializableToMap {

    override fun toMap(): Map<String, Any?> {
        return mapOf(
            "key" to key,
            "username" to username,
            "whatIDo" to whatIDo,
        )
    }

}