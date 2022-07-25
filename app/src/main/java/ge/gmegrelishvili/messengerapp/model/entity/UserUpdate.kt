package ge.gmegrelishvili.messengerapp.model.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserUpdate(
    val whatIDo: String = "",
    val profileImage: String? = null
) : SerializableToMap {

    override fun toMap(): Map<String, Any?> {
        return mapOf(
            "whatIDo" to whatIDo,
            "profileImage" to profileImage
        )
    }

}