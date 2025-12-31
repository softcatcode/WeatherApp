package com.softcat.weatherapp.presentation.web

import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
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
import timber.log.Timber

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

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    databaseEnabled = true
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }

                webViewClient = object : WebViewClient() {
                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        Timber.e("WebView: Page load error: ${error?.description} for ${request?.url}")
                    }

                    override fun onReceivedHttpError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        errorResponse: WebResourceResponse?
                    ) {
                        Timber.e("WebView: HTTP ${errorResponse?.statusCode} for ${request?.url}")
                    }
                }
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