package com.softcat.database.managers.remote.user

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.gson.Gson
import com.softcat.database.exceptions.UserExistsException
import com.softcat.database.exceptions.UserVerificationException
import com.softcat.database.internal.DatabaseRules
import com.softcat.database.mapper.userDbModel
import com.softcat.database.model.UserDbModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import kotlin.math.max

class UsersManagerImpl @Inject constructor(): UsersManager {

    private val auth by lazy {
        Firebase.auth
    }
    private val usersRef by lazy {
        Firebase.database.getReference(DatabaseRules.USERS_TABLE_NAME)
    }

    private fun correctUserNames(userList: List<UserDbModel>) = userList.map {
        it.copy(name = "\"${it.name}\"")
    }

    private suspend fun updateUsers(newUserList: List<UserDbModel>) {
        withTimeout(DatabaseRules.TIMEOUT) {
            var flag = true
            usersRef.setValue(correctUserNames(newUserList)).addOnSuccessListener {
                flag = false
            }.addOnFailureListener {
                throw it
            }
            while (flag) {
                delay(1L)
            }
        }
    }

    private suspend fun registerUser(email: String, password: String) {
        return withTimeout(DatabaseRules.TIMEOUT) {
            var flag = true
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                flag = false
            }.addOnFailureListener {
                throw it
            }
            while (flag) {
                delay(1L)
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
                throw it
            }
            while (flag) {
                delay(1L)
            }
            users
        }
    }

    private fun getMaxId(users: List<UserDbModel>): Int {
        var res = 0
        users.forEach { res = max(res, it.id) }
        return res
    }

    private suspend fun createFavouriteCitiesEntry(userId: Int) {
        withTimeout(DatabaseRules.TIMEOUT) {
            val reference = Firebase.database.getReference(userId.toString())
            var flag = true
            reference.setValue(emptyList<Int>()).addOnSuccessListener {
                flag = false
            }.addOnFailureListener {
                throw it
            }
            while (flag) {
                delay(1L)
            }
        }
    }

    override suspend fun signIn(name: String, email: String, password: String): Result<UserDbModel> {
        val users: List<UserDbModel>
        try {
            users = loadUserList()
        } catch (e: Exception) {
            return Result.failure(e)
        }
        users.find { it.name == name }?.let {
            return Result.failure(UserExistsException(name))
        }
        val newUserId = getMaxId(users) + 1
        try {
            createFavouriteCitiesEntry(newUserId)
            registerUser(email, password)
        } catch (e: Exception) {
            return Result.failure(e)
        }

        val userModel = userDbModel(newUserId, name, email, password)
        val newUserList = users.toMutableList().apply {
            add(userModel)
        }
        try {
            updateUsers(newUserList)
        } catch (e: Exception) {
            return Result.failure(e)
        }
        return Result.success(userModel)
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