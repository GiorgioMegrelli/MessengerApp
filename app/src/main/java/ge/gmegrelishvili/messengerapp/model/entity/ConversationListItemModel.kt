package ge.gmegrelishvili.messengerapp.model.entity

import android.graphics.Bitmap

data class ConversationListItemModel(val conversationUserName: String, val lastMessage: String, val timeAfterLastMessage: String, val bitmap: Bitmap)