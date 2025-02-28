package com.softcat.weatherapp.data.implementations

import android.content.Context
import android.location.Geocoder
import com.softcat.weatherapp.data.mapper.toEntities
import com.softcat.weatherapp.data.network.api.ApiService
import com.softcat.weatherapp.domain.interfaces.SearchRepository
import java.util.Locale
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): SearchRepository {
    override suspend fun search(query: String) = apiService.searchCity(query).toEntities()
    override fun getCurrentCity(
        context: Context,
        latitude: Double,
        longitude: Double,
        onCityObtained: (String) -> Unit
    ) {
        val geoCoder = Geocoder(context, Locale.getDefault())
        geoCoder.getFromLocation(latitude, longitude, 1) { addressList ->
            val cityName = addressList.getOrNull(0)?.adminArea.orEmpty()
            onCityObtained(cityName)
        }
    }
}