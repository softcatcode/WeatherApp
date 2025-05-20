package com.softcat.weatherapp.presentation.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun SwitchRegionStorageMethodButton(
    localRegionStorage: Boolean = true,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = Modifier.width(150.dp).height(40.dp),
        onClick = onClick
    ) {
        Text(
            text = if (localRegionStorage) "Local" else "Remote",
            fontSize = 16.sp,
            color = Color.Black,
        )
    }
}

@Composable
fun SettingsScreen(component: SettingsComponent) {
    val model = component.model.collectAsState()
    val state = model.value
    Scaffold { paddings ->
        Box(
            modifier = Modifier.padding(paddings),
            contentAlignment = Alignment.TopCenter
        ) {
            when (state) {
                SettingsStore.State.Initial -> {}
                is SettingsStore.State.Settings -> {
                    SwitchRegionStorageMethodButton(
                        localRegionStorage = state.localRegionStorage,
                        onClick = { component.switchLab9Setting() }
                    )
                }
            }
        }
    }
}