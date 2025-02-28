package com.softcat.weatherapp.presentation.widgets.hourly

import android.icu.util.Calendar
import android.net.Uri
import androidx.compose.foundation.lazy.LazyRow
import androidx.glance.GlanceModifier
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceComposable
import androidx.glance.Image
import androidx.glance.appwidget.ImageProvider
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentHeight
import androidx.glance.layout.wrapContentSize
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextDefaults.defaultTextStyle
import androidx.glance.unit.ColorProvider
import com.softcat.weatherapp.domain.entity.Weather
import com.softcat.weatherapp.presentation.extensions.toTemperatureString
import com.softcat.weatherapp.presentation.utils.defaultWeather
import kotlin.math.min

private fun upcomingWeatherIndex(items: List<Weather>): Int {
    var i = 0
    val currentTime = Calendar.getInstance().timeInMillis
    while (i < items.size && items[i].date.timeInMillis < currentTime)
        ++i
    return i
}

@Composable
@GlanceComposable
fun Test(weatherList: List<Weather>) {
    //val weatherList = mutableListOf<Weather>()
    //repeat(24) { weatherList.add(defaultWeather) }
    val maxColumns = 8

    Column(
        modifier = GlanceModifier
            .background(Color.White)
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var i = upcomingWeatherIndex(weatherList)
        while (i < weatherList.size) {
            val items = weatherList.subList(i, min(i + maxColumns, weatherList.size))
            i += maxColumns
            WeatherRow(items)
        }
    }
}

@Composable
@GlanceComposable
private fun WeatherRow(
    items: List<Weather>
) {
    Row(
        modifier = GlanceModifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items.forEach { weather ->
            Column(
                modifier = GlanceModifier
                    .background(Color.White)
                    .wrapContentSize()
                    .padding(horizontal = 5.dp, vertical = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = GlanceModifier
                        .wrapContentSize(),
                    text = weather.tempC.toTemperatureString(),
                    style = defaultTextStyle.copy(
                        fontSize = 16.sp,
                        color = ColorProvider(Color.Gray),
                        textAlign = TextAlign.Center
                    )
                )
                Image(
                    modifier = GlanceModifier
                        .size(40.dp)
                        .background(Color.Red),
                    provider = ImageProvider(Uri.parse(weather.conditionUrl)),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun HourlyWeather(
    weatherList: List<Weather> = listOf(defaultWeather, defaultWeather, defaultWeather, defaultWeather)
) {
    LazyRow {
        items(weatherList, key = { it.date.timeInMillis }) { weather ->
            Column(
                modifier = GlanceModifier.width(50.dp).height(60.dp).padding(all = 2.dp)
            ) {
                Text(
                    modifier = GlanceModifier.height(10.dp).width(50.dp),
                    text = weather.tempC.toTemperatureString(),
                    style = defaultTextStyle.copy(
                        fontSize = 12.sp,
                        color = ColorProvider(MaterialTheme.colorScheme.secondary)
                    )
                )
                Image(
                    modifier = GlanceModifier.size(50.dp),
                    provider = ImageProvider(Uri.parse(weather.conditionUrl)),
                    contentDescription = null
                )
            }
        }
    }
}