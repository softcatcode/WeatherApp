package com.softcat.weatherapp.presentation.calendar

import android.icu.util.Calendar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcat.weatherapp.R
import com.softcat.weatherapp.presentation.utils.ErrorDialog
import com.softcat.weatherapp.presentation.utils.HintButton
import com.softcat.weatherapp.presentation.utils.HintDialog
import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale

@Composable
fun YearButton(
    isSelected: Boolean,
    yearValue: Int,
    onYearSelected: (Int) -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.background
    } else {
        Transparent
    }
    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onBackground
    } else {
        MaterialTheme.colorScheme.background
    }
    OutlinedButton(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .padding(horizontal = 5.dp)
            .wrapContentSize(),
        onClick = { onYearSelected(yearValue) },
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
        ),
        shape = CircleShape
    ) {
        Text(
            modifier = Modifier.clip(CircleShape),
            text = yearValue.toString(),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontFamily = exo2FontFamily,
            color = textColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CalendarTopBar(
    onYearChange: (Int) -> Unit = {},
    onBackClick: () -> Unit = {},
    onHintIconClick: () -> Unit = {},
    minYear: Int = 1990,
    maxYear: Int = 2025,
    selectedYear: Int = 2024
) {
    TopAppBar(
        modifier = Modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                reverseLayout = true
            ) {
                for (year in maxYear downTo minYear) {
                    item {
                        YearButton(
                            isSelected = year == selectedYear,
                            yearValue = year,
                            onYearSelected = onYearChange
                        )
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .background(Transparent),
                onClick = onBackClick
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    tint = MaterialTheme.colorScheme.background
                )
            }
        },
        actions = { HintButton(onClick = onHintIconClick) }
    )
}

@Preview
@Composable
fun Month(
    modifier: Modifier = Modifier,
    name: String = "September",
    highlightedDays: Set<Int> = setOf(12, 20, 19),
    daysCount: Int = 30,
    weekdayOffset: Int = 2,
    currentDay: Int? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
            .then(modifier)
    ) {
        MonthTitle(
            modifier = Modifier,
            title = name,
        )
        MonthDays(
            beginWeekDay = weekdayOffset,
            daysCount = daysCount,
            highlightedDays = highlightedDays,
            currentDay = currentDay
        )
    }
}

@Composable
fun MonthList(
    modifier: Modifier = Modifier,
    elementModifier: Modifier = Modifier,
    year: Int,
    highlightedDays: List<Set<Int>>?
) {
    val calendar = Calendar.getInstance(Locale.getDefault())
    val currentYear = calendar[Calendar.YEAR]
    val currentMonth = calendar[Calendar.MONTH]
    val currentDay = calendar[Calendar.DAY_OF_MONTH]
    val state = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        state = state
    ) {
        itemsIndexed(
            items = listOf(
                R.string.january_title,
                R.string.february_title,
                R.string.march_title,
                R.string.april_title,
                R.string.may_title,
                R.string.june_title,
                R.string.july_title,
                R.string.august_title,
                R.string.september_title,
                R.string.october_title,
                R.string.november_title,
                R.string.december_title,
            ),
            key = { _, item -> item }
        ) { monthIndex, monthNameId ->
            val weekday = LocalDate.of(year, monthIndex + 1, 1).dayOfWeek.value
            Month(
                modifier = elementModifier,
                name = stringResource(id = monthNameId),
                daysCount = YearMonth.of(year, monthIndex + 1).lengthOfMonth(),
                weekdayOffset = weekday - 1,
                currentDay = currentDay.takeIf {
                    currentYear == year && currentMonth == monthIndex
                },
                highlightedDays = highlightedDays?.getOrNull(monthIndex).orEmpty()
            )
        }
    }
    LaunchedEffect(Unit) {
        if (currentYear == year)
            state.scrollToItem(currentMonth)
    }
}

@Composable
private fun CalendarStateContent(
    state: CalendarStore.State,
    paddings: PaddingValues,
    onErrorDismiss: () -> Unit = {}
) {
    when (val calendarState = state.calendarState) {
        is CalendarStore.State.CalendarState.Initial -> {}

        is CalendarStore.State.CalendarState.Loading -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(70.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        is CalendarStore.State.CalendarState.Loaded -> {
            MonthList(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .padding(paddingValues = paddings),
                year = state.year,
                highlightedDays = calendarState.highlightedDays
            )
        }

        is CalendarStore.State.CalendarState.Error -> {
            ErrorDialog(
                throwable = calendarState.throwable,
                onDismissRequest = onErrorDismiss
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarContent(
    component: CalendarComponent
) {
    val state by component.model.collectAsState()
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var hintDialogVisible by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CalendarTopBar(
                onYearChange = { component.selectYear(year = it) },
                onBackClick = { component.back() },
                onHintIconClick = { hintDialogVisible = true },
                selectedYear = state.year
            )
        },
        floatingActionButton = {
            IconButton(
                modifier = Modifier.size(50.dp),
                onClick = { isExpanded = true },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(0.7f),
                    tint = MaterialTheme.colorScheme.background,
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(id = R.string.settings)
                )
            }
        }
    ) { paddings ->
        CalendarStateContent(
            state = state,
            paddings = paddings,
            onErrorDismiss = { component.highlightDays() }
        )
        CalendarBottomSheet(
            isExpanded = isExpanded,
            onDismiss = {
                isExpanded = false
                component.highlightDays()
            },
            currentWeatherType = state.weatherParams.weatherType,
            minTemperature = state.weatherParams.temperatureMin.toString(),
            maxTemperature = state.weatherParams.temperatureMax.toString(),
            onMinTempChange = { component.changeMinTemp(it) },
            onMaxTempChange = { component.changeMaxTemp(it) },
            onWeatherTypeSelected = { component.selectWeatherType(it) },
            onWindSpeedValueChange = { component.changeWindSpeed(it) },
            onHumidityValueChange = { component.changeHumidity(it) },
            onSnowVolumeValueChange = { component.changeSnowVolume(it) },
            onPrecipitationsValueChange = { component.changePrecipitations(it) },
            precipitations = state.weatherParams.precipitations,
            snowVolume = state.weatherParams.snowVolume,
            windSpeed = state.weatherParams.windSpeed,
            humidity = state.weatherParams.humidity
        )
        if (hintDialogVisible) {
            HintDialog(
                onDismissRequest = { hintDialogVisible = false },
                title = stringResource(id = R.string.hint),
                text = stringResource(id = R.string.calendar_hint)
            )
        }
    }
}