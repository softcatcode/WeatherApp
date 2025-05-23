package com.softcat.weatherapp.presentation

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.arkivanov.decompose.defaultComponentContext
import com.softcat.domain.useCases.LoadWeatherTypesUseCase
import com.softcat.weatherapp.LogsTree
import com.softcat.weatherapp.WeatherApplication
import com.softcat.weatherapp.presentation.root.RootComponentImpl
import com.softcat.weatherapp.presentation.root.RootContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: RootComponentImpl.Factory

    @Inject
    lateinit var loadWeatherTypesUseCase: LoadWeatherTypesUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as WeatherApplication).component.inject(this)
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            loadWeatherTypesUseCase()
        }

        enableEdgeToEdge()
        Timber.plant(LogsTree)
        requestPermissions()
        setContent {
            RootContent(component = rootComponentFactory.create(defaultComponentContext()))
        }
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val firstPermission = permissions.getOrDefault(
            Manifest.permission.ACCESS_FINE_LOCATION,
            false
        )
        if (!firstPermission)
            Timber.w("Permission ACCESS_FINE_LOCATION is denied.")

        val secondPermission = permissions.getOrDefault(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            false
        )
        if (!secondPermission)
            Timber.w("Permission ACCESS_COARSE_LOCATION is denied.")

        if (!firstPermission || !secondPermission) {
            Toast.makeText(this, "permissions are denied", Toast.LENGTH_SHORT).show()
            requestPermissions()
        } else
            Timber.i("Permissions are granted: $permissions.")
    }

    private fun requestPermissions() {
        Timber.i("Permissions are requested.")
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}