package ge.gmegrelishvili.messengerapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ge.gmegrelishvili.messengerapp.R
import ge.gmegrelishvili.messengerapp.view.search.SearchUserAdapter
import ge.gmegrelishvili.messengerapp.viewmodel.MessengerAppViewModel
import java.util.*
import kotlin.concurrent.schedule

class SearchActivity : AppCompatActivity() {

    private lateinit var searchNameField: EditText
    private lateinit var messageBoxView: TextView

    private lateinit var usersAdapter: SearchUserAdapter

    private var task: TimerTask? = null

    private val viewModel: MessengerAppViewModel by lazy {
        MessengerAppViewModel.getViewModelProvider(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchNameField = findViewById(R.id.search_name_field)
        messageBoxView = findViewById(R.id.search_message_box)

        usersAdapter = SearchUserAdapter(this)

        searchNameField.addTextChangedListener(TextChangeListener())

        findViewById<RecyclerView>(R.id.found_users).adapter = usersAdapter

        findViewById<ImageView>(R.id.search_back_button).setOnClickListener {
            finish()
        }

        showMessage(WelcomeMessage)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun findUsers(name: String) {
        viewModel.searchUsers(name) { users, error ->
            usersAdapter.apply {
                if (error == null) {
                    this.users.clear()
                    if (users!!.isEmpty()) {
                        showMessage(NoResultMessage)
                    } else {
                        this.users.addAll(users)
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun showMessage(value: String?) {
        if (value != null) {
            messageBoxView.text = value
        }
        messageBoxView.visibility = View.VISIBLE
    }

    private fun hideMessage() {
        messageBoxView.visibility = View.GONE
    }

    private inner class TextChangeListener : TextWatcher {

        private var lastLength = 0

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            task?.cancel()
            if (p0 != null) {
                val value = p0.toString()
                if (value.length >= MinChars) {
                    hideMessage()
                    task = Timer().schedule(DelayTimeMillis) {
                        findUsers(value)
                    }
                } else {
                    if (value.length < lastLength) {
                        hideMessage()
                    } else {
                        showMessage(InfoMessage)
                    }
                }
                lastLength = value.length
            }
        }
    }

    companion object {
        private const val MinChars = 3
        private const val DelayTimeMillis = 300L

        private const val WelcomeMessage = "Type to find users"
        private const val InfoMessage = "Min $MinChars letters to search"
        private const val NoResultMessage = "No Result"
    }

}