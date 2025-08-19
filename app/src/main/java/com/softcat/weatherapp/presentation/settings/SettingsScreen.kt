package com.softcat.weatherapp.presentation.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.softcat.weatherapp.R
import com.softcat.weatherapp.presentation.utils.TextIconButton

@Composable
fun SettingsScreen(component: SettingsComponent) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        TextIconButton(
            modifier = Modifier,
            text = stringResource(R.string.send_logs),
            icon = Icons.AutoMirrored.Filled.Send,
            onClick = { component.sendLogs() }
        )
    }
}