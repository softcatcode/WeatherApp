package com.softcat.weatherapp.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.softcat.weatherapp.R

@Preview
@Composable
fun ErrorDismissButton(
    onClick: () -> Unit = {}
) {
    IconButton(onClick = onClick) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = Icons.Filled.Close,
            tint = Color.Black,
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ErrorDialog(
    modifier: Modifier = Modifier,
    throwable: Throwable = Throwable(message = "Error message."),
    onDismissRequest: () -> Unit = {},
) {
    BasicAlertDialog(
        modifier = Modifier
            .background(Color.Transparent)
            .then(modifier),
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            shape = RoundedCornerShape(15)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        imageVector = Icons.Default.Error,
                        tint = Color.Red.copy(alpha = 0.8f),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = stringResource(id = R.string.error_title),
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.weight(1f))
                    ErrorDismissButton(onClick = onDismissRequest)
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(11.dp)
                        .padding(bottom = 10.dp)
                        .background(MaterialTheme.colorScheme.secondary)
                )
                Text(
                    text = throwable.message.orEmpty(),
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}