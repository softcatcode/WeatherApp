package com.softcat.data.network.api

import com.softcat.data.network.dto.CityDto
import com.softcat.data.network.dto.CurrentWeatherResponse
import com.softcat.data.network.dto.WeatherForecastDto
import com.softcat.data.network.dto.WeatherTypeInfoDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewWeatherApiService {

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

    @GET("conditions.json")
    suspend fun loadWeatherConditions(): List<WeatherTypeInfoDto>

    @GET("swagger")
    fun getSwaggerUI(): Call<ResponseBody>

    companion object {
        const val BASE_URL = "http://192.168.0.104:8080/"
    }
}