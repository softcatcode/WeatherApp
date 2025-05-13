package com.softcat.database.internal.queries

import com.softcat.database.internal.DatabaseRules.CITIES_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.COUNTRIES_TABLE_NAME

object InsertQueries {
    const val INSERT_COUNTRY = "insert into $COUNTRIES_TABLE_NAME values(null, \'%s\')"

    const val INSERT_CITY = "insert into $CITIES_TABLE_NAME values (%d, \'%s\', %d, %f, %f)"
}