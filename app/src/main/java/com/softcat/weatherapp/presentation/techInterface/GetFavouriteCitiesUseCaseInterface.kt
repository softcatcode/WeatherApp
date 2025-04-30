package com.softcat.weatherapp.presentation.techInterface

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import com.softcat.weatherapp.domain.useCases.GetFavouriteCitiesUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun GetFavouriteCitiesUseCaseInterface(
    getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase,
) {
    val scope = rememberCoroutineScope()
    val result = remember { mutableStateOf("") }
    var offset by remember { mutableFloatStateOf(0f) }
    Column(
        modifier = Modifier
            .height(100.dp)
            .border(1.dp, Color.Black)
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            onClick = {
                scope.launch {
                    val res = getFavouriteCitiesUseCase().first()
                    result.value = res.toString()
                }
            }
        ) {
            Text("Get favourites")
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(
                    state = rememberScrollableState { delta ->
                        offset += delta
                        delta
                    },
                    orientation = Orientation.Vertical
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = ScrollState(offset.toInt())),
                text = result.value
            )
        }
    }
}