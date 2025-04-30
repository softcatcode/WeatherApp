package com.softcat.weatherapp.presentation.techInterface

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcat.weatherapp.domain.useCases.GetCurrentWeatherUseCase
import kotlinx.coroutines.launch

@Composable
fun GetCurrentWeatherUseCaseInterface(
    getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
) {
    val scope = rememberCoroutineScope()
    val cityId = remember { mutableStateOf("") }
    val result = remember { mutableStateOf("") }
    var offset by remember { mutableFloatStateOf(0f) }
    Column(
        modifier = Modifier
            .height(150.dp)
            .border(1.dp, Color.Black)
    ) {
        Row(
            modifier = Modifier
                .height(65.dp)
                .fillMaxWidth(),
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight()
                    .padding(vertical = 8.dp),
                value = cityId.value,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black
                ),
                onValueChange = {
                    cityId.value = it
                },
                singleLine = true,
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                onClick = {
                    scope.launch {
                        val res = getCurrentWeatherUseCase(cityId.value.toInt())
                        result.value = res.toString()
                    }
                },
                content = { Text(text = "Process") }
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .scrollable(
                    orientation = Orientation.Vertical,
                    state = rememberScrollableState { delta ->
                        offset += delta.toInt()
                        delta
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = ScrollState(offset.toInt())),
                text = result.value,
                fontSize = 16.sp
            )
        }
    }
}