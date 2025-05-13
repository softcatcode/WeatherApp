package com.softcat.database.exceptions

class NoCommandResultException: Exception() {
    override val message = "Command did not return result."
}