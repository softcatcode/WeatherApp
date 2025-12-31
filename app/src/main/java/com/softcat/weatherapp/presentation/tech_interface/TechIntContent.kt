package com.softcat.weatherapp.presentation.tech_interface

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcat.weatherapp.presentation.ui.theme.CustomPink
import com.softcat.weatherapp.presentation.ui.theme.CustomPurple

@Composable
@Preview
fun UseCase(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    name: String = "UseCase",
    onClick: () -> Unit = {}
) {
    val bkg = if (isSelected) CustomPurple else CustomPink
    Card(
        modifier = Modifier
            .wrapContentSize()
            .then(modifier),
        shape = RoundedCornerShape(20),
        colors = CardDefaults.cardColors(containerColor = bkg),
        border = BorderStroke(1.dp, CustomPurple),
        onClick = onClick
    ) {
        Text(
            modifier = Modifier.padding(5.dp),
            color = Color.White,
            text = name,
            fontSize = 36.sp
        )
    }
}

@Composable
@Preview
fun UseCases(
    modifier: Modifier = Modifier,
    onSelectIndex: (Int) -> Unit = {},
    selectedIndex: Int = 0
) {
    val useCases = listOf(
        "AddToFavourites",
        "RemoveFromFavourite",
        "GetFavourites",
        "GetForecast",
        "GetCurrentWeather",
        "SearchCity",
        "Register",
        "LogIn"
    )
    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(useCases, key = { index, elem -> elem }) { index, elem ->
            UseCase(
                name = elem,
                onClick = { onSelectIndex(index) },
                isSelected = selectedIndex == index
            )
        }
    }
}

@Composable
@Preview
fun ExecuteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = CustomPink),
        border = BorderStroke(2.dp, CustomPurple),
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = "Execute",
            fontSize = 32.sp
        )
    }
}

@Composable
@Preview
fun InputField(
    modifier: Modifier = Modifier,
    text: String = "",
    onTextChanged: (String) -> Unit = {}
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .then(modifier),
        value = text,
        onValueChange = onTextChanged,
        placeholder = {
            Text(
                text = "Enter an argument",
                fontSize = 12.sp
            )
        },
        shape = RoundedCornerShape(10),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        )
    )
}

@Composable
fun TechIntContent(component: TechIntComponent) {
    val state by component.model.collectAsState()
    val arg = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, top = 20.dp),
    ) {
        UseCases(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            onSelectIndex = { component.selectUseCase(it) },
            selectedIndex = state.selectedUseCaseIndex
        )
        InputField(
            text = arg.value,
            onTextChanged = { arg.value = it }
        )
        ExecuteButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            when (state.selectedUseCaseIndex) {
                0 -> component.addToFavourites(arg.value)
                1 -> component.removeFromFavourites(arg.value.toIntOrNull() ?: 0)
                2 -> component.getFavouriteCities()
                3 -> component.getForecast(arg.value)
                4 -> component.getCurrentWeather(arg.value)
                5 -> component.search(arg.value)
                6 -> {
                    val words = arg.value.split(' ')
                    if (words.size >= 3)
                        component.register(words[0], words[1], words[2])
                }
                7 -> {
                    val words = arg.value.split(' ')
                    if (words.size >= 2)
                        component.logIn(words[0], words[1])
                }
            }
        }
        Text(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            text = state.answer,
            fontSize = 16.sp,
        )
    }
}