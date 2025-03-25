package com.softcat.weatherapp.presentation.extensions

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

fun ComponentContext.componentScope() = CoroutineScope(
    Dispatchers.Main.immediate + SupervisorJob()
).apply {
    lifecycle.doOnDestroy { cancel() }
}

fun Float.toTemperatureString() = "${roundToInt()}°C"

fun Calendar.formattedFullDate(): String =
    SimpleDateFormat("EEEE | d MMM y", Locale.getDefault()).format(time)

fun Calendar.formattedTime(): String =
    SimpleDateFormat("HH:mm", Locale.getDefault()).format(time)

fun Calendar.formattedShortWeekDay(): String =
    SimpleDateFormat("EEE", Locale.getDefault()).format(time)