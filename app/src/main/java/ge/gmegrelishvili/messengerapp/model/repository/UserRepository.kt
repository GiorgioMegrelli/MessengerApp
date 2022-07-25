package ge.gmegrelishvili.messengerapp.model.repository

import ge.gmegrelishvili.messengerapp.model.entity.User
import ge.gmegrelishvili.messengerapp.model.entity.UserUpdate

interface UserRepository {

    fun insertUser(key: String, user: User, insertUserResult: InsertUserResult)

    fun getUser(key: String, getUserResult: GetUserResult)

    fun updateUser(key: String, user: UserUpdate, updateUserResult: UpdateUserResult)

    fun getUsersByName(username: String, getUsersByNameResult: GetUsersByNameResult)

    companion object {
        interface InsertUserResult {
            fun <T> insertUserFinished(error: T?)
        }

        interface GetUserResult {
            fun <T> getUserFinished(user: User?, error: T?)
        }

        interface UpdateUserResult {
            fun <T> updateUserFinished(error: T?)
        }

        interface GetUsersByNameResult {
            fun <T> getUsersByNameFinished(users: List<User>?, error: T?)
        }
    }

}