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
import com.softcat.domain.entity.WeatherParameters.Companion.MAX_HUMIDITY
import com.softcat.domain.entity.WeatherParameters.Companion.MAX_PRECIPITATIONS
import com.softcat.domain.entity.WeatherParameters.Companion.MAX_SNOW_VOLUME
import com.softcat.domain.entity.WeatherParameters.Companion.MAX_WIND_SPEED
import com.softcat.domain.entity.WeatherParameters.Companion.MIN_HUMIDITY
import com.softcat.domain.entity.WeatherParameters.Companion.MIN_PRECIPITATIONS
import com.softcat.domain.entity.WeatherParameters.Companion.MIN_SNOW_VOLUME
import com.softcat.domain.entity.WeatherParameters.Companion.MIN_WIND_SPEED
import com.softcat.domain.entity.WeatherType
import com.softcat.weatherapp.R

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
                modifier = Modifier
                    .weight(1f)
                    .height(250.dp),
                elementModifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                iconWithTextModifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = 3.dp),
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
    windSpeed: ClosedFloatingPointRange<Float>,
    humidity: ClosedFloatingPointRange<Float>,
    precipitations: ClosedFloatingPointRange<Float>,
    snowVolume: ClosedFloatingPointRange<Float>,
    onMinTempChange: (String) -> Unit,
    onMaxTempChange: (String) -> Unit,
    onWeatherTypeSelected: (WeatherType) -> Unit,
    onWindSpeedValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onHumidityValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onPrecipitationsValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onSnowVolumeValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
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
            scrimColor = MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
            dragHandle = { BottomSheetDragHandle() }
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
                        minValue = MIN_WIND_SPEED,
                        maxValue = MAX_WIND_SPEED,
                        value = windSpeed,
                        onValueChange = onWindSpeedValueChange
                    )
                }
                item {
                    WeatherParameter(
                        titleResId = R.string.humidity_parameter,
                        iconResId = R.drawable.humidity_parameter,
                        minValue = MIN_HUMIDITY,
                        maxValue = MAX_HUMIDITY,
                        value = humidity,
                        onValueChange = onHumidityValueChange
                    )
                }
                item {
                    WeatherParameter(
                        titleResId = R.string.precipitations_parameter,
                        iconResId = R.drawable.precipitations_parameter,
                        minValue = MIN_PRECIPITATIONS,
                        maxValue = MAX_PRECIPITATIONS,
                        value = precipitations,
                        onValueChange = onPrecipitationsValueChange
                    )
                }
                item {
                    WeatherParameter(
                        titleResId = R.string.snow_volume_parameter,
                        iconResId = R.drawable.snow_volume_parameter,
                        minValue = MIN_SNOW_VOLUME,
                        maxValue = MAX_SNOW_VOLUME,
                        value = snowVolume,
                        onValueChange = onSnowVolumeValueChange
                    )
                }
            }
        }
    }
}