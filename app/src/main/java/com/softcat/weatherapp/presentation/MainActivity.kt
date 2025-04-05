package com.softcat.weatherapp.presentation

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.arkivanov.decompose.defaultComponentContext
import com.softcat.weatherapp.WeatherApplication
import com.softcat.weatherapp.presentation.root.RootComponentImpl
import com.softcat.weatherapp.presentation.root.RootContent
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: RootComponentImpl.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as WeatherApplication).component.inject(this)
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
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
        val secondPermission = permissions.getOrDefault(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            false
        )
        if (!firstPermission || !secondPermission) {
            Toast.makeText(this, "permissions are denied", Toast.LENGTH_SHORT).show()
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}