package ge.gmegrelishvili.messengerapp.model.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.gmegrelishvili.messengerapp.model.repository.strategy.EmailStrategy
import ge.gmegrelishvili.messengerapp.model.repository.strategy.TestEmailStrategy

class FirebaseAuthRepository(
    private var emailStrategy: EmailStrategy = TestEmailStrategy()
) : AuthRepository {

    private val auth = Firebase.auth

    override fun createUser(
        username: String,
        password: String,
        createUserResult: AuthRepository.Companion.CreateUserResult
    ) {
        val email = emailStrategy.createEmail(username)
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                createUserResult.createUserFinished(auth.currentUser?.uid, null)
            } else {
                createUserResult.createUserFinished(null, it.exception)
            }
        }
    }

    override fun signInUser(
        username: String,
        password: String,
        signInUserResult: AuthRepository.Companion.SignInUserResult
    ) {
        val email = emailStrategy.createEmail(username)
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                signInUserResult.signInUserFinished(auth.currentUser?.uid, null)
            } else {
                signInUserResult.signInUserFinished(null, it.exception)
            }
        }
    }

    override fun getCurrentUid(): String? {
        return auth.currentUser?.uid
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun setEmailStrategy(emailStrategy: EmailStrategy) {
        this.emailStrategy = emailStrategy
    }

}