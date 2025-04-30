package com.softcat.weatherapp.presentation.techInterface

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.softcat.weatherapp.domain.useCases.RemoveFromFavouriteUseCase
import com.softcat.weatherapp.domain.useCases.SearchCityUseCase
import kotlinx.coroutines.launch

@Composable
fun RemoveFromFavouriteUseCaseInterface(
    removeFromFavouriteUseCase: RemoveFromFavouriteUseCase,
    searchUseCase: SearchCityUseCase
) {
    val scope = rememberCoroutineScope()
    val cityName = remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(1.dp, Color.Black),
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(80.dp)
                .padding(vertical = 8.dp),
            value = cityName.value,
            onValueChange = { cityName.value = it }
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(vertical = 8.dp),
            onClick = {
                scope.launch {
                    val city = searchUseCase(cityName.value).first()
                    removeFromFavouriteUseCase(city.id)
                }
            },
            content = { Text("Remove") }
        )
    }
}