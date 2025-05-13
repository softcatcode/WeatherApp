package com.softcat.database.commands

import com.softcat.database.exceptions.NoDelayCommandException
import com.softcat.database.managers.remote.user.UsersManager
import com.softcat.database.model.UserDbModel

class VerifyUserCommand(
    private val name: String,
    private val password: String,
    private val usersManager: UsersManager
): Command {

    var result: Result<UserDbModel>? = null
        private set

    override suspend fun execute() {
        result = usersManager.logIn(name, password)
    }

    override suspend fun rollback() {
        throw NoDelayCommandException()
    }
}