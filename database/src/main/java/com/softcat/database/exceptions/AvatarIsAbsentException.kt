package com.softcat.database.exceptions

class AvatarIsAbsentException: Exception() {
    override val message = "User avatar image was not loaded or was deleted."
}