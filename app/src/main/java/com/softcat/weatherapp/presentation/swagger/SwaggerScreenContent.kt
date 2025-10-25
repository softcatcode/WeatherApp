package com.softcat.weatherapp.presentation.swagger

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun SwaggerContent(
    swaggerLink: String,
    paddings: PaddingValues
) {
    AndroidView(
        modifier = Modifier.fillMaxSize().padding(paddings),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
            }
        },
        update = { webView ->
            webView.loadUrl(swaggerLink)
        }
    )
}

@Composable
fun SwaggerScreen(component: SwaggerComponent) {
    val state = component.model.collectAsState().value
    Scaffold { paddings ->
        when (state) {
            is SwaggerStore.State.Content -> SwaggerContent(state.swaggerLink, paddings)
        }
    }
}