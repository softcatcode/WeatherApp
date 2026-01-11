package com.softcat.database.commands

import com.softcat.database.exceptions.NoCommandResultException
import com.softcat.database.exceptions.NoDelayCommandException
import com.softcat.database.managers.remote.user.UsersManager
import com.softcat.database.model.UserAvatarDbModel

class ReadAvatarCommand(
    private val manager: UsersManager,
    private val userId: String
): Command {

    var result: Result<UserAvatarDbModel> = Result.failure(NoCommandResultException())
        private set

    override suspend fun execute() {
        result = manager.readUserAvatar(userId)
    }

    override suspend fun rollback() {
        throw NoDelayCommandException()
    }
}