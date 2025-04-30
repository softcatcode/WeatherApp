package com.softcat.weatherapp.presentation.techInterface

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.softcat.weatherapp.domain.useCases.AddToFavouriteUseCase
import com.softcat.weatherapp.domain.useCases.GetFavouriteCitiesUseCase
import com.softcat.weatherapp.domain.useCases.GetForecastUseCase
import com.softcat.weatherapp.domain.useCases.GetLastCityFromDatastoreUseCase
import com.softcat.weatherapp.domain.useCases.GetTodayForecastUseCase
import com.softcat.weatherapp.domain.useCases.ObserveIsFavouriteUseCase
import com.softcat.weatherapp.domain.useCases.RemoveFromFavouriteUseCase
import com.softcat.weatherapp.domain.useCases.SaveToDatastoreUseCase
import com.softcat.weatherapp.domain.useCases.SearchCityUseCase
import com.softcat.weatherapp.domain.useCases.GetCurrentCityNameUseCase as GetCurrentCityNameUseCase1

@Composable
fun UseCases(
    modifier: Modifier = Modifier,
    useCases: List<Any>,
    onUseCaseClicked: (String) -> Unit
) {

    val cellSize = 185.dp
    LazyVerticalGrid(
        modifier = Modifier
            .then(modifier),
        columns = GridCells.FixedSize(cellSize),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(items = useCases) {
            val name = it::class.simpleName.toString()
            Button(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .border(1.dp, Color.Black),
                onClick = { onUseCaseClicked(name) },
                content = { Text(name, color = Color.Black) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechInterfaceScreen(
    getCurrentCityNameUseCase: GetCurrentCityNameUseCase1,
    getTodayForecastUseCase: GetTodayForecastUseCase,
    getForecastUseCase: GetForecastUseCase,
    getLastCityFromDatastoreUseCase: GetLastCityFromDatastoreUseCase,
    addToFavouritesUseCase: AddToFavouriteUseCase,
    removeFromFavouriteUseCase: RemoveFromFavouriteUseCase,
    saveToDatastoreUseCase: SaveToDatastoreUseCase,
    searchUseCase: SearchCityUseCase,
    observeIsFavouriteUseCase: ObserveIsFavouriteUseCase,
    getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase
) {
    val useCaseName = remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .background(Color.Blue),
                title = { Text("Use-cases") },
                navigationIcon = {
                    IconButton(
                        onClick = { useCaseName.value = "" },
                        content = { Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White) }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { paddings ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddings),
            contentAlignment = Alignment.Center
        ) {
            when (useCaseName.value) {
                GetCurrentCityNameUseCase1::class.simpleName -> GetCurrentCityNameUseCaseInterface(getCurrentCityNameUseCase)
                GetTodayForecastUseCase::class.simpleName -> GetTodayForecastUseCaseInterface(getTodayForecastUseCase)
                GetForecastUseCase::class.simpleName -> GetForecastUseCaseInterface(getForecastUseCase)
                GetLastCityFromDatastoreUseCase::class.simpleName -> GetLastCityFromDatastoreUseCaseInterface(getLastCityFromDatastoreUseCase)
                AddToFavouriteUseCase::class.simpleName -> AddToFavouriteUseCaseInterface(addToFavouritesUseCase, searchUseCase)
                RemoveFromFavouriteUseCase::class.simpleName -> RemoveFromFavouriteUseCaseInterface(removeFromFavouriteUseCase, searchUseCase)
                SaveToDatastoreUseCase::class.simpleName -> SaveToDatastoreUseCaseInterface(saveToDatastoreUseCase)
                SearchCityUseCase::class.simpleName -> SearchCityUseCaseInterface(searchUseCase)
                ObserveIsFavouriteUseCase::class.simpleName -> ObserveIsFavouriteUseCaseInterface(observeIsFavouriteUseCase)
                GetFavouriteCitiesUseCase::class.simpleName -> GetFavouriteCitiesUseCaseInterface(getFavouriteCitiesUseCase)
                else -> {
                    UseCases(
                        useCases = listOf(
                            getCurrentCityNameUseCase, getTodayForecastUseCase, getForecastUseCase,
                            getLastCityFromDatastoreUseCase, addToFavouritesUseCase, removeFromFavouriteUseCase,
                            saveToDatastoreUseCase, searchUseCase, observeIsFavouriteUseCase,
                            getFavouriteCitiesUseCase
                        ),
                        onUseCaseClicked = { useCaseName.value = it }
                    )
                }
            }
        }
    }
}