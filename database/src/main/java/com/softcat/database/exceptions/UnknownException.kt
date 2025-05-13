package com.softcat.database.exceptions

class UnknownException(
    msg: String
): Exception("Unknown error. Something strange happened: message=\'$msg\'.")