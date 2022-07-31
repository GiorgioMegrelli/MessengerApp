package ge.gmegrelishvili.messengerapp.view.main

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import ge.gmegrelishvili.messengerapp.R
import ge.gmegrelishvili.messengerapp.model.entity.*
import ge.gmegrelishvili.messengerapp.viewmodel.MessengerAppViewModel
import java.text.SimpleDateFormat
import kotlin.collections.HashMap

class HomeFragment(fragmentActivity: FragmentActivity) : Fragment() {
    private lateinit var recyclerView: RecyclerView
    var recyclerViewAdapter = ConversationListAdapter(mutableListOf(), fragmentActivity)
    private lateinit var searchEt : EditText
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database

    private val viewModel: MessengerAppViewModel by lazy {
        ViewModelProvider(
            this, MessengerAppViewModel.Companion.MessengerAppViewModelFactory()
        ).get(MessengerAppViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val createdView = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = createdView?.findViewById(R.id.conversationsRecyclerView)!!
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        searchEt = createdView.findViewById(R.id.search_text)
        //search-shi textis listener
        //addConversationsForOneUser()
        fetchConversations()
        addOnDataChangedListener()
        return createdView
    }

    private fun addConversationsForOneUser() {
        val message1 = Message("iKo3NeGrUMc7bPW0gEgup5GuKLs2", "0akBbDBCRHbEXgmR3cYF64LFBrY2", "first message", "1658399732558")
        val message2 = Message("iKo3NeGrUMc7bPW0gEgup5GuKLs2", "0akBbDBCRHbEXgmR3cYF64LFBrY2", "second message", "1658399734558")
        val messageList1 : MutableList<Message> = mutableListOf(message1, message2)
        val conversation1 = Conversation(messageList1)

        val message3 = Message("iKo3NeGrUMc7bPW0gEgup5GuKLs2", "eyxXpU6AgnVtbiafnUmdgETmIas2", "third message", "1658399732558")
        val messageList2 : MutableList<Message> = mutableListOf(message3)
        val conversation2 = Conversation(messageList2)

        database.getReference("userConversationsData")
                                            .child("iKo3NeGrUMc7bPW0gEgup5GuKLs2")
                                            .child("conversationMap")
                                            .child("0akBbDBCRHbEXgmR3cYF64LFBrY2")
                                            .setValue(conversation1)

        database.getReference("userConversationsData")
                                            .child("iKo3NeGrUMc7bPW0gEgup5GuKLs2")
                                            .child("conversationMap")
                                            .child("eyxXpU6AgnVtbiafnUmdgETmIas2")
                                            .setValue(conversation2)
    }

    private fun addOnDataChangedListener() {
        auth = Firebase.auth
        auth.currentUser!!.displayName?.let {
            database.getReference("userConversationsData").child(
                it
            )
        }?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userId = viewModel.getCurrentUid()
                val res: DataSnapshot = dataSnapshot.child(userId)
                val value = res.getValue(ConversationMap::class.java)
                if (value != null)
                    renderConversations(value)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchConversations()
    }

    private fun fetchConversations() {
        auth = Firebase.auth
        val ref =
            auth.currentUser!!.displayName?.let {
                database.getReference("userConversationsData").child(
                    it
                )
            }
        ref!!.get().addOnCompleteListener {
            if (it.isSuccessful){
                val userId = viewModel.getCurrentUid()
                val res : DataSnapshot = it.result.child(userId)
                val userConversationsData =res.getValue(ConversationMap::class.java)
                if(userConversationsData != null)
                    renderConversations(userConversationsData)

            }
        }

    }

    private fun renderConversations(conversationMap: ConversationMap) {
        val conversationsMap = conversationMap.conversationMap ?: HashMap<String, Conversation>()
        val conversationsList = mutableListOf<ConversationListItemModel>()
        for((key, value) in conversationsMap){
            val conversation = value.messageList
            val lastMessage = conversation?.get(conversation.size - 1)
            val ref = Firebase.storage.reference.child("profileImages/${key}")
            ref.getBytes(Long.MAX_VALUE).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                viewModel.getUser(key) {user ->
                    if(user != null){
                        val conversationListItem = ConversationListItemModel(user.username, lastMessage!!.value,
                            getTimeAfterLastMessage(lastMessage.timestamp), bitmap
                        )
                        conversationsList.add(conversationListItem)
                        recyclerViewAdapter.list = conversationsList
                        recyclerViewAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun getTimeAfterLastMessage(lastMessageTimestamp: String): String {
        var timeAfterLastMessage = ""
        val currentTimestamp = System.currentTimeMillis()
        val diff = currentTimestamp - lastMessageTimestamp.toLong()
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        if(hours < 1){
            return "$timeAfterLastMessage$minutes min"
        }
        if(days < 1){
            return "$timeAfterLastMessage$hours hour"
        }
        val sdf = SimpleDateFormat("d MMM")
        return sdf.format(diff * 1000).toString()
    }


}