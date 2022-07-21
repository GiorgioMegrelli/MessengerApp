package ge.gmegrelishvili.messengerapp.model.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import ge.gmegrelishvili.messengerapp.model.entity.User

class FirebaseUserRepository : UserRepository {

    private val database = Firebase.database

    override fun insertUser(
        key: String,
        user: User,
        insertUserResult: UserRepository.Companion.InsertUserResult
    ) {
        val usersRef = database.getReference(ReferenceName)
        usersRef.child(key).updateChildren(user.toMap()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                insertUserResult.insertUserFinished(null)
            } else {
                insertUserResult.insertUserFinished(task.exception)
            }
        }
    }

    override fun getUser(key: String, getUserResult: UserRepository.Companion.GetUserResult) {
        val usersRef = database.getReference(ReferenceName)
        usersRef.child(key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<User>()
                getUserResult.getUserFinished(user, null)
            }

            override fun onCancelled(error: DatabaseError) {
                getUserResult.getUserFinished(null, error)
            }
        })
    }

    override fun updateUser(
        key: String,
        user: User,
        updateUserResult: UserRepository.Companion.UpdateUserResult
    ) {
    }

    override fun getUsersByName(
        username: String,
        getUsersByNameResult: UserRepository.Companion.GetUsersByNameResult
    ) {
        val usersRef = database.getReference(ReferenceName)
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<User>()
                for (c in snapshot.children) {
                    val user = c.getValue<User>()
                    if (user != null) {
                        val uname = user.username.lowercase()
                        if (uname.contains(username.lowercase())) {
                            list.add(user)
                        }
                    }
                }
                getUsersByNameResult.getUsersByNameFinished(list, null)
            }

            override fun onCancelled(error: DatabaseError) {
                getUsersByNameResult.getUsersByNameFinished(null, error)
            }
        })
    }

    companion object {
        private const val ReferenceName = "users"
    }

}