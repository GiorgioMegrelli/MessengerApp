package ge.gmegrelishvili.messengerapp.view.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import ge.gmegrelishvili.messengerapp.R
import ge.gmegrelishvili.messengerapp.model.entity.User
import ge.gmegrelishvili.messengerapp.view.ToastWrapper
import ge.gmegrelishvili.messengerapp.view.errors.ViewError
import ge.gmegrelishvili.messengerapp.viewmodel.MessengerAppViewModel


class ProfileFragment(
    private val fragmentActivity: FragmentActivity
) : Fragment() {

    private val toast = ToastWrapper(fragmentActivity)
    private lateinit var currentUser: User

    private val viewModel: MessengerAppViewModel by lazy {
        ViewModelProvider(
            this, MessengerAppViewModel.Companion.MessengerAppViewModelFactory(fragmentActivity)
        ).get(MessengerAppViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val createdView = inflater.inflate(R.layout.fragment_profile, container, false)

        viewModel.getUser { user ->
            if (user == null) {
                viewModel.signOut()
                fragmentActivity.finishAffinity()
            } else {
                currentUser = user
                drawUi(createdView, user)
            }
        }

        return createdView
    }

    private fun drawUi(createdView: View, user: User) {
        val usernameEdittext = createdView.findViewById<EditText>(R.id.profile_edittext_username)
        val whatIDoEdittext = createdView.findViewById<EditText>(R.id.profile_edittext_what_i_do)

        usernameEdittext.setText(user.username)
        whatIDoEdittext.setText(user.whatIDo)

        createdView.findViewById<Button>(R.id.profile_sign_out_button).setOnClickListener {
            viewModel.signOut()
        }

        createdView.findViewById<ImageView>(R.id.profile_image_holder).setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
        }

        createdView.findViewById<Button>(R.id.profile_update_button).setOnClickListener {
            val username = usernameEdittext.text.toString().trim()
            val whatIDo = whatIDoEdittext.text.toString().trim()
            if (toast.shortIf(ViewError.EmptyUsername, username.isEmpty())) {
                return@setOnClickListener
            }
            if (toast.shortIf(ViewError.EmptyWhatIDo, whatIDo.isEmpty())) {
                return@setOnClickListener
            }
        }
    }

}