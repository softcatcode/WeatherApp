package com.softcat.weatherapp.data.implementations

import com.softcat.weatherapp.data.mapper.toEntity
import com.softcat.weatherapp.data.network.api.ApiService
import com.softcat.weatherapp.domain.interfaces.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): WeatherRepository {
    override suspend fun getWeather(cityId: Int) = apiService.loadCurrentWeather(cityIdToQuery(cityId)).toEntity()

    override suspend fun getForecast(cityId: Int) = apiService.loadForecast(cityIdToQuery(cityId)).toEntity()

    companion object {
        private const val PREFIX_CITY_ID = "id:"

        fun cityIdToQuery(cityId: Int) = "$PREFIX_CITY_ID$cityId"
    }
}