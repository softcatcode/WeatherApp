package com.softcat.data.mapper

import com.softcat.domain.entity.City
import com.softcat.data.local.model.CityDbModel

fun City.toDbModel(): CityDbModel = CityDbModel(id, name, country)

fun CityDbModel.toEntity(): City = City(id, name, country)

fun List<CityDbModel>.toEntities() = map { it.toEntity() }