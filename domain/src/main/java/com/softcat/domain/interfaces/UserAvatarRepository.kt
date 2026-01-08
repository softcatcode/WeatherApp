package com.softcat.domain.interfaces

import android.content.Context
import android.net.Uri
import com.softcat.domain.entity.UserAvatar

interface UserAvatarRepository {
    suspend fun loadAvatarFromGallery(context: Context, uri: Uri?): Result<UserAvatar>

    suspend fun saveAvatar(avatar: UserAvatar): Result<Unit>

    suspend fun getAvatarFromDatabase(userId: String): Result<UserAvatar>
}