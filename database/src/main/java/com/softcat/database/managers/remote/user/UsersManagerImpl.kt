package com.softcat.database.managers.remote.user

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.gson.Gson
import com.softcat.database.exceptions.UserVerificationException
import com.softcat.database.internal.DatabaseRules
import com.softcat.database.model.UserDbModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class UsersManagerImpl @Inject constructor(): UsersManager {

    private val auth by lazy {
        Firebase.auth
    }
    private val usersRef by lazy {
        Firebase.database.getReference(DatabaseRules.USERS_TABLE_NAME)
    }

    private fun UserDbModel.formatUser(id: String) = copy(
        id = id,
        name = "\"$name\""
    )

    private suspend fun saveUser(user: UserDbModel): String {
        return withTimeout(DatabaseRules.TIMEOUT) {
            val reference = usersRef.push()
            val userId = reference.key.toString()
            val newUser = user.formatUser(userId)

            var flag = true
            reference.setValue(newUser).addOnSuccessListener {
                flag = false
            }.addOnFailureListener {
                try {
                    if (isActive)
                        throw it
                } catch (_: Exception) {}
            }
            while (flag) {
                if (isActive)
                    delay(1L)
                else
                    cancel()
            }
            userId
        }
    }

    private suspend fun registerUser(email: String, password: String) {
        return withTimeout(DatabaseRules.TIMEOUT) {
            var flag = true
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                flag = false
            }.addOnFailureListener {
                try {
                    if (isActive)
                        throw it
                } catch (_: Exception) {}
            }
            while (flag) {
                if (isActive)
                    delay(1L)
                else
                    cancel()
            }
        }
    }

    override suspend fun loadUserList(): List<UserDbModel> {
        return withTimeout(DatabaseRules.TIMEOUT) {
            var flag = true
            val users = mutableListOf<UserDbModel>()
            usersRef.get().addOnSuccessListener {
                val iterator = it.children.iterator()
                while (iterator.hasNext()) {
                    val userJson = iterator.next().value.toString()
                    val user = Gson().fromJson(userJson, UserDbModel::class.java)
                    users.add(user)
                }
                flag = false
            }.addOnFailureListener {
                try {
                    if (isActive)
                        throw it
                } catch (_: Exception) {}
            }
            while (flag) {
                if (isActive)
                    delay(1L)
                else
                    cancel()
            }
            users
        }
    }

    private suspend fun createFavouriteCitiesEntry(userId: String) {
        withTimeout(DatabaseRules.TIMEOUT) {
            val reference = Firebase.database.getReference(userId)
            var flag = true
            reference.setValue(emptyList<Int>()).addOnSuccessListener {
                flag = false
            }.addOnFailureListener {
                try {
                    if (isActive)
                        throw it
                } catch (_: Exception) {}
            }
            while (flag) {
                if (isActive)
                    delay(1L)
                else
                    cancel()
            }
        }
    }

    override suspend fun signIn(user: UserDbModel): Result<UserDbModel> {
        return try {
            registerUser(user.email, user.password)
            val generatedId = saveUser(user)
            createFavouriteCitiesEntry(generatedId)
            Result.success(user.copy(id = generatedId))
        } catch (e: Exception) {
            auth.signOut()
            Result.failure(e)
        }
    }

    override suspend fun logIn(name: String, password: String): Result<UserDbModel> {
        val users: List<UserDbModel>
        try {
            users = loadUserList()
        } catch (e: Exception) {
            return Result.failure(e)
        }
        return users.find { it.name == name && it.password == password }?.let {
            Result.success(it)
        } ?: Result.failure(UserVerificationException(name))
    }
}