package com.softcat.database.internal.queries

import com.softcat.database.internal.DatabaseRules.CITIES_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.COUNTRIES_TABLE_NAME
import com.softcat.database.internal.DatabaseRules.DATABASE_NAME

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
}