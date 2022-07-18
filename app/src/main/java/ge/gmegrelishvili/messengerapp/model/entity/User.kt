package ge.gmegrelishvili.messengerapp.model.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val username: String = "",
    val whatIDo: String = "",
    val profileImage: String? = null
) : SerializableToMap {

    override fun toMap(): Map<String, Any?> {
        return mapOf(
            "username" to username,
            "whatIDo" to whatIDo,
            "profileImage" to profileImage
        )
    }

}