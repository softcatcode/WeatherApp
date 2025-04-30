package com.softcat.weatherapp.presentation.techInterface

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.softcat.domain.useCases.GetCurrentCityNameUseCase
import kotlinx.coroutines.launch

@Composable
fun GetCurrentCityNameUseCaseInterface(
    getCurrentCityNameUseCase: GetCurrentCityNameUseCase
) {
    val context = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()
    val city = remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(1.dp, Color.Black)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(0.5f).fillMaxHeight(),
            text = city.value,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Button(
            modifier = Modifier.fillMaxSize(),
            onClick = {
                scope.launch {
                    getCurrentCityNameUseCase(context) { city.value = it }
                }
            },
            content = { Text("Get Current City") }
        )
    }
}