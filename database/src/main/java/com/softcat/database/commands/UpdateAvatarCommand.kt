package com.softcat.database.commands

import com.softcat.database.exceptions.NoCommandResultException
import com.softcat.database.exceptions.NoDelayCommandException
import com.softcat.database.managers.remote.user.UsersManager
import com.softcat.database.model.UserAvatarDbModel

class UpdateAvatarCommand(
    private val manager: UsersManager,
    private val userId: String,
    private val avatar: UserAvatarDbModel
): Command {

    var result: Result<Unit> = Result.failure(NoCommandResultException())
        private set

    override suspend fun execute() {
        result = manager.updateUserAvatar(userId, avatar)
    }

    override suspend fun rollback() {
        throw NoDelayCommandException()
    }
}