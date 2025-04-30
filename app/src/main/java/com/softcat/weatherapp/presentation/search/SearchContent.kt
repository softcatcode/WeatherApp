package com.softcat.weatherapp.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.softcat.domain.entity.City
import com.softcat.weatherapp.R

@Composable
private fun CityCard(city: City, onClick: (City) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .clickable { onClick(city) }
        ) {
            Text(
                text = city.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = city.country,
                color = Color.Black,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(component: SearchComponent) {
    val state by component.model.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) { focusRequester.requestFocus() }

    TextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black
    )
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = state.searchQuery,
                onQueryChange = { component.changeSearchQuery(it) },
                onSearch = {},
                expanded = true,
                onExpandedChange = {},
                enabled = true,
                placeholder = {
                    Text(
                        text = stringResource(R.string.title_search),
                        color = Color.Black
                    )
                },
                leadingIcon = {
                    IconButton(onClick = { component.back() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Color.Black
                        )
                    }
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.title_search),
                        tint = Color.Black
                    )
                },
                colors = SearchBarDefaults.inputFieldColors(),
                interactionSource = null,
            )
        },
        expanded = true,
        onExpandedChange = {},
        modifier = Modifier.focusRequester(focusRequester),
        shape = SearchBarDefaults.inputFieldShape,
        colors = colors(),
        tonalElevation = SearchBarDefaults.TonalElevation,
        shadowElevation = SearchBarDefaults.ShadowElevation,
        windowInsets = SearchBarDefaults.windowInsets,
        content = {
            when (val searchState = state.searchState) {
                SearchStore.State.SearchState.EmptyResult -> {
                    Text(
                        text = stringResource(id = R.string.title_empty_search_result),
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }

                SearchStore.State.SearchState.Error -> {
                    Text(
                        text = stringResource(id = R.string.title_empty_search_result),
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }

                SearchStore.State.SearchState.Initial -> {}

                SearchStore.State.SearchState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
                    }
                }

                is SearchStore.State.SearchState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(
                            items = searchState.cities,
                            key = { it.id }
                        ) { city ->
                            CityCard(city) { component.clickCity(it) }
                        }
                    }
                }
            }
        },
    )
}