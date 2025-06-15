package com.softcat.database.internal.queries

import com.softcat.database.internal.DatabaseRules.COUNTRIES_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.CURRENT_WEATHER_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.PLOT_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.WEATHER_TABLE_NAME

object IndexQueries {

    const val CREATE_COUNTRY_INDEX = """
        create index index_${COUNTRIES_TABLE_NAME}_name on $COUNTRIES_TABLE_NAME(name);
    """

    const val CREATE_PLOT_INDEX = """
        create index index_${PLOT_TABLE_NAME}_authorId
        on $PLOT_TABLE_NAME(authorId);
    """

    const val CREATE_WEATHER_INDEX = """
        create index index_${WEATHER_TABLE_NAME}_cityId_timeEpoch
        on $WEATHER_TABLE_NAME(cityId, timeEpoch);
    """

    const val CREATE_CURRENT_WEATHER_INDEX = """
        create index index_${CURRENT_WEATHER_TABLE_NAME}_cityId_timeEpoch
        on $CURRENT_WEATHER_TABLE_NAME(cityId, timeEpoch);
    """
}