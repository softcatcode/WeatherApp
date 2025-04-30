package com.softcat.weatherapp.data.mapper

import com.softcat.domain.entity.City
import com.softcat.weatherapp.data.local.model.CityDbModel

fun City.toDbModel(): CityDbModel = CityDbModel(id, name, country)

fun CityDbModel.toEntity(): City = City(id, name, country)

fun List<City>.toDbModels() = map { it.toDbModel() }

fun List<CityDbModel>.toEntities() = map { it.toEntity() }