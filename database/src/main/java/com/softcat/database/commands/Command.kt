package com.softcat.database.commands

sealed interface Command {

    suspend fun execute()

    suspend fun rollback()
}