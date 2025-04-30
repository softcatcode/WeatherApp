package com.softcat.weatherapp.presentation.techInterface

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcat.weatherapp.domain.useCases.ObserveIsFavouriteUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun ObserveIsFavouriteUseCaseInterface(
    observeIsFavouriteUseCase: ObserveIsFavouriteUseCase,
) {
    val scope = rememberCoroutineScope()
    val city = remember { mutableStateOf("") }
    val result = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .height(100.dp)
            .border(1.dp, Color.Black)
    ) {
        Row(
            modifier = Modifier
                .height(65.dp)
                .fillMaxWidth(),
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .fillMaxHeight()
                    .padding(vertical = 8.dp),
                value = city.value,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black
                ),
                onValueChange = {
                    city.value = it
                },
                singleLine = true,
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                onClick = {
                    scope.launch {
                        val res = observeIsFavouriteUseCase(city.value.toInt()).first()
                        result.value = res.toString()
                    }
                },
                content = { Text(text = "isFavourite") }
            )
        }
        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize(),
                text = result.value,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}