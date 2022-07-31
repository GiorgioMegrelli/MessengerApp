package ge.gmegrelishvili.messengerapp.view.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.gmegrelishvili.messengerapp.R
import ge.gmegrelishvili.messengerapp.model.entity.ConversationListItemModel
import ge.gmegrelishvili.messengerapp.view.ConversationActivity

class ConversationListAdapter(var list: List<ConversationListItemModel>, private val avtivity: Activity):
    RecyclerView.Adapter<ConversationListAdapter.ConversationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.conversation_list_item, parent, false)
        return ConversationViewHolder(view, avtivity)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val item = list[position]
        holder.initView(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ConversationViewHolder(itemView: View, private val avtivity: Activity): RecyclerView.ViewHolder(itemView){
        val conversationProfilePicView = itemView.findViewById<ImageView>(R.id.conversationProfilePic)
        val conversationUserNameView = itemView.findViewById<TextView>(R.id.conversationUserName)
        val lastMessageView = itemView.findViewById<TextView>(R.id.lastMessage)
        val timeAfterLastMessageView = itemView.findViewById<TextView>(R.id.timeAfterLastMessage)

        fun initView(conversationListItemModel: ConversationListItemModel) {


            conversationProfilePicView.setImageBitmap(conversationListItemModel.bitmap)
            conversationUserNameView.text = conversationListItemModel.conversationUserName
            lastMessageView.text = conversationListItemModel.lastMessage

            timeAfterLastMessageView.text = conversationListItemModel.timeAfterLastMessage.toString()

            itemView.setOnClickListener {
                avtivity.startActivity(
                    Intent(
                        itemView.context,
                        ConversationActivity::class.java
                    )
                )
            }

        }

    }
}

