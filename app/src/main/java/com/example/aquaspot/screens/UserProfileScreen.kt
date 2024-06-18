package com.example.aquaspot.screens

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aquaspot.Navigation.Routes
import com.example.aquaspot.location.LocationService
import com.example.aquaspot.viewmodels.AuthViewModel

@Composable
fun UserProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel
){
    val context = LocalContext.current
    Column {
        Button(onClick = {

            viewModel.logout()
            navController.navigate(Routes.loginScreen)
        }) {
            Text(text = "Odjavi se")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            Intent(context, LocationService::class.java).apply {
                action = LocationService.ACTION_STOP
                context.startService(this)
            }
        }) {
            Text(text = "Stop")
        }
    }
}