package com.softcat.data.implementations

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
import com.softcat.domain.interfaces.LocationRepository
import timber.log.Timber
import javax.inject.Inject
import kotlin.NullPointerException

class LocationRepositoryImpl @Inject constructor(
    private val geocoder: Geocoder
): LocationRepository {
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
            Timber.i("Location permissions denied, getLocation terminated.")
            return
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Timber.i("GPS provider is disabled, getLocation terminated.")
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val getLocationTaskResult: Task<Location> = fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            CancellationTokenSource().token
        )
        getLocationTaskResult.addOnCompleteListener {
            try {
                Timber.i("Fused location client replied: ${it.result}.")
                onLocationLoaded(it.result)
            } catch (e: NullPointerException) {
                Timber.i("Location is undefined: fused location client returned null.")
            }
        }
    }

    override fun getCurrentCity(
        context: Context,
        onCityNameLoaded: (String) -> Unit
    ) {
        Timber.i("${this::class.simpleName}.getCurrentCity($context, $onCityNameLoaded)")
        getLocation(context) { location ->
            val latitude = location.latitude
            val longitude = location.longitude
            geocoder.getFromLocation(latitude, longitude, 1) { addressList ->
                val cityName = addressList.getOrNull(0)?.adminArea.orEmpty()
                onCityNameLoaded(cityName)
            }
        }
    }
}