package com.example.aquaspot.screens

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aquaspot.Navigation.Routes
import com.example.aquaspot.R
import com.example.aquaspot.data.Resource
import com.example.aquaspot.exceptions.AuthExceptionsMessages
import com.example.aquaspot.location.LocationService
import com.example.aquaspot.screens.components.customAuthError
import com.example.aquaspot.screens.components.customClickableText
import com.example.aquaspot.screens.components.customPasswordInput
import com.example.aquaspot.screens.components.customTextInput
import com.example.aquaspot.screens.components.greyText
import com.example.aquaspot.screens.components.headingText
import com.example.aquaspot.screens.components.inputTextIndicator
import com.example.aquaspot.screens.components.loginImage
import com.example.aquaspot.screens.components.loginRegisterCustomButton
import com.example.aquaspot.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel?,
    navController: NavController
) {
    val loginFlow = viewModel?.loginFlow?.collectAsState()
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val isEmailError = remember { mutableStateOf(false) }
    val emailErrorText = remember { mutableStateOf("") }

    val isPasswordError = remember { mutableStateOf(false) }
    val passwordErrorText = remember { mutableStateOf("") }

    val isError = remember { mutableStateOf(false) }
    val errorText = remember { mutableStateOf("") }

    val buttonIsEnabled = remember { mutableStateOf(true) }
    val isLoading = remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        loginImage()
        headingText(textValue = stringResource(id = R.string.welcome_text))
        Spacer(modifier = Modifier.height(5.dp))
        greyText(textValue = stringResource(id = R.string.login_text))
        Spacer(modifier = Modifier.height(20.dp))
        if (isError.value) customAuthError(errorText = errorText.value)
        Spacer(modifier = Modifier.height(20.dp))
        inputTextIndicator(textValue = stringResource(id = R.string.email_input_text))
        Spacer(modifier = Modifier.height(10.dp))
        customTextInput(
            isEmail = true,
            inputValue = email,
            inputText = stringResource(id = R.string.email_example),
            leadingIcon = Icons.Outlined.Email,
            isError = isEmailError,
            errorText = emailErrorText
        )
        Spacer(modifier = Modifier.height(10.dp))
        inputTextIndicator(textValue = stringResource(id = R.string.password_input_text))
        Spacer(modifier = Modifier.height(10.dp))
        customPasswordInput(
            inputValue = password,
            inputText = stringResource(id = R.string.password_example),
            leadingIcon = Icons.Outlined.Lock,
            isError = isPasswordError,
            errorText = passwordErrorText
        )
        Spacer(modifier = Modifier.height(50.dp))
        loginRegisterCustomButton(
            buttonText = stringResource(id = R.string.login_button),
            isEnabled = buttonIsEnabled,
            isLoading = isLoading,
            onClick = {
                isEmailError.value = false
                isPasswordError.value = false
                isError.value = false
                isLoading.value = true
                viewModel?.login(email.value, password.value)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        customClickableText(firstText = "Još uvek nemate nalog? ", secondText = "Registruj se", onClick = {
            navController.navigate(Routes.registerScreen)
        })
    }

    loginFlow?.value.let {
        when (it) {
            is Resource.Failure -> {
                isLoading.value = false
                Log.d("Error", it.exception.message.toString())
//                val context = LocalContext.current
//                Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()

                when (it.exception.message.toString()) {
                    AuthExceptionsMessages.emptyFields -> {
                        isEmailError.value = true
                        isPasswordError.value = true
                    }
                    AuthExceptionsMessages.badlyEmailFormat -> {
                        isEmailError.value = true
                        emailErrorText.value = stringResource(id = R.string.email_badly_formatted)
                    }
                    AuthExceptionsMessages.invalidCredential -> {
                        isError.value = true
                        errorText.value = stringResource(id = R.string.credentials_error)
                    }

                    else -> {}
                }
            }
            is Resource.loading -> {
                // Do nothing, as isLoading is already set in onClick
            }
            is Resource.Success -> {
                isLoading.value = false
                LaunchedEffect(Unit) {
                    navController.navigate(Routes.indexScreen) {
                        popUpTo(Routes.indexScreen) {
                            inclusive = true
                        }
                    }
                }
            }
            null -> {}
        }
    }
}