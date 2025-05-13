package com.softcat.data.mapper

import com.softcat.domain.entity.City
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel

fun City.toDbModel(countryId: Int): CityDbModel = CityDbModel(
    id = id,
    name = name,
    countryId = countryId,
    latitude = latitude,
    longitude = longitude
)

fun CityDbModel.toEntity(country: String): City = City(
    id = id,
    name = name,
    country = country,
    latitude = latitude,
    longitude = longitude,
)

fun List<CityDbModel>.toEntities(countries: List<CountryDbModel>) = map { city ->
    val country = countries.find { city.countryId == it.id }
    val countryName = country?.name ?: ""
    city.toEntity(countryName)
}