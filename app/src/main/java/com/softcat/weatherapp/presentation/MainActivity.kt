package com.softcat.weatherapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
        setContent {
            RootContent(component = rootComponentFactory.create(defaultComponentContext()))
        }
    }
}