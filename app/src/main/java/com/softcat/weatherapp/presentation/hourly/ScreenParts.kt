package com.softcat.weatherapp.presentation.hourly

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.softcat.weatherapp.R
import com.softcat.weatherapp.domain.entity.AstrologicalParameters
import com.softcat.weatherapp.domain.entity.Weather
import com.softcat.weatherapp.domain.entity.WeatherType
import com.softcat.weatherapp.presentation.ui.theme.PurpleGrey80
import java.util.Calendar

private val defaultWeather = Weather(
    type = WeatherType.Clouds,
    tempC = 20f,
    feelsLike = 23f,
    conditionText = "Cloudy with a bit of sun",
    conditionUrl = "//cdn.weatherapi.com/weather/64x64/night/113.png",
    date = Calendar.getInstance(),
    formattedDate = "2024-09-29",
    humidity = 20f,
    windSpeed = 3f,
    snowVolume = 0f,
    precipitations = 2f,
    astrologicalParams = AstrologicalParameters(
        sunriseTime = "06:12 AM",
        sunsetTime = "21:32 PM",
        moonriseTime = "22:01 AM",
        moonsetTime = "05:05 AM",
    )
)

@Preview
@Composable
fun WeatherExtraInfo(
    weather: Weather = defaultWeather
) {
    val mmLabel = stringResource(id = R.string.millimeters)
    val mpsLabel = stringResource(id = R.string.meters_per_second)
    val parameters = listOf(
        R.string.wind_speed_parameter to "${weather.windSpeed.toInt()} $mpsLabel",
        R.string.precipitations_parameter to "${weather.precipitations.toInt()} $mmLabel",
        R.string.snow_volume_parameter to "${weather.snowVolume.toInt()} $mmLabel",
        R.string.humidity_parameter to "${weather.humidity.toInt()} %",
    )
    val iconIds = listOf(
        R.drawable.wind_parameter,
        R.drawable.precipitations_parameter,
        R.drawable.snow_volume_parameter,
        R.drawable.humidity_parameter
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
        shape = RoundedCornerShape(20),
        colors = CardDefaults.cardColors(
            containerColor = PurpleGrey80
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp)
        ) {
            parameters.forEachIndexed { index, pair ->
                val (title, text) = pair
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(3.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = iconIds[index]),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                    Text(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        text = stringResource(id = title),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = text,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Preview
@Composable
fun HourWeatherItem(
    time: String = "11:00",
    weather: Weather = defaultWeather
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(10),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(2f)
            ) {
                Text(
                    text = time,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = weather.conditionText,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            GlideImage(
                modifier = Modifier
                    .size(36.dp)
                    .padding(2.dp),
                model = weather.conditionUrl,
                contentDescription = null
            )
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(id = R.string.temperature),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "${weather.tempC.toInt()} °C",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(id = R.string.feels_like),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "${weather.feelsLike.toInt()} °C",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
    DropdownMenu(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Transparent),
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        WeatherExtraInfo()
    }
}