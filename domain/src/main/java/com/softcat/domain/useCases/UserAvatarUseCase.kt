package com.softcat.domain.useCases

import android.content.Context
import android.net.Uri
import com.softcat.domain.entity.UserAvatar
import com.softcat.domain.interfaces.UserAvatarRepository
import timber.log.Timber
import javax.inject.Inject

class UserAvatarUseCase @Inject constructor(
    private val repository: UserAvatarRepository
) {
    suspend fun read(context: Context, uri: Uri?): Result<UserAvatar> {
        Timber.i("${this::class.simpleName} loadAvatar($uri) invoked")
        return repository.loadAvatarFromGallery(context, uri)
    }

    suspend fun save(userId: String, avatar: UserAvatar): Result<Unit> {
        Timber.i("${this::class.simpleName} saveAvatar($avatar) invoked")
        return repository.saveAvatar(userId, avatar)
    }

    suspend fun get(userId: String): Result<UserAvatar> {
        Timber.i("${this::class.simpleName} get($userId) invoked")
        return repository.getAvatarFromDatabase(userId)
    }
}