package com.example.aquaspot.screens

import android.text.Layout.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.aquaspot.Navigation.Routes
import com.example.aquaspot.viewmodels.AuthViewModel
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun IndexScreen(
    viewModel: AuthViewModel?,
    navController: NavController
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
//            viewModel?.logout()
            navController.navigate(Routes.loginScreen)
        }) {
            Text(text = "Odjavi se")
        }
    }
}