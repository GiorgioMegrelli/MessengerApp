package ge.gmegrelishvili.messengerapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import ge.gmegrelishvili.messengerapp.R
import ge.gmegrelishvili.messengerapp.view.errors.ViewError
import ge.gmegrelishvili.messengerapp.view.util.ToastWrapper


class SignInActivity : SigningActivity() {

    private val toast = ToastWrapper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.isSignedIn()) {
            signIn()
        } else {
            setContentView(R.layout.activity_sign_in)
            setUpListeners()
        }
    }

    override fun setUpListeners() {
        usernameEdittext = findViewById(R.id.sign_in_edittext_username)
        passwordEdittext = findViewById(R.id.sign_in_edittext_password)

        findViewById<Button>(R.id.sign_in_sign_up_button).setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        findViewById<Button>(R.id.sign_in_sign_in_button).setOnClickListener {
            val username = usernameEdittext.text.toString().trim()
            val password = passwordEdittext.text.toString().trim()
            ViewError.EmptyUsername
            if (toast.shortIf(ViewError.EmptyUsername, username.isEmpty())) {
                return@setOnClickListener
            }
            if (toast.shortIf(ViewError.EmptyPassword, password.isEmpty())) {
                return@setOnClickListener
            }
            viewModel.signIn(username, password) { error ->
                if (error?.message != null) {
                    toast.short(error.message!!)
                } else {
                    signIn()
                }
            }
        }
    }

}