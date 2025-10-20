package com.softcat.data.network.api

import com.softcat.data.network.dto.WeatherTypeInfoDto
import retrofit2.http.GET

interface DocsApiService {

    @GET("conditions.json")
    suspend fun loadWeatherConditions(): List<WeatherTypeInfoDto>
}