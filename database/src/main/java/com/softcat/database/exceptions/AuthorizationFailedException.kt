package com.softcat.database.exceptions

class AuthorizationFailedException(
    login: String,
    password: String
): Exception("Authorization failed: incorrect user name ($login) or password ($password).")