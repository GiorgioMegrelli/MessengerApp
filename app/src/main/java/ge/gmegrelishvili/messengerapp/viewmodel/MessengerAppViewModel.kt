package ge.gmegrelishvili.messengerapp.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import ge.gmegrelishvili.messengerapp.model.entity.User
import ge.gmegrelishvili.messengerapp.model.repository.AuthRepository
import ge.gmegrelishvili.messengerapp.model.repository.FirebaseAuthRepository
import ge.gmegrelishvili.messengerapp.model.repository.FirebaseUserRepository
import ge.gmegrelishvili.messengerapp.model.repository.UserRepository

class MessengerAppViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    /*
    * Default User:
    *   Username:   test
    *   Password:   testtest
    * */

    fun isSignedIn(): Boolean {
        return authRepository.getCurrentUid() != null
    }

    fun signIn(username: String, password: String, callback: (Exception?) -> Unit) {
        val callbackInterface = object : AuthRepository.Companion.SignInUserResult {
            override fun <T> signInUserFinished(key: String?, error: T?) {
                if (error == null) {
                    callback(null)
                    return
                }
                if (error is FirebaseAuthInvalidUserException) {
                    callback(Exception(InvalidUsernameExceptionString))
                } else if (error is FirebaseAuthInvalidCredentialsException) {
                    callback(Exception(InvalidPasswordExceptionString))
                } else {
                    callback(Exception(UnknownExceptionString))
                }
            }
        }
        authRepository.signInUser(username, password, callbackInterface)
    }

    fun createUser(
        username: String,
        password: String,
        whatIDo: String,
        callback: (Exception?) -> Unit
    ) {
        val userRepoHandler = object : UserRepository.Companion.InsertUserResult {
            override fun <T> insertUserFinished(error: T?) {
                if (error == null) {
                    callback(null)
                } else {
                    callback(Exception(UnknownExceptionString))
                }
            }
        }
        val authRepoHandler = object : AuthRepository.Companion.CreateUserResult {
            override fun <T> createUserFinished(key: String?, error: T?) {
                if (error == null) {
                    val user = User(username, password, whatIDo)
                    if (key != null) {
                        userRepository.insertUser(key, user, userRepoHandler)
                    }
                    return
                }
                if (error is FirebaseAuthWeakPasswordException) {
                    callback(Exception(WeakPasswordExceptionString))
                } else if (error is FirebaseAuthUserCollisionException) {
                    callback(Exception(TakenUsernameExceptionString))
                } else {
                    callback(Exception(UnknownExceptionString))
                }
            }
        }
        authRepository.createUser(username, password, authRepoHandler)
    }

    fun signOut() {
        authRepository.signOut()
    }

    companion object {
        private const val ViewModelExceptionString = "Illegal ViewModel"

        private const val InvalidUsernameExceptionString = "Invalid Username"
        private const val InvalidPasswordExceptionString = "Invalid Password"
        private const val UnknownExceptionString = "Unknown Exception"
        private const val WeakPasswordExceptionString = "Weak Password"
        private const val TakenUsernameExceptionString = "Username is Taken"

        class MessengerAppViewModelFactory(private val activity: Activity) :
            ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MessengerAppViewModel::class.java)) {
                    val userRepository = FirebaseUserRepository()
                    val authRepository = FirebaseAuthRepository()
                    val viewModel = MessengerAppViewModel(userRepository, authRepository)
                    return viewModel as T
                }
                throw IllegalStateException(ViewModelExceptionString)
            }
        }
    }

}