package com.softcat.weatherapp.data.network.api

import com.softcat.weatherapp.data.network.dto.CityDto
import com.softcat.weatherapp.data.network.dto.WeatherCurrentDto
import com.softcat.weatherapp.data.network.dto.WeatherDto
import com.softcat.weatherapp.data.network.dto.WeatherForecastDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("current.json?key=$KEY")
    suspend fun loadCurrentWeather(
        @Query("q") query: String
    ): WeatherCurrentDto

    @GET("forecast.json?key=$KEY")
    suspend fun oadForecast(
        @Query("q") query: String,
        @Query("days") dayCount: Int = 4
    ): WeatherForecastDto

    @GET("search.json?key=$KEY")
    suspend fun searchCity(
        @Query("q") query: String
    ): List<CityDto>

    companion object {
        private const val KEY = ""
    }

}