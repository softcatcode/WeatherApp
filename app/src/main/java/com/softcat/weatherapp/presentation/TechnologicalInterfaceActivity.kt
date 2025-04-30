package com.softcat.weatherapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.softcat.weatherapp.WeatherApplication
import com.softcat.weatherapp.domain.useCases.AddToFavouriteUseCase
import com.softcat.weatherapp.domain.useCases.GetCurrentCityNameUseCase
import com.softcat.weatherapp.domain.useCases.GetFavouriteCitiesUseCase
import com.softcat.weatherapp.domain.useCases.GetForecastUseCase
import com.softcat.weatherapp.domain.useCases.GetLastCityFromDatastoreUseCase
import com.softcat.weatherapp.domain.useCases.GetTodayForecastUseCase
import com.softcat.weatherapp.domain.useCases.ObserveIsFavouriteUseCase
import com.softcat.weatherapp.domain.useCases.RemoveFromFavouriteUseCase
import com.softcat.weatherapp.domain.useCases.SaveToDatastoreUseCase
import com.softcat.weatherapp.domain.useCases.SearchCityUseCase
import com.softcat.weatherapp.presentation.techInterface.TechInterfaceScreen
import javax.inject.Inject

class TechnologicalInterfaceActivity : ComponentActivity() {

    @Inject
    lateinit var getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase

    @Inject
    lateinit var getCurrentCityNameUseCase: GetCurrentCityNameUseCase

    @Inject
    lateinit var getTodayForecastUseCase: GetTodayForecastUseCase

    @Inject
    lateinit var getForecastUseCase: GetForecastUseCase

    @Inject
    lateinit var getLastCityFromDatastoreUseCase: GetLastCityFromDatastoreUseCase

    @Inject
    lateinit var addToFavouritesUseCase: AddToFavouriteUseCase

    @Inject
    lateinit var removeFromFavouriteUseCase: RemoveFromFavouriteUseCase

    @Inject
    lateinit var saveToDatastoreUseCase: SaveToDatastoreUseCase

    @Inject
    lateinit var searchUseCase: SearchCityUseCase

    @Inject
    lateinit var observeIsFavouriteUseCase: ObserveIsFavouriteUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as WeatherApplication).component.inject(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TechInterfaceScreen(
                getCurrentCityNameUseCase = getCurrentCityNameUseCase,
                getTodayForecastUseCase = getTodayForecastUseCase,
                getForecastUseCase = getForecastUseCase,
                getLastCityFromDatastoreUseCase = getLastCityFromDatastoreUseCase,
                addToFavouritesUseCase = addToFavouritesUseCase,
                removeFromFavouriteUseCase = removeFromFavouriteUseCase,
                saveToDatastoreUseCase = saveToDatastoreUseCase,
                searchUseCase = searchUseCase,
                observeIsFavouriteUseCase = observeIsFavouriteUseCase,
                getFavouriteCitiesUseCase = getFavouriteCitiesUseCase
            )
        }
    }
}