package com.softcat.database.exceptions

class DuplicateCountryException(
    name: String
): Exception("Country with name $name already exists.")