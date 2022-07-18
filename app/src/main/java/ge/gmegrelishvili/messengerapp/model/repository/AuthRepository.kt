package ge.gmegrelishvili.messengerapp.model.repository;

import ge.gmegrelishvili.messengerapp.model.repository.strategy.EmailStrategy

interface AuthRepository {

    fun createUser(username: String, password: String, createUserResult: CreateUserResult)

    fun signInUser(username: String, password: String, signInUserResult: SignInUserResult)

    fun getCurrentUid(): String?

    fun signOut()

    fun setEmailStrategy(emailStrategy: EmailStrategy)

    companion object {
        interface CreateUserResult {
            fun <T> createUserFinished(key: String?, error: T?)
        }

        interface SignInUserResult {
            fun <T> signInUserFinished(key: String?, error: T?)
        }
    }

}
