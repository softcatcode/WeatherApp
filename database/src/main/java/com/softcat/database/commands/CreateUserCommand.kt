package com.softcat.database.commands

import com.softcat.database.managers.remote.user.UsersManager
import com.softcat.database.exceptions.NoDelayCommandException
import com.softcat.database.model.UserDbModel

class CreateUserCommand internal constructor(
    private val user: UserDbModel,
    private val usersManager: UsersManager
): Command {

    var result: Result<UserDbModel>? = null
        private set

    override suspend fun execute() {
        result = usersManager.signIn(user)
    }

    override suspend fun rollback() {
        throw NoDelayCommandException()
    }
}