package com.softcat.weatherapp.data.mapper

import com.softcat.domain.entity.City
import com.softcat.weatherapp.data.network.dto.CityDto

fun CityDto.toEntity() = City(id, name, country)

fun List<CityDto>.toEntities() = map { it.toEntity() }