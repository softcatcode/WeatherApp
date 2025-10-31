package com.softcat.weatherapp.presentation.web

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebContent(
    link: String,
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
            webView.loadUrl(link)
        }
    )
}

@Composable
fun WebScreen(component: WebComponent) {
    val state = component.model.collectAsState().value
    Scaffold { paddings ->
        when (state) {
            is WebStore.State.Content -> WebContent(state.link, paddings)
        }
    }
}