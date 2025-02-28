package com.softcat.weatherapp.presentation.widgets.hourly

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.softcat.weatherapp.domain.useCases.GetTodayForecastUseCase
import com.softcat.weatherapp.domain.useCases.SearchCityUseCase
import com.softcat.weatherapp.domain.entity.Weather
import java.util.Locale

class TodayHourlyWeatherWidget(
    private val getForecastUseCase: GetTodayForecastUseCase,
    private val searchUseCase: SearchCityUseCase
): GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val weatherList = getHourlyWeather(context)
        provideContent {
            Test(weatherList)
        }
    }

    private var cityName: String? = null
    private var cityNameLoading: Boolean = true

    private suspend fun getHourlyWeather(context: Context): List<Weather> {
        val permissionCheck = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!permissionCheck) {
            return emptyList()
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return emptyList()
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//        val getLocationTaskResult: Task<Location> = fusedLocationClient.getCurrentLocation(
//            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
//            CancellationTokenSource().token
//        )
//        getLocationTaskResult.addOnCompleteListener {
//            val location = it.result
//            getCityName(context, location.latitude, location.longitude) { result ->
//                cityName = result
//                cityNameLoading = false
//            }
//        }
//        while (cityNameLoading) { }
        cityName = "Москва"

        val query = cityName ?: return emptyList()

        val cityId = searchUseCase(query).firstOrNull()?.id ?: return emptyList()
        return getForecastUseCase(cityId)
    }

    private fun getCityName(
        context: Context,
        latitude: Double,
        longitude: Double,
        onCityNameObtained: (String) -> Unit
    ) = Geocoder(context, Locale.getDefault()).getFromLocation(
        latitude,
        longitude,
        1
    ) { addressList ->
        val cityName = addressList.getOrNull(0)?.adminArea.orEmpty()
        onCityNameObtained(cityName)
    }
}