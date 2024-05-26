package com.softcat.weatherapp.data.mapper

import com.softcat.weatherapp.data.local.model.CityDbModel
import com.softcat.weatherapp.domain.entity.City

fun City.toDbModel(): CityDbModel = CityDbModel(id, name, country)

fun CityDbModel.toEntity(): City = City(id, name, country)

fun List<City>.toDbModels() = map { it.toDbModel() }

fun List<CityDbModel>.toEntities() = map { it.toEntity() }