package com.softcat.data.network.api

import com.softcat.data.network.dto.CityDto
import com.softcat.data.network.dto.CurrentWeatherResponse
import com.softcat.data.network.dto.WeatherForecastDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("current.json?")
    suspend fun loadCurrentWeather(
        @Query("q") query: String
    ): CurrentWeatherResponse

    @GET("forecast.json?")
    suspend fun loadForecast(
        @Query("q") query: String,
        @Query("days") dayCount: Int = 2
    ): WeatherForecastDto

    @GET("history.json?")
    suspend fun loadWeatherHistory(
        @Query("q") query: String,
        @Query("dt") startDate: String,
        @Query("end_dt") endDate: String,
    ): WeatherForecastDto

    @GET("search.json?")
    suspend fun searchCity(
        @Query("q") query: String
    ): List<CityDto>

}