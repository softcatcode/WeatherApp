package com.softcat.weatherapp.presentation.favourite

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.softcat.weatherapp.R
import com.softcat.weatherapp.presentation.extensions.toTemperatureString
import com.softcat.weatherapp.presentation.ui.theme.Orange
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
        item(span = { GridItemSpan(2) }) {
            SearchCard { component.onSearchClick() }
        }
        itemsIndexed(
            items = state.cityItems,
            key = { _, item -> item.city.id }
        ) { index, item ->
            CityCard(cityItem = item, index = index) { component.onCityItemClick(item.city) }
        }
        item {
            AddFavouriteCityCard { component.onAddToFavouritesClick() }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BoxScope.WeatherStateContent(cityItem: FavouritesStore.State.CityItem) {
    when (val state = cityItem.weatherState) {
        is FavouritesStore.State.WeatherState.Content -> {
            GlideImage(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(56.dp),
                model = state.iconUrl,
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(top = 10.dp),
                text = state.tempC.toTemperatureString(),
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 48.sp)
            )
        }

        FavouritesStore.State.WeatherState.Error -> {}

        FavouritesStore.State.WeatherState.Initial -> {}

        FavouritesStore.State.WeatherState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Composable
private fun CityCard(
    cityItem: FavouritesStore.State.CityItem,
    index: Int,
    onClick: () -> Unit
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
                .padding(24.dp)
                .clickable { onClick() },
        ) {
            WeatherStateContent(cityItem = cityItem)
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

@Composable
fun AddFavouriteCityCard(onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors().copy(containerColor = Color.Transparent),
        shape = MaterialTheme.shapes.extraLarge,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        Box(
            modifier = Modifier
                .sizeIn(minHeight = 196.dp)
                .fillMaxSize()
                .padding(24.dp)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(100.dp),
                imageVector = Icons.Default.Add,
                tint = Orange,
                contentDescription = null
            )
            Text(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = stringResource(id = R.string.button_add_favourite),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun SearchCard(onClick: () -> Unit) {
    val gradient = WeatherCardGradient.gradients[3 % WeatherCardGradient.gradients.size]
    Card(
        modifier = Modifier.padding(top = 30.dp),
        shape = CircleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient.primaryGradient)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.background,
                contentDescription = null,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
            Text(
                modifier = Modifier.padding(end = 16.dp),
                text = stringResource(id = R.string.title_search),
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}