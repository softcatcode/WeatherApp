package com.softcat.weatherapp.presentation.authorization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softcat.weatherapp.R
import com.softcat.weatherapp.presentation.ui.theme.DarkBlue
import com.softcat.weatherapp.presentation.ui.theme.LightBlue

@Preview
@Composable
fun LoginField(
    value: String = "",
    onValueChange: (String) -> Unit = {}
) {
    AuthTextField(
        value = value,
        onValueChange = onValueChange,
        trailingIcon = Icons.Filled.AccountCircle,
        hint = stringResource(R.string.login)
    )
}

@Preview
@Composable
fun PasswordField(
    value: String = "",
    onValueChange: (String) -> Unit = {}
) {
    AuthTextField(
        modifier = Modifier.padding(vertical = 16.dp),
        value = value,
        onValueChange = onValueChange,
        trailingIcon = Icons.Filled.Lock,
        hint = stringResource(R.string.password)
    )
}

@Preview
@Composable
fun RepeatPasswordField(
    value: String = "",
    onValueChange: (String) -> Unit = {}
) {
    AuthTextField(
        modifier = Modifier.padding(vertical = 16.dp),
        value = value,
        onValueChange = onValueChange,
        trailingIcon = Icons.Filled.Repeat,
        hint = stringResource(R.string.password_repeat)
    )
}

@Preview
@Composable
fun SignInInputs(
    modifier: Modifier = Modifier,
    login: String = "Daniil",
    password: String = "482550",
    repeatedPsw: String = "482550",
    onLoginChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onRepeatPswChanged: (String) -> Unit = {},
    onSignInClicked: () -> Unit = {},
    onSwitchToLogIn: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthTitle(
            text = stringResource(R.string.sign_in)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            LoginField(
                value = login,
                onValueChange = onLoginChanged
            )
            PasswordField(
                value = password,
                onValueChange = onPasswordChanged
            )
            RepeatPasswordField(
                value = repeatedPsw,
                onValueChange = onRepeatPswChanged
            )
            AuthButton(
                text = stringResource(R.string.sign_in),
                onClick = onSignInClicked,
                color = DarkBlue
            )
            AuthDelimiter()
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            contentAlignment = Alignment.Center
        ) {
            AuthButton(
                text = stringResource(R.string.log_in),
                onClick = onSwitchToLogIn,
                color = LightBlue
            )
        }
    }
}

@Preview
@Composable
fun LogInInputs(
    modifier: Modifier = Modifier,
    login: String = "Daniil",
    password: String = "482550",
    onLoginChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onLogInClicked: () -> Unit = {},
    onSwitchToSignIn: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthTitle(
            text = stringResource(R.string.log_in)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .then(modifier),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            LoginField(
                value = login,
                onValueChange = onLoginChanged
            )
            PasswordField(
                value = password,
                onValueChange = onPasswordChanged
            )
            AuthButton(
                text = stringResource(R.string.log_in),
                onClick = onLogInClicked,
                color = LightBlue
            )
            AuthDelimiter()
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f),
            contentAlignment = Alignment.Center
        ) {
            AuthButton(
                text = stringResource(R.string.sign_in),
                onClick = onSwitchToSignIn,
                color = DarkBlue
            )
        }

    }
}

@Composable
fun AuthorizationScreen(component: AuthorizationComponent) {
    val model = component.model.collectAsState()
    val state = model.value
    Scaffold(
        topBar = { AuthTopBar() }
    ) { paddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .padding(horizontal = 8.dp)
        ) {
            when (state.type) {
                AuthorizationStore.State.ScreenType.Initial -> {}
                AuthorizationStore.State.ScreenType.LogIn -> {
                    LogInInputs(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        login = state.login,
                        password = state.password,
                        onLoginChanged = { component.changeLogin(it) },
                        onPasswordChanged = { component.changePassword(it) },
                        onSwitchToSignIn = { component.switchToSignIn() },
                        onLogInClicked = { component.enter(state.login, state.password) }
                    )
                }
                is AuthorizationStore.State.ScreenType.SignIn -> {
                    SignInInputs(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        login = state.login,
                        password = state.password,
                        repeatedPsw = state.type.repeatPassword,
                        onLoginChanged = { component.changeLogin(it) },
                        onPasswordChanged = { component.changePassword(it) },
                        onSwitchToLogIn = { component.switchToLogIn() },
                        onRepeatPswChanged = { component.changeRepeatedPassword(it) },
                        onSignInClicked = {
                            component.register(
                                state.login,
                                state.password,
                                state.type.repeatPassword
                            )
                        }
                    )
                }
            }
            if (state.isLoading) {
                ProgressBar(
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
            state.error?.let {
                ErrorLabel(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    error = it
                )
            }
        }
    }
}