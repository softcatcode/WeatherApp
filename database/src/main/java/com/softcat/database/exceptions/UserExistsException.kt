package com.softcat.database.exceptions

class UserExistsException(
    name: String
): Exception("User with name $name already exists.")