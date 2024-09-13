package com.softcat.weatherapp.presentation.calendar

import android.icu.util.Calendar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcat.weatherapp.R
import com.softcat.weatherapp.domain.entity.WeatherType
import com.softcat.weatherapp.presentation.ui.theme.CalendarPurple
import java.time.Clock
import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale
import java.util.TimeZone

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
        CalendarPurple
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
    minYear: Int = 1990,
    maxYear: Int = 2025,
    selectedYear: Int = 2024
) {
    TopAppBar(
        modifier = Modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CalendarPurple,
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
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
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
                    currentYear == year && currentMonth == monthIndex + 1
                },
                highlightedDays = highlightedDays?.getOrNull(monthIndex).orEmpty()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CalendarContent(
    highlightedDays: List<Set<Int>>? = null,
    year: Int = 2024,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CalendarTopBar(
                onYearChange = {},
                onBackClick = {},
                selectedYear = Calendar.getInstance().get(Calendar.YEAR)
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
    ) {
        MonthList(
            modifier = Modifier.padding(horizontal = 5.dp),
            year = year,
            highlightedDays = highlightedDays
        )
        CalendarBottomSheet(
            isExpanded = isExpanded,
            onDismiss = { isExpanded = false },
            currentWeatherType = WeatherType.Clouds,
            minTemperature = "12",
            maxTemperature = "24",
            onMinTempChange = {},
            onMaxTempChange = {},
            onWeatherTypeSelected = {},
            onWindSpeedValueChange = {},
            onHumidityValueChange = {},
            onSnowVolumeValueChange = {},
            onPrecipitationsValueChange = {},
            precipitations = 0f,
            snowVolume = 0f,
            windSpeed = 0f,
            humidity = 0f
        )
    }
}