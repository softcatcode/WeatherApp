package com.softcat.weatherapp.presentation.calendar

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.softcat.weatherapp.domain.entity.City
import javax.inject.Inject

class CalendarStoreFactory @Inject constructor(
    val storeFactory: StoreFactory
) {
    fun create(city: City): CalendarStore {
        TODO("Not implemented yet")
    }
}