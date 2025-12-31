package com.softcat.weatherapp.presentation.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.softcat.data.mapper.toCalendar
import com.softcat.domain.entity.Forecast
import com.softcat.domain.entity.Weather
import com.softcat.domain.entity.WeatherParameters.Companion.MAX_TEMPERATURE
import com.softcat.domain.entity.WeatherParameters.Companion.MIN_TEMPERATURE
import com.softcat.weatherapp.R
import com.softcat.weatherapp.presentation.extensions.formattedFullDate
import com.softcat.weatherapp.presentation.extensions.formattedShortWeekDay
import com.softcat.weatherapp.presentation.extensions.toTemperatureString
import com.softcat.weatherapp.presentation.ui.theme.CustomPurple
import com.softcat.weatherapp.presentation.ui.theme.WeatherCardGradient
import com.softcat.weatherapp.presentation.utils.ErrorDialog
import com.softcat.weatherapp.presentation.utils.defaultWeather

@Composable
private fun Initial() {

}

@Composable
private fun Error(
    error: Throwable,
    onErrorDismiss: () -> Unit
) {
    ErrorDialog(
        throwable = error,
        onDismissRequest = onErrorDismiss
    )
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = MaterialTheme.colorScheme.background
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
@Preview(showBackground = true)
private fun Loaded(
    forecast: Forecast = Forecast(
        hourly = emptyList(),
        upcoming = emptyList(),
        weather = defaultWeather
    ),
    onWeatherItemClicked: (Int) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        Text(
            modifier = Modifier.weight(0.5f),
            text = forecast.weather?.conditionText.orEmpty(),
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = forecast.weather?.tempC?.toTemperatureString().orEmpty(),
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 70.sp)
            )
            GlideImage(
                modifier = Modifier.size(70.dp),
                model = forecast.weather?.conditionUrl,
                contentDescription = null
            )
        }
        Text(
            text = forecast.weather?.timeEpoch?.toCalendar()?.formattedFullDate().orEmpty(),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.weight(1f))
        AnimatedUpcomingWeatherContainer(
            daysWeather = forecast.upcoming.orEmpty(),
            onWeatherItemClicked = onWeatherItemClicked
        )
        Spacer(Modifier.weight(0.5f))
    }
}

@Composable
private fun UpcomingWeather(
    daysWeather: List<Weather>,
    onWeatherItemClicked: (Int) -> Unit
) {
    Card(
        modifier = Modifier.padding(24.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.25f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.title_upcoming),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.background
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(daysWeather) { index, item ->
                    SmallWeatherCard(
                        weather = item,
                        onClick = { onWeatherItemClicked(index) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedUpcomingWeatherContainer(
    daysWeather: List<Weather>,
    onWeatherItemClicked: (Int) -> Unit
) {
    val state = remember {
        MutableTransitionState(false).apply { targetState = true }
    }
    val animation =
            fadeIn(animationSpec = tween(500)) +
            slideIn(
                animationSpec = tween(500),
                initialOffset = { IntOffset(x = 0, y = it.height) }
            )
    AnimatedVisibility(
        visibleState = state,
        enter = animation
    ) {
        UpcomingWeather(
            daysWeather = daysWeather,
            onWeatherItemClicked = onWeatherItemClicked
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun SmallWeatherCard(
    weather: Weather,
    onClick: () -> Unit = {},
    isForCurrentDay: Boolean = false
) {
    val cardBorder = if (isForCurrentDay)
        BorderStroke(1.dp, CustomPurple)
    else
        BorderStroke(0.dp, Color.Unspecified)
    OutlinedCard(
        modifier = Modifier
            .height(128.dp)
            .width(100.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        border = cardBorder,
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = weather.avgTemp.toTemperatureString(),
                color = Color.Black
            )
            GlideImage(
                modifier = Modifier.size(48.dp),
                model = weather.conditionUrl,
                contentDescription = null
            )
            Text(
                text = if (isForCurrentDay)
                        stringResource(id = R.string.today)
                    else
                        weather.date.formattedShortWeekDay(),
                color = Color.Black
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    cityName: String,
    isFavourite: Boolean,
    onFavouriteStatusClicked: () -> Unit,
    onCalendarClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Text(text = cityName) },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.background
        ),
        navigationIcon = {
            IconButton(onClick = { onBackClicked() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background
                )
            }
        },
        actions = {
            val icon = if (isFavourite) Icons.Default.Star else Icons.Default.StarBorder
            IconButton(onClick = onFavouriteStatusClicked) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background
                )
            }
            IconButton(onClick = onCalendarClicked) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.calendar),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
    )
}

private fun getBackgroundGradient(state: DetailsStore.State): Brush {
    val forecastState = state.forecastState
    return if (forecastState is DetailsStore.State.ForecastState.Loaded) {
        val t = forecastState.forecast.weather?.tempC ?: 0f
        val r: Float = 1f / (MAX_TEMPERATURE - MIN_TEMPERATURE) * 0.8f * (t - MIN_TEMPERATURE)
        val b = 1f - r
        val firstColor = Color(r, 0f, b)
        val secondColor = Color(r, 0.4f, b)
        Brush.linearGradient(listOf(firstColor, secondColor))
    } else {
        WeatherCardGradient.gradients[1].primaryGradient
    }
}

@Composable
fun DetailsContent(component: DetailsComponent) {
    val state by component.model.collectAsState()
    Scaffold(
        topBar = {
            TopBar(
                cityName = state.city.name,
                isFavourite = state.isFavourite,
                onFavouriteStatusClicked = { component.changeFavouriteStatus() },
                onCalendarClicked = { component.openCityCalendar() },
                onBackClicked = { component.back() }
            )
        },
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .background(getBackgroundGradient(state)),
        contentColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(modifier = Modifier) {
            when (val forecastState = state.forecastState) {
                is DetailsStore.State.ForecastState.Error -> Error(
                    error = forecastState.error,
                    onErrorDismiss = { component.back() }
                )
                DetailsStore.State.ForecastState.Initial -> Initial()
                is DetailsStore.State.ForecastState.Loaded -> Loaded(
                    forecastState.forecast,
                    onWeatherItemClicked = { index -> component.openHourlyWeather(index) }
                )
                DetailsStore.State.ForecastState.Loading -> Loading()
            }
        }
    }
}