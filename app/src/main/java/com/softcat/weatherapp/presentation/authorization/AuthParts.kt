package com.softcat.weatherapp.presentation.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softcat.weatherapp.R
import com.softcat.weatherapp.presentation.ui.theme.DarkBlue
import com.softcat.weatherapp.presentation.ui.theme.LightBlue
import com.softcat.weatherapp.presentation.ui.theme.OutlineColorFocused
import com.softcat.weatherapp.presentation.ui.theme.OutlineColorUnfocused

@Preview
@Composable
fun AuthTitle(
    modifier: Modifier = Modifier,
    text: String = "Sign in"
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .then(modifier)
    ) {
        Spacer(Modifier.height(10.dp))
        Text(
            modifier = Modifier.wrapContentHeight(),
            text = text,
            style = MaterialTheme.typography.headlineLarge,
            color = LightBlue,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(10.dp))
    }
}

@Preview
@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    hint: String = "hint",
    onValueChange: (String) -> Unit = {},
    trailingIcon: ImageVector = Icons.Filled.AccountCircle
) {
    OutlinedTextField(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .then(modifier),
        textStyle = MaterialTheme.typography.bodyLarge,
        value = value,
        onValueChange = onValueChange,
        colors = OutlinedTextFieldDefaults.colors().copy(
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedIndicatorColor = OutlineColorUnfocused,
            focusedIndicatorColor = OutlineColorFocused
        ),
        placeholder = {
            Text(
                modifier = Modifier.wrapContentHeight(),
                text = hint,
                style = MaterialTheme.typography.bodyLarge,
                color = OutlineColorUnfocused
            )
        },
        trailingIcon = {
            Icon(
                modifier = Modifier.size(48.dp).padding(end = 8.dp),
                imageVector = trailingIcon,
                contentDescription = null,
                tint = OutlineColorUnfocused
            )
        },
        shape = RoundedCornerShape(50)
    )
}

@Preview
@Composable
fun AuthButton(
    modifier: Modifier = Modifier,
    text: String = "Log in",
    onClick: () -> Unit = {},
    color: Color = LightBlue
) {
    Button(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(0.8f)
            .then(modifier),
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = color
        )
    ) {
        Text(
            modifier = Modifier.wrapContentHeight().fillMaxWidth(),
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun ProgressBar(
    modifier: Modifier = Modifier
) {
    Box (
        modifier = Modifier
            .size(70.dp)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = DarkBlue,
            trackColor = LightBlue,
            strokeWidth = 5.dp,
            strokeCap = StrokeCap.Butt
        )
    }
}

@Preview
@Composable
fun ErrorLabel(
    modifier: Modifier = Modifier,
    error: Throwable = Exception("error description")
) {
    Text(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .then(modifier),
        text = error.localizedMessage ?:
            error.cause?.localizedMessage ?:
            stringResource(R.string.error_title),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = Color.Red,
        textAlign = TextAlign.Center,
    )
}

@Preview
@Composable
fun AuthDelimiter(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .weight(1f)
                .background(OutlineColorUnfocused)
        )
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .width(60.dp),
            text = stringResource(R.string.or),
            style = MaterialTheme.typography.headlineSmall,
            color = OutlineColorUnfocused,
            textAlign = TextAlign.Center
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .weight(1f)
                .background(OutlineColorUnfocused)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AuthTopBar() {
    TopAppBar(
        navigationIcon = {
            Icon(
                modifier = Modifier.size(36.dp).padding(start = 8.dp),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = Color.White
            )
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineSmall,
                text = stringResource(R.string.auth_title),
                textAlign = TextAlign.Center,
                color = Color.White
            )
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = DarkBlue
        )
    )
}
