package com.softcat.weatherapp.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Web
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.softcat.weatherapp.R
import com.softcat.weatherapp.presentation.utils.TextIconButton
import com.softcat.domain.entity.WebPageType

@Composable
fun SettingsScreen(component: SettingsComponent) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextIconButton(
            modifier = Modifier,
            text = stringResource(R.string.send_logs),
            icon = Icons.AutoMirrored.Filled.Send,
            onClick = { component.sendLogs() }
        )
        TextIconButton(
            text = stringResource(R.string.swagger_interface),
            icon = Icons.Default.Api,
            onClick = { component.openWebPage(WebPageType.SwaggerUI) }
        )
        TextIconButton(
            text = stringResource(R.string.tech_interface),
            icon = Icons.Default.Adb,
            onClick = { component.techInterface() }
        )
        TextIconButton(
            text = stringResource(R.string.documentation_page),
            icon = Icons.Default.Web,
            onClick = { component.openWebPage(WebPageType.Documentation) }
        )
        TextIconButton(
            text = stringResource(R.string.statistic_page),
            icon = Icons.Default.Assessment,
            onClick = { component.openWebPage(WebPageType.Statistics) }
        )
        TextIconButton(
            text = stringResource(R.string.status_page),
            icon = Icons.Default.Call,
            onClick = { component.openWebPage(WebPageType.ServerStatus) }
        )
        TextIconButton(
            text = stringResource(R.string.admin_page),
            icon = Icons.Default.Android,
            onClick = { component.openWebPage(WebPageType.Admin) }
        )
        TextIconButton(
            text = stringResource(R.string.useful_links),
            icon = Icons.Default.AttachFile,
            onClick = { component.openWebPage(WebPageType.UsefulLinks) }
        )
    }
}