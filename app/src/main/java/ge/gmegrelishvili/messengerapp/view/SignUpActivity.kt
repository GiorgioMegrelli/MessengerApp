package ge.gmegrelishvili.messengerapp.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import ge.gmegrelishvili.messengerapp.R
import ge.gmegrelishvili.messengerapp.view.errors.ViewError

class SignUpActivity : SigningActivity() {

    private val toast = ToastWrapper(this)

    private lateinit var whatIDoEdittext: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setUpListeners()
    }

    override fun setUpListeners() {
        usernameEdittext = findViewById(R.id.sign_up_edittext_username)
        passwordEdittext = findViewById(R.id.sign_up_edittext_password)
        whatIDoEdittext = findViewById(R.id.sign_up_edittext_what_i_do)

        findViewById<Button>(R.id.sign_up_sign_up_button).setOnClickListener {
            val username = usernameEdittext.text.toString().trim()
            val password = passwordEdittext.text.toString().trim()
            val whatIDo = whatIDoEdittext.text.toString().trim()
            if (toast.shortIf(ViewError.EmptyUsername, username.isEmpty())) {
                return@setOnClickListener
            }
            if (toast.shortIf(ViewError.EmptyPassword, password.isEmpty())) {
                return@setOnClickListener
            }
            if (toast.shortIf(ViewError.EmptyWhatIDo, whatIDo.isEmpty())) {
                return@setOnClickListener
            }
            viewModel.createUser(username, password, whatIDo) { error ->
                if (error?.message != null) {
                    toast.short(error.message!!)
                } else {
                    signIn()
                }
            }
        }
    }

}