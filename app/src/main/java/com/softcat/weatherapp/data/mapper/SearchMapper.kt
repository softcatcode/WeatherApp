package com.softcat.weatherapp.data.mapper

import com.softcat.weatherapp.data.network.dto.CityDto
import com.softcat.weatherapp.domain.entity.City

fun CityDto.toEntity() = City(id, name, country)

fun List<CityDto>.toEntities() = map { it.toEntity() }