package com.softcat.weatherapp.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.DefaultTintColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcat.weatherapp.R
import com.softcat.weatherapp.domain.entity.WeatherType
import com.softcat.weatherapp.presentation.ui.theme.CalendarPink
import com.softcat.weatherapp.presentation.ui.theme.CalendarPurple

private val specialFontFamily = FontFamily(
    Font(R.font.exo2_regular, FontWeight.Normal),
    Font(R.font.exo2_bold, FontWeight.Bold)
)

@Preview
@Composable
fun MonthTitle(title: String = "September") {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background),
        text = title,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        fontFamily = specialFontFamily
    )
}

@Composable
private fun MonthDayBox(
    isHighlighted: Boolean,
    text: String,
    cellSize: Int
) {
    val textColor = if (isHighlighted) {
        White
    } else {
        MaterialTheme.colorScheme.onBackground
    }
    val backgroundColor = if (isHighlighted) {
        Brush.verticalGradient(listOf(CalendarPurple, CalendarPink))
    } else {
        Brush.linearGradient(listOf(White, White))
    }
    Box(
        modifier = Modifier
            .size(cellSize.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = textColor
        )
    }
}

@Preview
@Composable
fun MonthDays(
    daysCount: Int = 30,
    beginWeekDay: Int = 2,
    highlightedDays: Set<Int> = setOf(5, 10, 11, 28)
) {
    val cellSize = 40
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 15.dp),

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            listOf(
                R.string.monday_label,
                R.string.tuesday_label,
                R.string.wednesday_label,
                R.string.thursday_label,
                R.string.friday_label,
                R.string.saturday_label,
                R.string.sunday_label
            ).forEach { strId ->
                Box(
                    modifier = Modifier.size(cellSize.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = strId),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp,
                    )
                }
            }
        }
        repeat((daysCount + 6) / 7) { weekNumber ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                repeat(7) { dayNumber ->
                    val monthDay = (weekNumber * 7 - beginWeekDay) + dayNumber + 1
                    val text = if (
                        weekNumber == 0 && dayNumber < beginWeekDay || monthDay > daysCount
                    ) "" else monthDay.toString()

                    MonthDayBox(
                        isHighlighted = monthDay in highlightedDays,
                        text = text,
                        cellSize = cellSize
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun WeatherParameter(
    title: String = "Temperature",
    iconResId: Int = R.drawable.temperature_parameter,
    minValue: Float = -50f,
    maxValue: Float = 50f,
    value: Float = 10f,
    onValueChange: (Float) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = iconResId),
            contentDescription = title,
            tint = Unspecified
        )
        Text(
            text = title,
            fontSize = 16.sp,
            fontFamily = specialFontFamily,
        )
        Slider(
            modifier = Modifier
                .weight(1f)
                .padding(20.dp),
            valueRange = minValue..maxValue,
            value = value,
            colors = SliderDefaults.colors().copy(
                thumbColor = CalendarPurple,
                disabledThumbColor = CalendarPurple,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondary
            ),
            onValueChange = onValueChange
        )
        Text(
            modifier = Modifier,
            text = value.toInt().toString(),
            fontSize = 16.sp,
            fontFamily = specialFontFamily,
        )
    }
}

@Composable
fun WeatherTypeSelector(
    currentType: WeatherType,
    onTypeChanged: (WeatherType) -> Unit
) {

}