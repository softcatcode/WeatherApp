package com.softcat.database.exceptions

class UserVerificationException(
    name: String
): Exception("Verification for user $name failed.")