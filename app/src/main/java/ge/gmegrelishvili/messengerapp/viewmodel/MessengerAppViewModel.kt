package ge.gmegrelishvili.messengerapp.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import ge.gmegrelishvili.messengerapp.model.entity.User
import ge.gmegrelishvili.messengerapp.model.entity.UserUpdate
import ge.gmegrelishvili.messengerapp.model.repository.*

class MessengerAppViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val imageRepository: ImageRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    fun isSignedIn(): Boolean {
        return authRepository.getCurrentUid() != null
    }

    fun getCurrentUid(): String {
        return authRepository.getCurrentUid()!!
    }

    fun getUser(key: String, callback: (User?) -> Unit) {
        userRepository.getUser(key, object : UserRepository.Companion.GetUserResult {
            override fun <T> getUserFinished(user: User?, error: T?) {
                callback(user)
            }
        })
    }

    fun getUser(callback: (User?) -> Unit) {
        if (authRepository.getCurrentUid() == null) {
            callback(null)
        }
        getUser(authRepository.getCurrentUid()!!, callback)
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
                    if (key != null) {
                        val user = User(key, username, whatIDo)
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

    fun updateUser(
        key: String,
        user: UserUpdate,
        callback: (Exception?) -> Unit
    ) {
        userRepository.updateUser(key, user, object : UserRepository.Companion.UpdateUserResult {
            override fun <T> updateUserFinished(error: T?) {
                if (error == null) {
                    callback(null)
                } else {
                    callback(Exception(UnknownExceptionString))
                }
            }
        })
    }

    fun signOut() {
        authRepository.signOut()
    }

    fun searchUsers(
        subUsername: String,
        callback: (List<User>?, Exception?) -> Unit
    ) {
        userRepository.getUsersByName(
            subUsername,
            object : UserRepository.Companion.GetUsersByNameResult {
                override fun <T> getUsersByNameFinished(users: List<User>?, error: T?) {
                    if (error == null) {
                        callback(users, null)
                    } else {
                        callback(null, Exception(UnknownExceptionString))
                    }
                }
            })
    }

    fun uploadImage(
        key: String,
        uri: Uri,
        callback: (Exception?) -> Unit
    ) {
        imageRepository.uploadImage(key, uri, object : ImageRepository.Companion.UploadImageResult {
            override fun <T> uploadImageFinished(error: T?) {
                if (error == null) {
                    callback(null)
                } else {
                    callback(Exception(UnknownExceptionString))
                }
            }
        })
    }

    fun downloadImage(
        key: String, callback: (Bitmap?, Exception?) -> Unit
    ) {
        imageRepository.downloadImage(key, object : ImageRepository.Companion.DownloadImageResult {
            override fun <T> downloadImageFinished(bitmap: Bitmap?, error: T?) {
                if (error == null) {
                    callback(bitmap, null)
                } else {
                    callback(null, Exception(UnknownExceptionString))
                }
            }
        })
    }

    companion object {
        private const val ViewModelExceptionString = "Illegal ViewModel"

        private const val InvalidUsernameExceptionString = "Invalid Username"
        private const val InvalidPasswordExceptionString = "Invalid Password"
        private const val UnknownExceptionString = "Unknown Exception"
        private const val WeakPasswordExceptionString = "Weak Password"
        private const val TakenUsernameExceptionString = "Username is Taken"

        @JvmStatic
        fun getViewModelProvider(owner: ViewModelStoreOwner): MessengerAppViewModel {
            return ViewModelProvider(owner, MessengerAppViewModelFactory()).get(
                MessengerAppViewModel::class.java
            )
        }

        class MessengerAppViewModelFactory :
            ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MessengerAppViewModel::class.java)) {
                    val userRepository = FirebaseUserRepository()
                    val authRepository = FirebaseAuthRepository()
                    val imageRepository = FirebaseImageRepository()
                    val messageRepository = FirebaseMessageRepository()

                    return MessengerAppViewModel(
                        userRepository,
                        authRepository,
                        imageRepository,
                        messageRepository
                    ) as T
                }
                throw IllegalStateException(ViewModelExceptionString)
            }
        }
    }

}