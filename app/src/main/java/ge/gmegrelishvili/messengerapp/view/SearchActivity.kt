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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ge.gmegrelishvili.messengerapp.R
import ge.gmegrelishvili.messengerapp.view.search.SearchUserAdapter
import ge.gmegrelishvili.messengerapp.view.util.ToastWrapper
import ge.gmegrelishvili.messengerapp.viewmodel.MessengerAppViewModel
import java.util.*
import kotlin.concurrent.schedule

class SearchActivity : AppCompatActivity() {

    private val toast = ToastWrapper(this)

    private lateinit var searchNameField: EditText
    private lateinit var loader: ConstraintLayout
    private lateinit var content: RecyclerView
    private lateinit var messageBox: TextView

    private lateinit var usersAdapter: SearchUserAdapter

    private var task: TimerTask? = null

    private val viewModel: MessengerAppViewModel by lazy {
        MessengerAppViewModel.getViewModelProvider(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchNameField = findViewById(R.id.search_name_field)
        loader = findViewById(R.id.loader)
        content = findViewById(R.id.search_found_users)
        messageBox = findViewById(R.id.search_message_box)

        usersAdapter = SearchUserAdapter(this)

        searchNameField.addTextChangedListener(TextChangeListener())

        content.adapter = usersAdapter

        findViewById<ImageView>(R.id.search_back_button).setOnClickListener {
            finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun findUsers(name: String) {
        showLoader()
        hideMessage()
        viewModel.searchUsers(name) { users, error ->
            usersAdapter.apply {
                if (error == null) {
                    this.users.clear()
                    if (users!!.isEmpty()) {
                        showMessage()
                    } else {
                        this.users.addAll(users)
                    }
                    notifyDataSetChanged()
                }
                hideLoader()
            }
        }
    }

    private fun showLoader() {
        runOnUiThread {
            loader.visibility = View.VISIBLE
            content.visibility = View.GONE
        }
    }

    private fun hideLoader() {
        runOnUiThread {
            loader.visibility = View.GONE
            content.visibility = View.VISIBLE
        }
    }

    private fun showMessage() {
        runOnUiThread {
            messageBox.visibility = View.VISIBLE
        }
    }

    private fun hideMessage() {
        runOnUiThread {
            messageBox.visibility = View.GONE
        }
    }

    private inner class TextChangeListener : TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            task?.cancel()
            if (p0 != null) {
                val value = p0.toString()
                if (value.length >= MinChars) {
                    task = Timer().schedule(DelayTimeMillis) {
                        findUsers(value)
                    }
                } else {
                    if (value.length == 1) {
                        toast.short(InfoMessage)
                    }
                    hideMessage()
                }
            }
        }
    }

    companion object {
        private const val MinChars = 3
        private const val DelayTimeMillis = 300L

        private const val InfoMessage = "Min $MinChars letters to search"
    }

}