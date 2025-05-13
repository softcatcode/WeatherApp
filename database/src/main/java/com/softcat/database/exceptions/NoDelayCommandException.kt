package com.softcat.database.exceptions

class NoDelayCommandException: Exception() {
    override val message = "LocalDatabaseCommand ${this::class.simpleName} cannot be delayed."
}