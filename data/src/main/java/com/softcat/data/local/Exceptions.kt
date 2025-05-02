package com.softcat.data.local

class UserExistsException(
    private val name: String
): Exception("User with name $name already exists.")

class AuthorizationFailedException(
    private val login: String,
    private val password: String
): Exception("Authorization failed: incorrect user name ($login) or password ($password).")