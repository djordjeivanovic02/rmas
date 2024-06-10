package com.example.aquaspot.screens.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquaspot.R
import com.example.aquaspot.ui.theme.greyTextColor
import com.example.aquaspot.ui.theme.mainColor
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun loginRegisterImage(){
Box(modifier = Modifier
    .fillMaxWidth()
    .padding(20.dp),

    contentAlignment = Alignment.Center){
        Image(painter = painterResource(id = R.drawable.loginimage),
            contentDescription = "Login Image",
            modifier = Modifier
                .width(210.dp)
                .height(210.dp)
        )
    }
}

@Composable
fun headingText(textValue: String){
    Text(style = TextStyle(
        color = Color.Black,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
        ),
        text = textValue
    )
}

@Composable
fun greyText(textValue: String){
    Text(style = TextStyle(
        color = greyTextColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    ),
        text = textValue
    )
}

@Composable
fun inputTextIndicator(textValue: String){
    Text(style = TextStyle(
        color = Color.Black,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
    ),
        text = textValue
    )
}

@Composable
fun customTextInput(
    inputValue: MutableState<String>,
    inputText: String,
    leadingIcon: ImageVector,
    isError: MutableState<Boolean>,
    errorText: MutableState<String>
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .shadow(
                6.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                1.dp,
                if (isError.value) Color.Red else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                Color.White,
                shape = RoundedCornerShape(20.dp)
            )
    ){
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { newValue ->
                inputValue.value = newValue
                isError.value = false
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = inputText,
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                )
            },
            leadingIcon = {
                Icon(imageVector = leadingIcon,
                    contentDescription = "",
                    tint = Color.Black)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            )
        )
    }
    if(isError.value) {
        Text(
            text = errorText.value,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        )
    }
}

@Composable
fun customPasswordInput(
    inputValue: MutableState<String>,
    inputText: String,
    leadingIcon: ImageVector

){
    var isError = remember {
        mutableStateOf(false)
    }
    var showPassword = remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .shadow(
                6.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                1.dp,
                if (isError.value) Color.Red else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                Color.White,
                shape = RoundedCornerShape(20.dp)
            )
    ){
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { newValue ->
                inputValue.value = newValue
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = inputText,
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                )
            },
            leadingIcon = {
                Icon(imageVector = leadingIcon,
                    contentDescription = "",
                    tint = Color.Black)
            },
            trailingIcon = {
                IconButton(onClick = {
                    showPassword.value = !showPassword.value
                }) {
                    Icon(
                        imageVector = if(!showPassword.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "",
                        tint = Color.Black
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            visualTransformation = if(!showPassword.value) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }
    if(isError.value) {
        Text(
            text = "Postoji nalog sa unetom email adresom",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        )
    }
}

@Composable
fun loginRegisterCustomButton(
    buttonText: String,
    onClick: () -> Unit
){

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .height(50.dp)
            .shadow(6.dp, shape = RoundedCornerShape(20.dp)),
        colors = ButtonDefaults.buttonColors(mainColor),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = buttonText,
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        )
    }
}

@Composable
fun customClickableText(
    firstText: String,
    secondText: String,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = firstText,
            style = TextStyle(
                fontSize = 12.sp
            )
        )
        Text(
            text = secondText,
            modifier = Modifier
                .clickable {
                    onClick()
                }
                .padding(start = 4.dp),
            style = TextStyle(
                fontSize = 12.sp,
                color = mainColor
            )
        )
    }
}