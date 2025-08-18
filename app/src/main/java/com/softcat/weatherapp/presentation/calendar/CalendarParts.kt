package com.softcat.weatherapp.presentation.calendar

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcat.domain.entity.WeatherParameters.Companion.MAX_TEMPERATURE
import com.softcat.domain.entity.WeatherParameters.Companion.MIN_TEMPERATURE
import com.softcat.domain.entity.WeatherType
import com.softcat.domain.entity.toIconResId
import com.softcat.domain.entity.toTitleResId
import com.softcat.weatherapp.R
import com.softcat.weatherapp.presentation.ui.theme.CalendarPink
import com.softcat.weatherapp.presentation.ui.theme.CalendarPurple
import kotlin.math.roundToInt

val exo2FontFamily = FontFamily(
    Font(R.font.exo2_regular, FontWeight.Normal),
    Font(R.font.exo2_bold, FontWeight.Bold)
)

@Preview
@Composable
fun MonthTitle(
    modifier: Modifier = Modifier,
    title: String = "September"
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
            .then(modifier),
        text = title,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        fontFamily = exo2FontFamily
    )
}

@Composable
private fun MonthDayBox(
    text: String,
    cellSize: Int,
    isHighlighted: Boolean = true,
    isSpecified: Boolean = false
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
        if (isSpecified) {
            val color = if (isHighlighted) {
                MaterialTheme.colorScheme.background
            } else {
                MaterialTheme.colorScheme.onBackground
            }
            Spacer(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 5.dp)
                    .size(3.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Preview
@Composable
fun MonthDays(
    modifier: Modifier = Modifier,
    daysCount: Int = 30,
    beginWeekDay: Int = 6,
    highlightedDays: Set<Int> = setOf(5, 10, 11, 28),
    currentDay: Int? = 11,
) {
    val cellSize = 40
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 15.dp)
            .then(modifier),

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
        repeat((daysCount + beginWeekDay + 6) / 7) { weekNumber ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 1.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                repeat(7) { dayNumber ->
                    val monthDay = (weekNumber * 7 - beginWeekDay) + dayNumber + 1
                    val text = if (
                        weekNumber == 0 && dayNumber < beginWeekDay || monthDay > daysCount
                    ) "" else monthDay.toString()

                    MonthDayBox(
                        isHighlighted = monthDay in highlightedDays,
                        isSpecified = monthDay == currentDay,
                        text = text,
                        cellSize = cellSize
                    )
                }
            }
        }
    }
}

private fun IntRange.format(): String = "$start - $endInclusive"
private fun IntRange.toFloatRange() = first.toFloat()..last.toFloat()
private fun ClosedFloatingPointRange<Float>.toIntRange() = start.roundToInt()..endInclusive.roundToInt()

@Preview
@Composable
fun WeatherParameter(
    modifier: Modifier = Modifier,
    titleResId: Int = R.string.wind_speed_parameter,
    iconResId: Int = R.drawable.wind_parameter,
    minValue: Int = -50,
    maxValue: Int = 50,
    value: IntRange = 0..30,
    onValueChange: (IntRange) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val title = stringResource(id = titleResId)
        Icon(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = iconResId),
            contentDescription = title,
            tint = Unspecified
        )
        Text(
            modifier = Modifier
                .weight(2.2f)
                .padding(start = 10.dp),
            text = title,
            fontSize = 12.sp,
            fontFamily = exo2FontFamily,
            color = MaterialTheme.colorScheme.onBackground
        )
        RangeSlider(
            modifier = Modifier
                .weight(5f)
                .padding(20.dp),
            valueRange = minValue.toFloat()..maxValue.toFloat(),
            value = value.toFloatRange(),
            colors = SliderDefaults.colors().copy(
                thumbColor = MaterialTheme.colorScheme.onBackground,
                disabledThumbColor = MaterialTheme.colorScheme.onBackground,
                activeTrackColor = MaterialTheme.colorScheme.onBackground,
                inactiveTrackColor = MaterialTheme.colorScheme.secondary
            ),
            onValueChange = { onValueChange(it.toIntRange()) }
        )
        Text(
            modifier = Modifier.weight(1.5f),
            text = value.format(),
            fontSize = 16.sp,
            fontFamily = exo2FontFamily,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun WeatherTypeSelector(
    modifier: Modifier = Modifier,
    elementModifier: Modifier = Modifier,
    iconWithTextModifier: Modifier = Modifier.size(100.dp),
    currentType: WeatherType = WeatherType.Sun,
    onItemSelected: (WeatherType) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    WeatherTypeItem(
        modifier = iconWithTextModifier,
        type = currentType,
        onClick = { expanded = true }
    )
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        WeatherType.entries.forEach { type ->
            WeatherTypeItem(
                modifier = elementModifier,
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
@Preview(showBackground = true)
fun TemperatureInput(
    selectedTemp: String = "20",
    onTempSelected: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    val tempRange = MIN_TEMPERATURE.toInt()..MAX_TEMPERATURE.toInt()
    Box(
        modifier = Modifier.wrapContentSize().padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = selectedTemp,
            fontSize = 24.sp,
            fontFamily = exo2FontFamily,
            color = MaterialTheme.colorScheme.primary,
        )
        DropdownMenu(
            modifier = Modifier,
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            tempRange.forEach { temp ->
                Button(
                    onClick = {
                        onTempSelected(temp.toString())
                        expanded = false
                    }
                ) {
                    Text(
                        text = temp.toString(),
                        fontSize = 24.sp,
                        fontFamily = exo2FontFamily,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
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
        modifier = Modifier.wrapContentHeight().then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = labelText,
            fontFamily = exo2FontFamily,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 18.sp,
        )
        Spacer(Modifier.width(10.dp))
        TemperatureInput(
            selectedTemp = value,
            onTempSelected = onValueChange
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TemperatureRangeInput(
    modifier: Modifier = Modifier,
    minValue: String = "10",
    maxValue: String = "23",
    onMinValueChange: (String) -> Unit = {},
    onMaxValueChange: (String) -> Unit = {},
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TemperatureInputField(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 10.dp),
            value = minValue,
            onValueChange = onMinValueChange,
            labelText = stringResource(id = R.string.from_label),
        )
        TemperatureInputField(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 10.dp),
            value = maxValue,
            onValueChange = onMaxValueChange,
            labelText = stringResource(id = R.string.to_label),
        )
    }
}

@Preview
@Composable
fun BottomSheetDragHandle() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(5.dp))
        Spacer(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .height(3.dp)
                .width(50.dp)
                .background(Gray)
        )
        Spacer(Modifier.height(3.dp))
        Spacer(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .height(3.dp)
                .width(30.dp)
                .background(Gray)

        )
        Spacer(Modifier.height(10.dp))
    }
}

@Preview
@Composable
fun WeatherTypeItem(
    modifier: Modifier = Modifier,
    type: WeatherType = WeatherType.Clouds,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .size(100.dp)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val title = stringResource(id = type.toTitleResId())
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = type.toIconResId()),
                contentDescription = title,
                tint = Unspecified,
            )
        }
        Text(
            modifier = Modifier
                .wrapContentSize(),
            text = title,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            fontFamily = exo2FontFamily
        )
    }
}