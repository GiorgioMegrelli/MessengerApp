package ge.gmegrelishvili.messengerapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import ge.gmegrelishvili.messengerapp.R


class SignInActivity : SigningActivity() {

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
            if (username.isEmpty()) {
                toast.short(ViewErrorUsername)
            } else if (password.isEmpty()) {
                toast.short(ViewErrorPassword)
            } else {
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

}