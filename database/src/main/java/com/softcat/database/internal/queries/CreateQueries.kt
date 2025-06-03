package com.softcat.database.internal.queries

import com.softcat.database.internal.DatabaseRules.CITIES_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.COUNTRIES_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.CURRENT_WEATHER_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.PLOT_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.WEATHER_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.WEATHER_TYPE_TABLE_NAME

internal object CreateQueries {
    // const val CREATE_DATABASE = "create database if not exists $DATABASE_NAME;"

    const val CREATE_COUNTRIES = """
        create table if not exists $COUNTRIES_TABLE_NAME(
            id Integer not null primary key AUTOINCREMENT,
            name Text not null
        );
    """

    const val CREATE_CITIES = """
        create table if not exists $CITIES_TABLE_NAME(
            id Integer not null primary key,
            name Text not null,
            countryId Integer not null,
            latitude Float not null,
            longitude Float not null,
            
            foreign key (countryId) references $COUNTRIES_TABLE_NAME(id)
        );
    """

    const val CREATE_WEATHER_TYPES = """
        create table if not exists $WEATHER_TYPE_TABLE_NAME(
            code Integer not null primary key,
            dayDescription Text not null,
            nightDescription Text not null,
            url Text not null,
            icon Blob
        );
    """

    const val CREATE_WEATHER = """
        create table if not exists $WEATHER_TABLE_NAME(
            id Integer not null primary key AUTOINCREMENT,
            timeEpoch Integer not null,
            cityId Integer not null,
            type Integer not null,
            avgTemp Float not null,
            humidity Integer not null,
            windSpeed Integer not null,
            snowVolume Integer not null,
            precipitations Integer not null,
            vision Float not null,
            sunriseTime Integer not null,
            sunsetTime Integer not null,
            moonriseTime Integer not null,
            moonsetTime Integer not null,
            moonIllumination Integer not null,
            moonPhase Text not null,
            rainChance Integer not null,
            
            foreign key (cityId) references $CITIES_TABLE_NAME(id),
            foreign key (type) references $WEATHER_TYPE_TABLE_NAME(code)
        );
    """

    const val CREATE_CURRENT_WEATHER = """
        create table if not exists $CURRENT_WEATHER_TABLE_NAME(
            id Integer not null primary key AUTOINCREMENT,
            cityId Integer not null,
            timeEpoch Integer not null,
            tempC Float not null,
            feelsLike Integer not null,
            isDay Integer not null,
            type Integer not null,
            windSpeed Integer not null,
            precipitations Integer not null,
            snow Integer not null,
            humidity Integer not null,
            cloud Integer not null,
            vision Float not null,
            
            foreign key (cityId) references $CITIES_TABLE_NAME(id),
            foreign key (type) references $WEATHER_TYPE_TABLE_NAME(code)
        );
    """

    const val CREATE_PLOT_TABLE = """
        create table if not exists $PLOT_TABLE_NAME(
            id Integer not null primary key AUTOINCREMENT,
            parameter Text not null,
            parameter_values Text not null,
            time_values Text not null,
            cityId Integer not null,
            authorId Text not null,
            description Text,
            
            foreign key (cityId) references $CITIES_TABLE_NAME(id)
        );
    """
}