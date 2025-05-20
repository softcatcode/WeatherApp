package com.softcat.database.internal.queries

import com.softcat.database.internal.DatabaseRules.CITIES_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.COUNTRIES_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.CURRENT_WEATHER_TABLE_NAME
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
            dayDescription Char(30) not null,
            nightDescription Char(30) not null,
            url Char(100) not null,
            icon Blob
        );
    """

    const val CREATE_WEATHER = """
        create table if not exists $WEATHER_TABLE_NAME(
            id: Integer not null primary key AUTOINCREMENT,
            timeEpoch: Integer not null,
            cityId: Integer not null,
            type: Integer not null,
            avgTemp: Float not null,
            humidity: Integer not null,
            windSpeed: Integer not null,
            snowVolume: Integer not null,
            precipitations: Integer not null,
            vision: Float not null,
            sunriseTime: Char(10) not null,
            sunsetTime: String not null,
            moonriseTime: Char(10) not null,
            moonsetTime: Char(10) not null,
            moonIllumination: Integer not null,
            isSunUp: Integer not null,
            isMoonUp: Integer not null,
            moonPhase: Char(10) not null,
            rainChance: Integer not null,
            
            foreign key (cityId) references $CITIES_TABLE_NAME(id),
            foreign key (type) references $WEATHER_TYPE_TABLE_NAME(id)
        );
    """

    const val CREATE_CURRENT_WEATHER = """
        create table if not exists $CURRENT_WEATHER_TABLE_NAME(
            id: Integer not null primary key AUTOINCREMENT,
            cityId: Integer not null,
            timeEpoch: Integer not null,
            tempC: Float not null,
            feelsLike: Integer not null,
            isDay: Integer not null,
            type: Integer not null,
            windSpeed: Integer not null,
            precipitations: Integer not null,
            snow: Integer not null,
            humidity: Integer not null,
            cloud: Integer not null,
            vision: Float not null,
            
            foreign key (cityId) references $CITIES_TABLE_NAME(id),
            foreign key (type) references $WEATHER_TYPE_TABLE_NAME(id)
        );
    """
}