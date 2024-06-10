package com.example.aquaspot.screens

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
import com.example.aquaspot.screens.components.customClickableText
import com.example.aquaspot.screens.components.customPasswordInput
import com.example.aquaspot.screens.components.customTextInput
import com.example.aquaspot.screens.components.greyText
import com.example.aquaspot.screens.components.headingText
import com.example.aquaspot.screens.components.inputTextIndicator
import com.example.aquaspot.screens.components.loginRegisterCustomButton
import com.example.aquaspot.screens.components.loginRegisterImage
import com.example.aquaspot.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel?,
    navController: NavController?
){
    val loginFlow = viewModel?.loginFlow?.collectAsState()
    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val isEmailError = remember {
        mutableStateOf(false)
    }
    val emailErrorText = remember{
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ){
        loginRegisterImage()
        headingText(textValue = stringResource(id = R.string.welcome_text))
        Spacer(modifier = Modifier.height(5.dp))
        greyText(textValue = stringResource(id = R.string.login_text))
        Spacer(modifier = Modifier.height(30.dp))
        inputTextIndicator(textValue = stringResource(id = R.string.email_input_text))
        Spacer(modifier = Modifier.height(10.dp))
        customTextInput(
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
            leadingIcon = Icons.Outlined.Lock
        )
        Spacer(modifier = Modifier.height(70.dp))
        loginRegisterCustomButton(
            buttonText = stringResource(id = R.string.login_button),
            onClick = {
                viewModel?.login(email.value, password.value)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        customClickableText(firstText = "Još uvek nemate nalog? ", secondText = "Registruj se", onClick = {
            navController?.navigate(Routes.indexScreen);
        })
    }

    loginFlow?.value.let{
        when(it){
            is Resource.Failure -> {
                Log.d("Error", it.exception.message.toString())
                val context = LocalContext.current
                Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                if(it.exception.message.toString() == "The email address is badly formatted.") {
                    isEmailError.value = true
                    emailErrorText.value = it.exception.message.toString()
                }else{}
            }
            is Resource.loading -> {
                Log.d("Loading", "Loading")
            }
            is Resource.Success -> {
                LaunchedEffect(Unit) {
                    navController?.navigate(Routes.indexScreen){
                        popUpTo(Routes.indexScreen){
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
fun LoginScreenPreview(){
    LoginScreen(viewModel = null, navController = null)
}
