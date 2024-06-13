package com.example.aquaspot.screens

import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.example.aquaspot.screens.components.customAuthError
import com.example.aquaspot.screens.components.customClickableText
import com.example.aquaspot.screens.components.customImagePicker
import com.example.aquaspot.screens.components.customPasswordInput
import com.example.aquaspot.screens.components.customTextInput
import com.example.aquaspot.screens.components.greyText
import com.example.aquaspot.screens.components.headingText
import com.example.aquaspot.screens.components.inputTextIndicator
import com.example.aquaspot.screens.components.loginRegisterCustomButton
import com.example.aquaspot.screens.components.registerImage
import com.example.aquaspot.viewmodels.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel?,
    navController: NavController?
) {
    val registerFlow = viewModel?.registerFlow?.collectAsState()

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val fullName = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val profileImage = remember { mutableStateOf(Uri.EMPTY) }

    val isEmailError = remember { mutableStateOf(false) }
    val emailErrorText = remember { mutableStateOf("") }

    val isPasswordError = remember { mutableStateOf(false) }
    val passwordErrorText = remember { mutableStateOf("") }

    val isImageError = remember { mutableStateOf(false) }
    val isFullNameError = remember { mutableStateOf(false) }
    val isPhoneNumberError = remember { mutableStateOf(false) }

    val isError = remember { mutableStateOf(false) }
    val errorText = remember { mutableStateOf("") }

    val buttonIsEnabled = remember { mutableStateOf(true) }
    val isLoading = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 120.dp)
        ) {
            registerImage(
                profileImage,
                isImageError
            )
            //Test
            Spacer(modifier = Modifier.height(20.dp))
            headingText(textValue = stringResource(id = R.string.register))
            Spacer(modifier = Modifier.height(5.dp))
            greyText(textValue = stringResource(id = R.string.register_text))
            Spacer(modifier = Modifier.height(20.dp))
            if (isError.value) customAuthError(errorText = errorText.value)
            Spacer(modifier = Modifier.height(20.dp))
            inputTextIndicator(textValue = stringResource(id = R.string.email_input_text))
            Spacer(modifier = Modifier.height(10.dp))
            customTextInput(
                isEmail = false,
                inputValue = email,
                inputText = stringResource(id = R.string.email_example),
                leadingIcon = Icons.Outlined.MailOutline,
                isError = isEmailError,
                errorText = emailErrorText
            )
            Spacer(modifier = Modifier.height(10.dp))
            inputTextIndicator(textValue = stringResource(id = R.string.full_name_text))
            Spacer(modifier = Modifier.height(10.dp))
            customTextInput(
                isEmail = false,
                inputValue = fullName,
                inputText = stringResource(id = R.string.full_name_example_text),
                leadingIcon = Icons.Outlined.Person,
                isError = isFullNameError,
                errorText = emailErrorText
            )
            Spacer(modifier = Modifier.height(10.dp))
            inputTextIndicator(textValue = stringResource(id = R.string.phone_number_text))
            Spacer(modifier = Modifier.height(10.dp))
            customTextInput(
                isEmail = false,
                isNumber = true,
                inputValue = phoneNumber,
                inputText = stringResource(id = R.string.phone_number_example_text),
                leadingIcon = Icons.Outlined.Phone,
                isError = isPhoneNumberError,
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
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .shadow(0.dp)
                .align(Alignment.BottomCenter)
                .padding(20.dp)
        ) {
            loginRegisterCustomButton(
                buttonText = stringResource(id = R.string.register_text),
                isEnabled = buttonIsEnabled,
                isLoading = isLoading,
                onClick = {
                    isImageError.value = false
                    isEmailError.value = false
                    isPasswordError.value = false
                    isImageError.value = false
                    isFullNameError.value = false
                    isPhoneNumberError.value = false
                    isError.value = false
                    isLoading.value = true

                    if(profileImage.value == Uri.EMPTY && profileImage.value != null){
                        isImageError.value = true
                        isLoading.value = false
                    }else if(email.value.isEmpty()){
                        isEmailError.value = true
                        isLoading.value = false
                    }else if(fullName.value.isEmpty()){
                        isFullNameError.value = true
                        isLoading.value = false
                    }else if(phoneNumber.value.isEmpty()){
                        isPhoneNumberError.value = true
                        isLoading.value = false
                    }else if(password.value.isEmpty()){
                        isPasswordError.value = true
                        isLoading.value = false
                    }else {
                        viewModel?.register(
                            fullName = fullName.value,
                            phoneNumber = phoneNumber.value,
                            profileImage = profileImage.value,
                            email = email.value,
                            password = password.value
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            customClickableText(
                firstText = stringResource(id = R.string.already_have_account),
                secondText = stringResource(id = R.string.lets_login),
                onClick = {
                    navController?.navigate(Routes.loginScreen)
                }
            )
        }
    }

    registerFlow?.value.let {
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
                    AuthExceptionsMessages.shortPassword -> {
                        isPasswordError.value = true
                        passwordErrorText.value = stringResource(id = R.string.short_password)
                    }
                    AuthExceptionsMessages.emailUsed -> {
                        isError.value = true
                        errorText.value = stringResource(id = R.string.email_used)
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
                    navController?.navigate(Routes.indexScreen) {
                        popUpTo(Routes.indexScreen) {
                            inclusive = true
                        }
                    }
                }
            }
            null -> Log.d("Test", "Test")
        }
    }
}

@Preview
@Composable
fun showRegisterScreen(){
    RegisterScreen(viewModel = null, navController = null)
}

//    loginFlow?.value.let {
//        when (it) {
//            is Resource.Failure -> {
//                isLoading.value = false
//                Log.d("Error", it.exception.message.toString())
//                val context = LocalContext.current
//                Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
//
//                when (it.exception.message.toString()) {
//                    AuthExceptionsMessages.emptyFields -> {
//                        isEmailError.value = true
//                        isPasswordError.value = true
//                    }
//                    AuthExceptionsMessages.badlyEmailFormat -> {
//                        isEmailError.value = true
//                        emailErrorText.value = stringResource(id = R.string.email_badly_formatted)
//                    }
//                    AuthExceptionsMessages.invalidCredential -> {
//                        isError.value = true
//                        errorText.value = stringResource(id = R.string.credentials_error)
//                    }
//                    else -> {}
//                }
//            }
//            is Resource.loading -> {
//                // Do nothing, as isLoading is already set in onClick
//            }
//            is Resource.Success -> {
//                isLoading.value = false
//                LaunchedEffect(Unit) {
//                    navController?.navigate(Routes.indexScreen) {
//                        popUpTo(Routes.indexScreen) {
//                            inclusive = true
//                        }
//                    }
//                }
//            }
//            null -> Log.d("Test", "Test")
//        }
//    }
//}