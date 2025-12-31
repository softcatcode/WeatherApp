package com.softcat.weatherapp.presentation.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.softcat.weatherapp.R
import com.softcat.weatherapp.presentation.calendar.exo2FontFamily
import com.softcat.weatherapp.presentation.ui.theme.CustomPink

@Composable
fun TextIconButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit = {}
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(30),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = text,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = icon,
                contentDescription = null,
                tint = Color.Red
            )
        }

    }
}

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
            tint = if (isActive) CustomPink else MaterialTheme.colorScheme.background
        )
    }
}

@Composable
fun MainBottomBar(
    onSelectIndex: (Int) -> Unit = {},
    currentIndex: Int
) {
    BottomAppBar(
        actions = {
            NavigationButton(
                icon = Icons.Filled.Home,
                width = 0.5f,
                isActive = currentIndex == 0,
                onClick = { onSelectIndex(0) }
            )
            NavigationButton(
                icon = Icons.Filled.Person,
                width = 1f,
                isActive = currentIndex == 1,
                onClick = { onSelectIndex(1) }
            )
        },
        containerColor = MaterialTheme.colorScheme.primary
    )
}