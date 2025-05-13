package com.softcat.database.internal.queries

import com.softcat.database.internal.DatabaseRules.CITIES_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.COUNTRIES_TABLE_NAME

internal object GetDataQueries {
    const val GET_CITIES = "select * from $CITIES_TABLE_NAME where 1;"

    const val GET_COUNTRIES = "select * from $COUNTRIES_TABLE_NAME where 1;"

    const val GET_CITY = "select * from $CITIES_TABLE_NAME where id = %d;"

    const val GET_COUNTRY_ID = "select id from $COUNTRIES_TABLE_NAME where name = \'%s\';"
}