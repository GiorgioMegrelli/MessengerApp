package ge.gmegrelishvili.messengerapp.view

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import ge.gmegrelishvili.messengerapp.R
import ge.gmegrelishvili.messengerapp.model.entity.*
import ge.gmegrelishvili.messengerapp.viewmodel.MessengerAppViewModel
import java.text.SimpleDateFormat

class ConversationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database

    private val viewModel: MessengerAppViewModel by lazy {
        MessengerAppViewModel.getViewModelProvider(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
        fetchMessages()
//        addOnDataChangedListener()
    }

    private fun fetchMessages() {
        val username = intent.extras!!.getString("username")
        var userId = ""
        viewModel.searchUsers(username!!){ list: List<User>?, exception: Exception? ->
            if(exception == null){
                if(!list!!.isEmpty()){
                    for (user in list){
                        if(user.username.equals(username)){
                            userId = user.key
                        }
                    }
                }
            }
        }
        val currentUserId = viewModel.getCurrentUid()
        auth = Firebase.auth
        val ref =
            auth.currentUser!!.displayName?.let {
                database.getReference("userConversationsData").child(
                    it
                )
            }
        ref!!.get().addOnCompleteListener {
            if (it.isSuccessful){

                val res : DataSnapshot = it.result.child(currentUserId).child("conversationMap").child(userId)
                val conversation = res.getValue(Conversation::class.java)
                if (conversation != null)
                    renderConversation(conversation, userId)

            }
        }
    }

    private fun renderConversation(conversation: Conversation, senderId : String) {
        val messageList = conversation.messageList
        var senderUsername = ""
        viewModel.getUser(senderId) {user ->
            if(user != null){
                setSenderProfileInfo(user)
                senderUsername = user.username
                for(message in messageList!!){
                    //gamovachinot mesijii
                    var curMessage = ConversationMessageModel(senderUsername, message.value, timestampToTime(message.timestamp))
                    var prosta = curMessage
                }
            }
        }

    }

    private fun setSenderProfileInfo(user: User) {
        val ref = Firebase.storage.reference.child("profileImages/${user.key}")
        ref.getBytes(Long.MAX_VALUE).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            this.findViewById<ImageView>(R.id.sender_image).setImageBitmap(bitmap)
            this.findViewById<TextView>(R.id.sender_username).text = user.username
            this.findViewById<TextView>(R.id.sender_what_i_do).text = user.whatIDo
        }

    }

    private fun timestampToTime(timestamp: String): String {
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(timestamp.toLong() * 1000).toString()
    }

}