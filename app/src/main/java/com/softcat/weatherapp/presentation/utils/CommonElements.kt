package com.softcat.weatherapp.presentation.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.softcat.weatherapp.R
import com.softcat.weatherapp.presentation.calendar.exo2FontFamily
import com.softcat.weatherapp.presentation.root.RootComponent
import com.softcat.weatherapp.presentation.ui.theme.CalendarPink

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

@Composable
fun NavigationButton(
    width: Float = 1f,
    icon: ImageVector = Icons.Filled.Home,
    isActive: Boolean,
    onClick: () -> Unit = {}
) {
    IconButton(
        modifier = Modifier
            .fillMaxWidth(width),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = icon,
            contentDescription = null,
            tint = if (isActive) CalendarPink else MaterialTheme.colorScheme.background
        )
    }
}

@Composable
fun MainBottomBar(
    onClick: (RootComponent.MainScreenSelection) -> Unit = {},
    selection: RootComponent.MainScreenSelection
) {
    BottomAppBar(
        actions = {
            NavigationButton(
                icon = Icons.Filled.Home,
                width = 0.5f,
                isActive = selection == RootComponent.MainScreenSelection.Favourites,
                onClick = { onClick(RootComponent.MainScreenSelection.Favourites) }
            )
            NavigationButton(
                icon = Icons.Filled.Person,
                width = 1f,
                isActive = selection == RootComponent.MainScreenSelection.Profile,
                onClick = { onClick(RootComponent.MainScreenSelection.Profile) }
            )
        },
        containerColor = MaterialTheme.colorScheme.primary
    )
}