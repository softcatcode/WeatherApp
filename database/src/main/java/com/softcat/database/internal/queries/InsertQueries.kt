package com.softcat.database.internal.queries

import com.softcat.database.internal.DatabaseRules.CITIES_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.COUNTRIES_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.CURRENT_WEATHER_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.WEATHER_TABLE_NAME

object InsertQueries {
    const val INSERT_COUNTRY = "insert into $COUNTRIES_TABLE_NAME values(null, \'%s\')"

    const val INSERT_CITY = "insert or replace into $CITIES_TABLE_NAME values (%d, \'%s\', %d, %f, %f)"

    const val INSERT_WEATHER = """
        insert into $WEATHER_TABLE_NAME values
            (null, %d, %d, %d, %f, %d, %d, %d, %d, %f, '%s', '%s', '%s', '%s', %d, %d, %d, '%s', %d);
    """

    const val INSERT_CURRENT_WEATHER = """
        insert into $CURRENT_WEATHER_TABLE_NAME values
            (null, %d, %d, %f, %d, %d, %d, %d, %d, %d, %d, %d, %f);
    """
}