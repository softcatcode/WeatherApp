package com.softcat.weatherapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.softcat.weatherapp.WeatherApplication
import com.softcat.weatherapp.domain.useCases.AddToFavouriteUseCase
import com.softcat.weatherapp.domain.useCases.SearchCityUseCase
import com.softcat.weatherapp.presentation.root.RootComponentImpl
import com.softcat.weatherapp.presentation.root.RootContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: RootComponentImpl.Factory

    @Inject
    lateinit var searchCityUseCase: SearchCityUseCase

    @Inject
    lateinit var addToFavouritesUseCase: AddToFavouriteUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as WeatherApplication).component.inject(this)
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            searchCityUseCase("пон").forEach { addToFavouritesUseCase(it) }
        }

        enableEdgeToEdge()
        setContent {
            RootContent(component = rootComponentFactory.create(defaultComponentContext()))
        }
    }
}