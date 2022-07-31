package ge.gmegrelishvili.messengerapp.view.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ge.gmegrelishvili.messengerapp.R
import ge.gmegrelishvili.messengerapp.model.entity.User
import ge.gmegrelishvili.messengerapp.view.ConversationActivity
import ge.gmegrelishvili.messengerapp.view.util.ImageUtil
import ge.gmegrelishvili.messengerapp.viewmodel.MessengerAppViewModel

class SearchUserAdapter(private val activity: AppCompatActivity) :
    RecyclerView.Adapter<SearchUserAdapter.UserViewHolder>() {

    val users = mutableListOf<User>()

    private val viewModel: MessengerAppViewModel by lazy {
        MessengerAppViewModel.getViewModelProvider(activity)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            itemView.findViewById<TextView>(R.id.user_profile_username).text = user.username
            itemView.findViewById<TextView>(R.id.user_profile_what_i_do).text = user.whatIDo
            viewModel.downloadImage(user.key) { bitmap, _ ->
                if (bitmap != null) {
                    itemView.findViewById<ImageView>(R.id.user_profile_image_holder)
                        .setImageBitmap(ImageUtil.cropImageRounded(bitmap))
                }
            }
            itemView.setOnClickListener {
                // Start char with this user
                activity.startActivity(Intent(itemView.context, ConversationActivity::class.java))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.searched_user_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }

}