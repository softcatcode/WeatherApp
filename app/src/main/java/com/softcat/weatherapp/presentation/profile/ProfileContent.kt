package com.softcat.weatherapp.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcat.domain.entity.User
import com.softcat.weatherapp.R

@Composable
fun UserTextData(
    modifier: Modifier = Modifier,
    user: User
) {
    val labels = listOf(
        "id",
        "name",
        "email",
        "password",
        "role"
    )
    val values = listOf(
        user.id,
        user.name,
        user.email,
        user.password,
        user.role.name
    )
    Row(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(0.3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            labels.forEach {
                Text(
                    modifier = Modifier
                        .padding(top = 25.dp, bottom = 10.dp),
                    text = it,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.Black.copy(alpha = 0.5f))
                )
            }
        }
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            values.forEach {
                Text(
                    modifier = Modifier
                        .padding(top = 25.dp, bottom = 10.dp),
                    text = it,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.Black.copy(alpha = 0.5f))
                )
            }
        }
    }
}

@Composable
fun AvatarCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(
            modifier = Modifier
                .size(64.dp)
                .padding(4.dp),
            imageVector = Icons.Filled.Person,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
fun SettingsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape
    ) {
        Icon(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxSize()
                .padding(8.dp),
            imageVector = Icons.Filled.Settings,
            contentDescription = stringResource(R.string.settings),
            tint = Color.White
        )
    }
}

@Composable
fun ProfileScreenButton(
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
fun OptionPanel(
    modifier: Modifier = Modifier,
    onExitClick: () -> Unit,
    onClearWeatherDataClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileScreenButton(
            modifier = Modifier.fillMaxWidth(0.4f),
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            text = stringResource(R.string.exit),
            onClick = onExitClick
        )
        ProfileScreenButton(
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Outlined.Delete,
            text = stringResource(R.string.clear),
            onClick = onClearWeatherDataClick
        )
    }
}

@Composable
@Preview(showBackground = true)
fun UserInfoScreen(
    user: User = User(
        id = "4721436512894512689",
        name = "Mike",
        role = User.Status.Premium,
        email = "test@email.com",
        password = "12345"
    ),
    onSettingsClick: () -> Unit = {},
    onExitClick: () -> Unit = {},
    onClearWeatherDataClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column {
            UserTextData(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(32.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                user = user,
            )
            OptionPanel(
                modifier = Modifier
                    .fillMaxWidth(),
                onExitClick = onExitClick,
                onClearWeatherDataClick = onClearWeatherDataClick
            )
        }
        AvatarCard(
            modifier = Modifier.wrapContentSize()
        )
        SettingsButton(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.BottomEnd),
            onClick = onSettingsClick
        )
    }
}

@Composable
fun ProfileContent(component: ProfileComponent) {
    val model = component.model.collectAsState()

    when (val state = model.value) {
        is ProfileStore.State.Content -> {
            UserInfoScreen(
                user = state.user,
                onSettingsClick = { component.openSettings() },
                onClearWeatherDataClick = { component.clearWeatherData() }
            )
        }
    }
}