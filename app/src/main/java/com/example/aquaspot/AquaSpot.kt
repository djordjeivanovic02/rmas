package com.example.aquaspot

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.example.aquaspot.Navigation.Router
import com.example.aquaspot.location.LocationService
import com.example.aquaspot.viewmodels.AuthViewModel
import com.example.aquaspot.viewmodels.BeachViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AquaSpot(
    viewModel: AuthViewModel,
    beachViewModel: BeachViewModel
){
    val context = LocalContext.current
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            1
        )
    } else {
        Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_START
//            context.startService(this)
            context.startForegroundService(this)
        }

    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Router(viewModel, beachViewModel)
//        Router(viewModel = viewModel)
    }
}