package com.softcat.data.mapper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.softcat.data.network.dto.UserDto
import com.softcat.database.model.UserAvatarDbModel
import com.softcat.database.model.UserDbModel
import com.softcat.domain.entity.User
import com.softcat.domain.entity.UserAvatar
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Calendar

fun UserDbModel.toEntity() = User(
    id = id,
    name = name,
    role = role.toUserRole(),
    email = email,
    password = password,
)

fun UserDto.toEntity() = User(
    id = userId,
    name = name,
    role = role.toUserRole(),
    email = email,
    password = password,
)

private fun String.toUserRole() = when (this) {
    UserDbModel.ROLE_REGULAR -> User.Status.Regular
    UserDbModel.ROLE_FOLLOWER -> User.Status.Follower
    UserDbModel.ROLE_PREMIUM -> User.Status.Premium
    else -> User.Status.Regular
}

private fun User.Status.toDbModel() = when (this) {
    User.Status.Regular -> UserDbModel.ROLE_REGULAR
    User.Status.Follower -> UserDbModel.ROLE_FOLLOWER
    User.Status.Premium -> UserDbModel.ROLE_PREMIUM
}

fun userDbModel(login: String, email: String, password: String): UserDbModel {
    return UserDbModel(
        name = login,
        email = email,
        password = password,
        role = User.Status.Regular.toDbModel(),
        registerTimeEpoch = Calendar.getInstance().timeInMillis / 1000L
    )
}

fun UserAvatar.toDbModel(): UserAvatarDbModel {
    val stream = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    val bytes = stream.toByteArray()
    return UserAvatarDbModel(
        Base64.encodeToString(bytes, Base64.DEFAULT)
    )
}

fun UserAvatarDbModel.toEntity(): UserAvatar {
    val bytes = Base64.decode(image, Base64.DEFAULT)
    val stream = ByteArrayInputStream(bytes)
    return UserAvatar(BitmapFactory.decodeStream(stream))
}
