package ge.gmegrelishvili.messengerapp.view

import android.content.Intent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ge.gmegrelishvili.messengerapp.viewmodel.MessengerAppViewModel

abstract class SigningActivity : AppCompatActivity() {

    protected val toast = ToastWrapper(this)

    protected val viewModel: MessengerAppViewModel by lazy {
        ViewModelProvider(
            this, MessengerAppViewModel.Companion.MessengerAppViewModelFactory(this)
        ).get(MessengerAppViewModel::class.java)
    }

    protected lateinit var usernameEdittext: EditText
    protected lateinit var passwordEdittext: EditText

    protected abstract fun setUpListeners()

    protected fun signIn() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    companion object {
        const val ViewErrorUsername = "'Username' field is Empty!"
        const val ViewErrorPassword = "'Password' field is Empty!"
        const val ViewErrorWhatIDo = "'What I Do' field is Empty!"
    }

}