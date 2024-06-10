package com.example.aquaspot.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquaspot.R
import com.example.aquaspot.ui.theme.greyTextColor

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
fun customTextInput(){
    val inputValue = remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        value = inputValue.value,
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "example@gmail.com",
                style = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            )
        },
        leadingIcon = {
            Icon(imageVector = Icons.Filled.MailOutline , contentDescription = "Mail icon")
        }
    )
}