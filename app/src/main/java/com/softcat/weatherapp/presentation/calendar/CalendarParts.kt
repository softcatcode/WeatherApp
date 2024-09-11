package com.softcat.weatherapp.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcat.weatherapp.R
import com.softcat.weatherapp.domain.entity.WeatherType
import com.softcat.weatherapp.presentation.ui.theme.CalendarPink
import com.softcat.weatherapp.presentation.ui.theme.CalendarPurple

val exo2FontFamily = FontFamily(
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
        fontFamily = exo2FontFamily
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
    modifier: Modifier = Modifier,
    title: String = "Wind speed",
    iconResId: Int = R.drawable.wind_parameter,
    minValue: Float = -50f,
    maxValue: Float = 50f,
    value: Float = 10f,
    onValueChange: (Float) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.background)
            .then(modifier),
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
            modifier = Modifier.padding(start = 10.dp),
            text = title,
            fontSize = 16.sp,
            fontFamily = exo2FontFamily,
            color = MaterialTheme.colorScheme.onBackground
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
            fontFamily = exo2FontFamily,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Composable
fun WeatherTypeSelector(
    currentType: WeatherType = WeatherType.Sun,
    onItemSelected: (WeatherType) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    WeatherTypeItem(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        type = currentType,
        onClick = { expanded = true }
    )
    DropdownMenu(
        modifier = Modifier
            .width(150.dp)
            .height(250.dp),
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        WeatherType.entries.forEach { type ->
            WeatherTypeItem(
                type = type,
                onClick = {
                    onItemSelected(type)
                    expanded = false
                }
            )
            HorizontalDivider()
        }
    }
}

@Composable
private fun TemperatureInputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ) {
        Box(
            modifier = Modifier.fillMaxHeight().padding(bottom = 15.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = labelText,
                fontFamily = exo2FontFamily,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp,
            )
        }
        TextField(
            modifier = Modifier
                .size(128.dp),
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle.Default.copy(
                fontSize = 50.sp,
                textAlign = TextAlign.Center,
                fontFamily = exo2FontFamily,
                color = CalendarPurple
            ),
            colors = TextFieldDefaults.colors().copy(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                cursorColor = MaterialTheme.colorScheme.tertiary,
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
    }
}

@Preview
@Composable
fun TemperatureRangeInput(
    modifier: Modifier = Modifier,
    minValue: String = "10",
    maxValue: String = "23",
    onMinValueChange: (String) -> Unit = {},
    onMaxValueChange: (String) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TemperatureInputField(
            value = minValue,
            onValueChange = onMinValueChange,
            labelText = stringResource(id = R.string.from_label),
        )
        TemperatureInputField(
            value = maxValue,
            onValueChange = onMaxValueChange,
            labelText = stringResource(id = R.string.to_label),
        )
    }
}