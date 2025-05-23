package com.softcat.data

import com.softcat.data.implementations.WeatherRepositoryImpl
import com.softcat.data.mapper.toCalendar
import com.softcat.data.network.dto.ForecastDayDto
import com.softcat.data.network.dto.CurrentWeatherDto
import com.softcat.data.objectMocks.apiServiceMock
import com.softcat.data.objectMocks.currentWeatherDto
import com.softcat.data.objectMocks.setupApiServiceMock
import com.softcat.data.objectMocks.weatherForecastDto
import com.softcat.domain.entity.Weather
import com.softcat.domain.entity.weatherTypeOf
import com.softcat.domain.interfaces.WeatherRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Before
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import kotlin.random.Random

class WeatherRepositoryTest {

    val repository: WeatherRepository = WeatherRepositoryImpl(
        apiService = apiServiceMock
    )

    @Before
    fun setupMocks() {
        setupApiServiceMock()
    }

    private fun checkWeatherDtoConversion(result: Weather, real: CurrentWeatherDto) {
        with (real) {
            assert(result.conditionText == condition.description)
            checkUrlConversion(result.conditionUrl, condition.iconUrl)
            assert(result.type == weatherTypeOf(condition.code))
            assert(result.tempC == tempC)
            assert(result.feelsLike == feelsLike)
            assert(result.humidity == humidity)
            assert(result.precipitations == precipitations)
            assert(result.snowVolume == snowVolume)
            assert(result.windSpeed == windSpeed)
        }
    }

    private fun checkHourlyWeatherConversion(result: List<Weather>, real: List<CurrentWeatherDto>) {
        assert(result.size == real.size)
        for (i in 0 until result.size) {
            checkWeatherDtoConversion(result[i], real[i])
        }
    }

    private fun checkUrlConversion(result: String, initial: String) {
        assert(result.substring(0, 6) == "https:")
        val tmp = result.substring(6, result.length).replace("128x128", "64x64")
        assert(tmp == initial)
    }

    private fun checkDayDtoConversion(weather: Weather, forecastDayDto: ForecastDayDto) {
        with (forecastDayDto.weather) {
            assert(weather.tempC == tempC)
            assert(weather.feelsLike == feelsLike)
            assert(weather.windSpeed == windSpeed)
            assert(weather.humidity == humidity)
            assert(weather.snowVolume == snowVolume)
            assert(weather.precipitations == precipitations)
            checkUrlConversion(weather.conditionUrl, condition.iconUrl)
            assert(weather.conditionText == condition.description)
            assert(weather.type == weatherTypeOf(condition.code))
        }
        assert(weather.formattedDate == forecastDayDto.formattedDate)
        assert(weather.date == forecastDayDto.date.toCalendar())
        with (forecastDayDto.astrologicalParams) {
            assert(weather.astrologicalParams?.sunsetTime == sunsetTime)
            assert(weather.astrologicalParams?.sunriseTime == sunriseTime)
            assert(weather.astrologicalParams?.moonsetTime == moonsetTime)
            assert(weather.astrologicalParams?.moonriseTime == moonriseTime)
        }
    }

    @Test
    fun getWeatherTest() = runBlocking {
        val cityId = Random.nextInt(1, 100)

        val result = repository.getWeather(cityId)

        verify(apiServiceMock, times(1))
            .loadCurrentWeather("id:$cityId")
        checkWeatherDtoConversion(result, currentWeatherDto.value)
    }

    @Test
    fun getForecastTest(): Unit = runBlocking {
        val cityId = Random.nextInt(1, 100)
        val result = repository.getForecast(cityId)

        verify(apiServiceMock, times(1))
            .loadForecast("id:$cityId")
        checkWeatherDtoConversion(result.weather, weatherForecastDto.current)
        weatherForecastDto.forecastDto.days.forEachIndexed { index, dayDto ->
            checkHourlyWeatherConversion(result.hourly[index], dayDto.hoursWeather)
            checkDayDtoConversion(result.upcoming[index], dayDto)
        }
    }

    @Test
    fun getTodayLocalForecastTest() = runBlocking {
        val cityId = Random.nextInt(1, 100)
        val result = repository.getTodayLocalForecast(cityId)
        verify(apiServiceMock, times(1))
            .loadForecast("id:$cityId", 1)
        assert(result.size == 24)
        val hourlyWeatherDto = weatherForecastDto.forecastDto.days.first().hoursWeather
        checkHourlyWeatherConversion(result, hourlyWeatherDto)
    }
}