package com.example.aquaspot.screens

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import com.example.aquaspot.Navigation.Routes
import com.example.aquaspot.R
import com.example.aquaspot.location.LocationService
import com.example.aquaspot.screens.components.MapScreen
import com.example.aquaspot.viewmodels.AuthViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IndexScreen(
    viewModel: AuthViewModel?,
    navController: NavController
) {
    val context = LocalContext.current

    // MutableState to hold the current camera position
    val myLocation = remember {
        mutableStateOf<LatLng?>(null)
    }
    val receiver = remember {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == LocationService.ACTION_LOCATION_UPDATE) {
                    val latitude =
                        intent.getDoubleExtra(LocationService.EXTRA_LOCATION_LATITUDE, 0.0)
                    val longitude =
                        intent.getDoubleExtra(LocationService.EXTRA_LOCATION_LONGITUDE, 0.0)
                    // Update the camera position
                    myLocation.value = LatLng(latitude, longitude)
                    Log.d("Nova lokacija", myLocation.toString())
                }
            }
        }
    }
    DisposableEffect(context) {
        LocalBroadcastManager.getInstance(context)
            .registerReceiver(receiver, IntentFilter(LocationService.ACTION_LOCATION_UPDATE))
        onDispose {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
        }
    }
    val nis = LatLng(43.321445, 21.896104)
    val cameraPositionState = rememberCameraPositionState {
        if(myLocation.value == null) {
            position = CameraPosition.fromLatLngZoom(nis, 100f)
        }else{
            position = CameraPosition.fromLatLngZoom(myLocation.value!!, 100f)
        }
    }
    var uiSettings = remember { mutableStateOf(MapUiSettings()) }
    var properties = remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }

    Column {
        Text(text = "Your Current Location:")
        Text(text = "Latitude: ${myLocation.value?.latitude}")
        Text(text = "Longitude: ${myLocation.value?.longitude}")

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = properties.value,
            uiSettings = uiSettings.value
        )
    }
}



//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center
//    ) {
//        Button(onClick = {
//            viewModel?.logout()
//            navController.navigate(Routes.loginScreen)
//        }) {
//            Text(text = "Odjavi se")
//        }
//        Spacer(modifier = Modifier.height(20.dp))
//        Button(onClick = {
//            Intent(context, LocationService::class.java).apply {
//                action = LocationService.ACTION_STOP
//                context.startService(this)
//            }
//        }) {
//            Text(text = "Stop")
//        }
//        MapView2(myLocation)
//    }
//}


@Composable
fun MapView2(myLocation: MutableState<LatLng>) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var map = remember { mutableStateOf<GoogleMap?>(null) }
    var marker = remember { mutableStateOf<Marker?>(null) }

    AndroidView({ context ->
        MapView(context).apply {
            onCreate(Bundle())
            getMapAsync { googleMap ->
                map.value = googleMap
                marker.value = googleMap.addMarker(MarkerOptions().position(myLocation.value))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation.value, 15f))
            }
        }
    }, modifier = Modifier.fillMaxSize()) { mapView ->
        mapView.onResume()
    }

    LaunchedEffect(myLocation.value) {
        marker.value!!.position = myLocation.value
        map.value!!.animateCamera(CameraUpdateFactory.newLatLng(myLocation.value))
    }
}
