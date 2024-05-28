package com.softcat.weatherapp.presentation.favourite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.softcat.weatherapp.presentation.ui.theme.CardGradient
import com.softcat.weatherapp.presentation.ui.theme.WeatherCardGradient

@Composable
fun FavouritesContent(component: FavouritesComponent) {
    val state by component.model.collectAsState()
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(
            items = state.cityItems,
            key = { _, item -> item.city.id }
        ) { index, item ->
            CityCard(cityItem = item, index = index)
        }
    }
}

private fun Modifier.setCardGradient(gradient: CardGradient): Modifier {
    background(gradient.primaryGradient)
    drawBehind {
        drawCircle(
            brush = gradient.secondaryGradient,
            center = Offset(center.x - size.width / 10, center.y + size.height / 2),
            radius = size.maxDimension / 2
        )
    }
    return this
}

@Composable
private fun CityCard(
    cityItem: FavouritesStore.State.CityItem,
    index: Int
) {
    val gradient = WeatherCardGradient.gradients[index % WeatherCardGradient.gradients.size]
    Card(
        modifier = Modifier
            .fillMaxSize()
            .shadow(
                elevation = 16.dp,
                spotColor = gradient.shadow,
                shape = MaterialTheme.shapes.extraLarge
            ),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Box(
            modifier = Modifier
                .background(gradient.primaryGradient)
                .drawBehind {
                    drawCircle(
                        brush = gradient.secondaryGradient,
                        center = Offset(center.x - size.width / 10, center.y + size.height / 2),
                        radius = size.maxDimension / 2
                    )
                }
                .fillMaxSize()
                .sizeIn(minHeight = 196.dp)
                .padding(24.dp),
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart),
                text = cityItem.city.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}