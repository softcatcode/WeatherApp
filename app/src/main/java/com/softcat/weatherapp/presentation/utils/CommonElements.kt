package com.softcat.weatherapp.presentation.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.softcat.weatherapp.R
import com.softcat.weatherapp.presentation.calendar.exo2FontFamily

@Composable
fun LoadingGifCloud(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current.applicationContext

    val imgLoader = ImageLoader.Builder(context)
        .components {
            add(ImageDecoderDecoder.Factory())
        }.build()
    val imgReqBuilder = ImageRequest.Builder(context)
        .data(R.drawable.loading_animation)
        .apply { size(Size.ORIGINAL) }
        .build()

    val painter = rememberAsyncImagePainter(imgReqBuilder, imgLoader)
    Column(
        modifier = Modifier.then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Text(
            text = stringResource(R.string.loading_auth),
            fontFamily = exo2FontFamily,
            style = MaterialTheme.typography.titleMedium
        )
    }
}