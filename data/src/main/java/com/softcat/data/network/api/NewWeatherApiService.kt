package com.softcat.data.network.api

import com.softcat.data.network.dto.CityDto
import com.softcat.data.network.dto.CurrentWeatherResponse
import com.softcat.data.network.dto.DeleteUserResponseDto
import com.softcat.data.network.dto.UserDto
import com.softcat.data.network.dto.WeatherForecastDto
import com.softcat.data.network.dto.WeatherTypeInfoDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NewWeatherApiService {

    @GET("cities/{city_id}/hours_weather")
    suspend fun loadHoursWeather(
        @Path("city_id") cityId: Int,
        @Query("user_id") userId: String
    ): CurrentWeatherResponse

    @GET("cities/{city_id}/forecasts")
    suspend fun loadForecast(
        @Path("city_id") cityId: Int,
        @Query("user_id") userId: String,
        @Query("start") startEpoch: Long,
        @Query("end") endEpoch: Long = -1L
    ): WeatherForecastDto

    @GET("cities")
    suspend fun searchCity(
        @Query("user_id") userId: String,
        @Query("query_line") query: String
    ): List<CityDto>

    @GET("conditions")
    suspend fun loadWeatherConditions(
        @Query("user_id") userId: String
    ): List<WeatherTypeInfoDto>

    @GET("users")
    suspend fun logIn(
        @Query("user_name") userName: String,
        @Query("password") password: String
    ): UserDto

    @POST("users")
    suspend fun createUser(
        @Query("user_name") userName: String,
        @Query("email") email: String,
        @Query("password") password: String
    ): UserDto

    @PUT("users")
    suspend fun updateUser(
        @Query("user_id") id: String,
        @Query("user_name") userName: String,
        @Query("email") email: String,
        @Query("password") password: String
    ): UserDto

    @DELETE("users")
    suspend fun deleteUser(
        @Query("id") id: String
    ): DeleteUserResponseDto

    @GET("users/{user_id}/favourites")
    suspend fun getFavouriteCities(
        @Path("user_id") userId: String
    ): List<CityDto>

    @PUT("users/{user_id}/favourites")
    suspend fun addToFavourites(
        @Path("user_id") userId: String,
        @Query("city_id") cityId: Int
    ): Response<Unit>

    @DELETE("users/{user_id}/favourites")
    suspend fun removeFromFavourites(
        @Path("user_id") userId: String,
        @Query("city_id") cityId: Int
    ): Response<Unit>

    companion object {
        //const val BASE_URL = "http://192.168.0.104:8080/api/v1/"
        const val BASE_URL = "http://10.236.95.2:8080/api/v1/"
    }
}