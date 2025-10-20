package com.softcat.weatherapp.presentation.web

import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebContent(htmlContent: String) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
        }
    )
}

@Composable
fun WebError() {

}

@Composable
fun WebLoading() {

}

@Composable
fun WebScreen(component: WebComponent) {
    val state = component.model.collectAsState().value
    Scaffold(

    ) { paddings ->
        Box(
            modifier = Modifier.padding(paddings)
        ) {
            when (state) {
                is WebStore.State.Content -> WebContent(state.htmlContent)
                WebStore.State.Error -> WebError()
                WebStore.State.Loading -> WebLoading()
            }
        }
    }

}