package com.softcat.data.implementations

import android.content.Context
import android.net.Uri
import com.softcat.domain.entity.UserAvatar
import com.softcat.domain.interfaces.UserAvatarRepository
import javax.inject.Inject

class UserAvatarRepositoryImpl @Inject constructor(): UserAvatarRepository {
    override suspend fun loadAvatarFromGallery(
        context: Context,
        uri: Uri?
    ): Result<UserAvatar> {
        TODO("Not yet implemented")
    }

    override suspend fun saveAvatar(avatar: UserAvatar): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getAvatarFromDatabase(userId: String): Result<UserAvatar> {
        TODO("Not yet implemented")
    }
}