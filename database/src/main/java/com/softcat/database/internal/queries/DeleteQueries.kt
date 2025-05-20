package com.softcat.database.internal.queries

import com.softcat.database.internal.DatabaseRules.CITIES_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.COUNTRIES_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.DATABASE_NAME
import com.softcat.database.internal.DatabaseRules.WEATHER_TYPE_TABLE_NAME

internal object DeleteQueries {
    const val DROP_DATABASE = "drop database if exists $DATABASE_NAME;"

    const val DROP_COUNTRIES = "drop table $COUNTRIES_TABLE_NAME;"

    const val DROP_CITIES = "drop table $CITIES_TABLE_NAME;"

    const val DELETE_COUNTRY = "delete from $COUNTRIES_TABLE_NAME where id = %d;"

    const val DELETE_CITY = "delete from $CITIES_TABLE_NAME where id = %d;"

    const val DROP_WEATHER_TYPES = "drop table if exists $WEATHER_TYPE_TABLE_NAME;"
}