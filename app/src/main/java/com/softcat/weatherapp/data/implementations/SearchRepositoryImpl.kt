package com.softcat.weatherapp.data.implementations

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.softcat.weatherapp.data.mapper.toEntities
import com.softcat.weatherapp.data.network.api.ApiService
import com.softcat.weatherapp.domain.interfaces.SearchRepository
import java.util.Locale
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): SearchRepository {
    override suspend fun search(query: String) = apiService.searchCity(query).toEntities()

    private fun getLocation(
        context: Context,
        onLocationLoaded: (Location) -> Unit
    ) {
        val permissionCheck = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!permissionCheck) {
            return
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val getLocationTaskResult: Task<Location> = fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            CancellationTokenSource().token
        )
        getLocationTaskResult.addOnCompleteListener {
            onLocationLoaded(it.result)
        }
    }

    override fun getCurrentCity(
        context: Context,
        onCityNameLoaded: (String) -> Unit
    ) = getLocation(context) { location ->
        val geoCoder = Geocoder(context, Locale.getDefault())
        val latitude = location.latitude
        val longitude = location.longitude
        geoCoder.getFromLocation(latitude, longitude, 1) { addressList ->
            val cityName = addressList.getOrNull(0)?.adminArea.orEmpty()
            onCityNameLoaded(cityName)
        }
    }
}