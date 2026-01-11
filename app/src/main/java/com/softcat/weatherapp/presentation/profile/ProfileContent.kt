package com.softcat.weatherapp.presentation.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.softcat.domain.entity.User
import com.softcat.weatherapp.R
import com.softcat.weatherapp.presentation.ui.theme.CustomPink
import com.softcat.weatherapp.presentation.ui.theme.DarkBlue
import com.softcat.weatherapp.presentation.ui.theme.WarningColor
import com.softcat.weatherapp.presentation.utils.ProgressBar

@Composable
fun TextIconButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit = {},
    background: Color
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = background),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 16.dp
        ),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                text = text,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }

    }
}

@Preview
@Composable
fun StringDataItem(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Filled.Person,
    hint: String = "hint",
    text: String = "some text"
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .then(modifier),
        textStyle = LocalTextStyle.current.copy(
            fontSize = 18.sp
        ),
        singleLine = true,
        value = text,
        onValueChange = {},
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.onBackground,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
            disabledBorderColor = MaterialTheme.colorScheme.onBackground,
            errorBorderColor = MaterialTheme.colorScheme.onBackground,
            disabledTextColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
        ),
        label = {
            Text(
                text = hint,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = hint,
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
    )
}

@Composable
fun OptionPanel(
    modifier: Modifier = Modifier,
    onExitClick: () -> Unit,
    onClearWeatherDataClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextIconButton(
            modifier = Modifier.wrapContentHeight(),
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            text = stringResource(R.string.exit),
            onClick = onExitClick,
            background = MaterialTheme.colorScheme.tertiary
        )
        TextIconButton(
            modifier = Modifier.wrapContentHeight(),
            icon = Icons.Outlined.Delete,
            text = stringResource(R.string.clear),
            onClick = onClearWeatherDataClick,
            background = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
fun UserDataSheet(
    modifier: Modifier = Modifier,
    user: User,
    onExitClick: () -> Unit,
    onClearWeatherDataClick: () -> Unit
) {
    val icons = listOf(
        Icons.Default.Info,
        Icons.Default.Person,
        Icons.Default.Email,
        Icons.Default.Lock,
        Icons.Default.Check
    )
    val data = with(user) {
        listOf(id, name, email, password, role.name)
    }
    val hints = listOf(
        stringResource(R.string.user_id),
        stringResource(R.string.user_name),
        stringResource(R.string.user_email),
        stringResource(R.string.user_password),
        stringResource(R.string.user_role)
    )
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 16.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 80.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (i in data.indices) {
                StringDataItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    hint = hints[i],
                    text = data[i],
                    icon = icons[i]
                )
            }
            OptionPanel(
                onExitClick = onExitClick,
                onClearWeatherDataClick = onClearWeatherDataClick
            )
        }
    }
}

@Composable
fun AvatarCard(
    modifier: Modifier = Modifier,
    avatarState: ProfileStore.State.AvatarState,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        onClick = onClick
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            if (avatarState.avatar != null) {
                Image(
                    modifier = Modifier.size(90.dp),
                    bitmap = avatarState.avatar.image.asImageBitmap(),
                    contentDescription = stringResource(R.string.avatar_image_description),
                    contentScale = ContentScale.Fit
                )
            } else {
                Icon(
                    modifier = Modifier
                        .size(90.dp)
                        .padding(20.dp),
                    imageVector = Icons.Filled.PhotoCamera,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            if (avatarState.updating) {
                ProgressBar(
                    modifier = Modifier.size(90.dp),
                    color = CustomPink.copy(alpha = 0.3f)
                )
            }
        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.profile_title),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = DarkBlue
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
    avatarState: ProfileStore.State.AvatarState = ProfileStore.State.AvatarState(),
    onSettingsClick: () -> Unit = {},
    onExitClick: () -> Unit = {},
    onClearWeatherDataClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            ProfileTopBar(
                Modifier.background(MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        val scrollState = rememberLazyListState()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            val color = MaterialTheme.colorScheme.tertiary
            Canvas(
                modifier = Modifier.fillMaxSize(),
                contentDescription = ""
            ) {
                val scroll = scrollState.firstVisibleItemScrollOffset
                drawCircle(
                    color = color,
                    radius = size.height / 2f + 2f * scroll,
                    center = Offset(size.width / 2, -3f * scroll)
                )
            }
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                item {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        UserDataSheet(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 32.dp, bottom = 16.dp),
                            user = user,
                            onClearWeatherDataClick = onClearWeatherDataClick,
                            onExitClick = onExitClick
                        )
                        AvatarCard(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .wrapContentSize(),
                            avatarState = avatarState,
                            onClick = onAvatarClick
                        )
                    }
                }
                item { Spacer(Modifier.height(250.dp)) }
            }

            SettingsButton(
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp)
                    .size(64.dp)
                    .align(Alignment.BottomEnd),
                onClick = onSettingsClick
            )
        }
    }

}

@Composable
@Preview
fun ConfirmDialog(
    message: String = "Default message",
    onConfirmClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_dialog_alert),
                contentDescription = stringResource(R.string.confirm_clear_weather),
                tint = WarningColor,
                modifier = Modifier.size(48.dp)
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontSize = 14.sp
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onDismissRequest,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(4f)
                ) {
                    Text(
                        text = stringResource(R.string.delay),
                        fontSize = 14.sp
                    )
                }

                Button(
                    onClick = onConfirmClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.weight(5f)
                ) {
                    Text(
                        text = stringResource(R.string.confirm),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

private fun checkReadImagePermission(context: Context) = ContextCompat.checkSelfPermission(
    context,
    Manifest.permission.READ_MEDIA_IMAGES
) == PackageManager.PERMISSION_GRANTED

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(component: ProfileComponent) {
    val model by component.model.collectAsState()

    val context = LocalContext.current
    val permissionWarning = stringResource(R.string.deny_read_images_warning)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        component.saveAvatar(context, it, model.user.id)
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launcher.launch("image/*")
        } else {
            Toast.makeText(context, permissionWarning, Toast.LENGTH_SHORT).show()
        }
    }

    UserInfoScreen(
        user = model.user,
        avatarState = model.avatarState,
        onSettingsClick = { component.openSettings() },
        onClearWeatherDataClick = { component.showDialog() },
        onAvatarClick = {
            if (checkReadImagePermission(context))
                launcher.launch("image/*")
            else
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        },
        onExitClick = { component.exit() }
    )
    if (model.showDialog) {
        BasicAlertDialog(
            onDismissRequest = { component.dismissDialog() }
        ) {

            ConfirmDialog(
                message = stringResource(R.string.confirm_clear_weather),
                onConfirmClick = {
                    component.clearWeatherData()
                },
                onDismissRequest = { component.dismissDialog() }
            )
        }
    }
}