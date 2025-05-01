package com.softcat.domain.interfaces

import android.content.Context

interface LocationRepository {
    fun getCurrentCity(
        context: Context,
        onCityNameLoaded: (String) -> Unit
    )
}