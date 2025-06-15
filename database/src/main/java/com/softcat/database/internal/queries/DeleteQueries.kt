package com.softcat.database.internal.queries

import com.softcat.database.internal.DatabaseRules.CITIES_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.COUNTRIES_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.CURRENT_WEATHER_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.PLOT_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.WEATHER_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.WEATHER_TYPE_TABLE_NAME

internal object DeleteQueries {
    //const val DROP_DATABASE = "drop database if exists $DATABASE_NAME;"

    const val DROP_COUNTRIES = "drop table if exists $COUNTRIES_TABLE_NAME;"

    const val DROP_CITIES = "drop table if exists $CITIES_TABLE_NAME;"

    const val DELETE_COUNTRY = "delete from $COUNTRIES_TABLE_NAME where id = %d;"

    const val DROP_PLOTS = "drop table if exists $PLOT_TABLE_NAME;"

    const val DELETE_CITY = "delete from $CITIES_TABLE_NAME where id = %d;"

    const val DROP_WEATHER_TYPES = "drop table if exists $WEATHER_TYPE_TABLE_NAME;"

    const val DROP_WEATHER = "drop table if exists $WEATHER_TABLE_NAME;"

    const val DROP_CURRENT_WEATHER = "drop table if exists $CURRENT_WEATHER_TABLE_NAME;"

    const val DELETE_WEATHER = """
        delete from $WEATHER_TABLE_NAME where cityId = %d and timeEpoch = %d;
    """

    const val DELETE_CURRENT_WEATHER = """
        delete from $CURRENT_WEATHER_TABLE_NAME where cityId = %d and timeEpoch = %d;
    """

    const val DELETE_PLOT = """
        delete from $PLOT_TABLE_NAME where id = %d;
    """

    const val DELETE_WEATHER_TYPE = """
        delete from $WEATHER_TYPE_TABLE_NAME where code = %d;
    """
}