package com.softcat.weatherapp.presentation.calendar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softcat.weatherapp.R
import com.softcat.weatherapp.domain.entity.WeatherType

private const val minWindSpeed = 0f
private const val maxWindSpeed = 30f
private const val minHumidity = 0f
private const val maxHumidity = 100f
private const val minPrecipitations = 0f
private const val maxPrecipitations = 1000f
private const val minSnowVolume = 0f
private const val maxSnowVolume = 1000f

private fun LazyListScope.temperatureSelector(
    currentWeatherType: WeatherType,
    onWeatherTypeSelected: (WeatherType) -> Unit,
    minTemperature: String,
    onMinTempChange: (String) -> Unit,
    maxTemperature: String,
    onMaxTempChange: (String) -> Unit,
) {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
        ) {
            WeatherTypeSelector(
                expandingColumnModifier = Modifier
                    .weight(1f)
                    .height(250.dp),
                elementModifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                iconWithTextModifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                onItemSelected = onWeatherTypeSelected,
                currentType = currentWeatherType
            )
            Spacer(Modifier.width(20.dp))
            TemperatureRangeInput(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxHeight(),
                minValue = minTemperature,
                maxValue = maxTemperature,
                onMinValueChange = onMinTempChange,
                onMaxValueChange = onMaxTempChange
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    isExpanded: Boolean,
    onDismiss: () -> Unit,
    currentWeatherType: WeatherType,
    minTemperature: String,
    maxTemperature: String,
    windSpeed: Float,
    humidity: Float,
    precipitations: Float,
    snowVolume: Float,
    onMinTempChange: (String) -> Unit,
    onMaxTempChange: (String) -> Unit,
    onWeatherTypeSelected: (WeatherType) -> Unit,
    onWindSpeedValueChange: (Float) -> Unit,
    onHumidityValueChange: (Float) -> Unit,
    onPrecipitationsValueChange: (Float) -> Unit,
    onSnowVolumeValueChange: (Float) -> Unit,
) {
    if (isExpanded) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .then(modifier),
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            tonalElevation = 1.dp,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.background,
            scrimColor = MaterialTheme.colorScheme.background,
            dragHandle = { BottomSheetDragHandle() },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
            ) {
                temperatureSelector(
                    currentWeatherType = currentWeatherType,
                    onWeatherTypeSelected = onWeatherTypeSelected,
                    minTemperature = minTemperature,
                    onMinTempChange = onMinTempChange,
                    maxTemperature = maxTemperature,
                    onMaxTempChange = onMaxTempChange
                )
                item {
                    WeatherParameter(
                        titleResId = R.string.wind_speed_parameter,
                        iconResId = R.drawable.wind_parameter,
                        minValue = minWindSpeed,
                        maxValue = maxWindSpeed,
                        value = windSpeed,
                        onValueChange = onWindSpeedValueChange
                    )
                }
                item {
                    WeatherParameter(
                        titleResId = R.string.humidity_parameter,
                        iconResId = R.drawable.humidity_parameter,
                        minValue = minHumidity,
                        maxValue = maxHumidity,
                        value = humidity,
                        onValueChange = onHumidityValueChange
                    )
                }
                item {
                    WeatherParameter(
                        titleResId = R.string.precipitations_parameter,
                        iconResId = R.drawable.precipitations_parameter,
                        minValue = minPrecipitations,
                        maxValue = maxPrecipitations,
                        value = precipitations,
                        onValueChange = onPrecipitationsValueChange
                    )
                }
                item {
                    WeatherParameter(
                        titleResId = R.string.snow_volume_parameter,
                        iconResId = R.drawable.snow_volume_parameter,
                        minValue = minSnowVolume,
                        maxValue = maxSnowVolume,
                        value = snowVolume,
                        onValueChange = onSnowVolumeValueChange
                    )
                }
            }
        }
    }
}