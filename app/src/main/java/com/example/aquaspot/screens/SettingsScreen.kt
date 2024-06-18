package com.example.aquaspot.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.aquaspot.location.LocationService

@Composable
fun SettingScreen(
    navController: NavController
){
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
//    val isTrackingServiceEnabled = sharedPreferences.getBoolean("tracking_location", true)

    Column {
        Button(onClick = {
            Intent(context, LocationService::class.java).apply {
                action = LocationService.ACTION_STOP
                context.startService(this)
            }
            Intent(context, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                context.startService(this)
            }
            with(sharedPreferences.edit()) {
                putBoolean("tracking_location", false)
                apply()
            }
        }) {
            Text(text = "Disable Service")
        }

        Button(onClick = {
            Intent(context, LocationService::class.java).apply {
                action = LocationService.ACTION_FIND_NEARBY
                context.startService(this)
            }
            with(sharedPreferences.edit()) {
                putBoolean("tracking_location", true)
                apply()
            }
        }) {
            Text(text = "Enable Service")
        }
    }
}