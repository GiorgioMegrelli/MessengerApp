package ge.gmegrelishvili.messengerapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ge.gmegrelishvili.messengerapp.R
import ge.gmegrelishvili.messengerapp.viewmodel.MessengerAppViewModel

class ConversationActivity : AppCompatActivity() {

    private val viewModel: MessengerAppViewModel by lazy {
        MessengerAppViewModel.getViewModelProvider(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
    }

}