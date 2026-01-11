package com.softcat.data.implementations

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.softcat.data.mapper.toDbModel
import com.softcat.data.mapper.toEntity
import com.softcat.database.facade.DatabaseFacade
import com.softcat.domain.entity.UserAvatar
import com.softcat.domain.interfaces.UserAvatarRepository
import javax.inject.Inject

class UserAvatarRepositoryImpl @Inject constructor(
    private val database: DatabaseFacade
): UserAvatarRepository {

    override suspend fun loadAvatarFromGallery(
        context: Context,
        uri: Uri?
    ): Result<UserAvatar> {
        try {
            if (uri == null) {
                return Result.failure(IllegalArgumentException("URI is null"))
            }
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            if (bitmap == null) {
                return Result.failure(RuntimeException("Failed to decode bitmap from URI"))
            }
            val scaled = Bitmap.createScaledBitmap(bitmap, 128, 128, true)
            bitmap.recycle()
            return Result.success(UserAvatar(scaled))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun saveAvatar(userId: String, avatar: UserAvatar): Result<Unit> {
        val response = database.updateAvatar(userId, avatar.toDbModel())
        val result = response.getOrElse {
            return Result.failure(it)
        }
        return Result.success(result)
    }

    override suspend fun getAvatarFromDatabase(userId: String): Result<UserAvatar> {
        val response = database.getAvatar(userId)
        val avatar = response.getOrElse {
            return Result.failure(it)
        }
        return Result.success(avatar.toEntity())
    }
}