package com.softcat.data.implementations

import com.softcat.domain.entity.Weather
import com.softcat.domain.interfaces.WeatherRepository
import com.softcat.data.mapper.toEntity
import com.softcat.data.network.api.ApiService
import com.softcat.domain.entity.Forecast
import timber.log.Timber
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): WeatherRepository {
    override suspend fun getWeather(cityId: Int): Weather {
        Timber.i("${this::class.simpleName}.getWeather($cityId)")
        return apiService.loadCurrentWeather(cityIdToQuery(cityId)).toEntity()
    }

    override suspend fun getForecast(cityId: Int): Forecast {
        Timber.i("${this::class.simpleName}.getForecast($cityId)")
        return apiService.loadForecast(cityIdToQuery(cityId)).toEntity()
    }

    override suspend fun getTodayLocalForecast(cityId: Int): List<Weather> {
        Timber.i("${this::class.simpleName}.getTodayLocalForecast($cityId)")
        return apiService.loadForecast(cityIdToQuery(cityId), 1).toEntity().hourly.first()
    }

    companion object {
        private const val PREFIX_CITY_ID = "id:"

        fun cityIdToQuery(cityId: Int) = "$PREFIX_CITY_ID$cityId"
    }
}