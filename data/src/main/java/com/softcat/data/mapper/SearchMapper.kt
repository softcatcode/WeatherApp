package com.softcat.data.mapper

import com.softcat.data.network.dto.CityDto
import com.softcat.domain.entity.City

fun CityDto.toEntity() = City(id, name, country)

fun List<CityDto>.toEntities() = map { it.toEntity() }