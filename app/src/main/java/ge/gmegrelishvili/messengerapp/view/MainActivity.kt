package ge.gmegrelishvili.messengerapp.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ge.gmegrelishvili.messengerapp.R
import ge.gmegrelishvili.messengerapp.model.repository.FirebaseAuthRepository
import ge.gmegrelishvili.messengerapp.viewmodel.MessengerAppViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MessengerAppViewModel by lazy {
        ViewModelProvider(
            this, MessengerAppViewModel.Companion.MessengerAppViewModelFactory(this)
        ).get(MessengerAppViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Small example
        val authRepository = FirebaseAuthRepository()
        val textView = findViewById<TextView>(R.id.test_id)
        textView.text = authRepository.getCurrentUid().toString()
        textView.setOnClickListener {
            viewModel.signOut()
            finish()
        }
    }

}