package com.softcat.weatherapp.presentation.hourly

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import com.softcat.weatherapp.presentation.extensions.formattedTime
import com.softcat.weatherapp.presentation.ui.theme.CalendarPurple
import com.softcat.weatherapp.presentation.ui.theme.Pink80
import com.softcat.weatherapp.presentation.ui.theme.Purple80
import com.softcat.weatherapp.presentation.utils.defaultWeather
import androidx.compose.foundation.lazy.items
import com.softcat.data.mapper.toCalendar
import com.softcat.domain.entity.CurrentWeather

@Preview
@Composable
fun WeatherExtraInfo(
    weather: CurrentWeather = defaultWeather
) {
    val cmLabel = stringResource(id = R.string.centimeters)
    val mmLabel = stringResource(id = R.string.millimeters)
    val mpsLabel = stringResource(id = R.string.meters_per_second)
    val parameters = listOf(
        R.string.wind_speed_parameter to "${weather.windSpeed} $mpsLabel",
        R.string.precipitations_parameter to "${weather.precipitations} $mmLabel",
        R.string.snow_volume_parameter to "${weather.snow ?: 0} $cmLabel",
        R.string.humidity_parameter to "${weather.humidity} %",
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
            .wrapContentHeight()
            .padding(horizontal = 36.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
        shape = RoundedCornerShape(20),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
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
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun TemperatureIndicator(
    modifier: Modifier = Modifier,
    titleStrId: Int,
    temperatureValue: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.wrapContentHeight().fillMaxWidth(),
            text = stringResource(id = titleStrId),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            modifier = Modifier.fillMaxSize(),
            text = temperatureValue,
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Preview
@Composable
fun HourWeatherItem(
    modifier: Modifier = Modifier,
    time: String = "11:00",
    weather: CurrentWeather = defaultWeather
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { expanded = true }
            .then(modifier),
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
                .fillMaxHeight()
                .padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1.7f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    modifier = Modifier.wrapContentHeight().fillMaxWidth(),
                    text = time,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = weather.conditionText,
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    color = Color.Black,
                    maxLines = 2
                )
            }
            Spacer(Modifier.width(5.dp))
            GlideImage(
                modifier = Modifier.size(52.dp),
                model = weather.conditionUrl,
                contentDescription = null
            )
            Spacer(Modifier.width(5.dp))
            TemperatureIndicator(
                modifier = Modifier.weight(1f),
                titleStrId = R.string.temperature,
                temperatureValue = "${weather.tempC.toInt()} °C"
            )
            Spacer(Modifier.width(5.dp))
            TemperatureIndicator(
                modifier = Modifier.weight(1f),
                titleStrId = R.string.feels_like,
                temperatureValue = "${weather.feelsLike} °C"
            )
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

@Composable
fun HourlyWeatherList(
    modifier: Modifier = Modifier,
    weatherList: List<CurrentWeather>
) {
    val background = Brush.linearGradient(listOf(Purple80, Pink80))
    LazyColumn(
        modifier = Modifier.background(background).then(modifier)
    ) {
        items(weatherList) { weather ->
            HourWeatherItem(
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, top = 5.dp),
                time = weather.timeEpoch.toCalendar().formattedTime(),
                weather = weather
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HourlyWeatherTopBar(
    onBackClicked: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.hourly_weather_title),
            )
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = CalendarPurple,
            titleContentColor = MaterialTheme.colorScheme.background
        ),
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
    )
}

