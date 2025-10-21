package com.softcat.weatherapp.presentation.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.Web
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.softcat.weatherapp.R
import com.softcat.weatherapp.presentation.utils.TextIconButton
import java.net.URI
import androidx.core.net.toUri

@Composable
fun SettingsScreen(component: SettingsComponent) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current.applicationContext
        TextIconButton(
            modifier = Modifier,
            text = stringResource(R.string.send_logs),
            icon = Icons.AutoMirrored.Filled.Send,
            onClick = { component.sendLogs() }
        )
        TextIconButton(
            text = stringResource(R.string.swagger_interface),
            icon = Icons.Default.Web,
            onClick = { component.swaggerInterface() }
        )
    }
}