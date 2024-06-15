package com.example.aquaspot.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PointF
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import com.example.aquaspot.Navigation.Routes
import com.example.aquaspot.location.LocationService
import com.example.aquaspot.screens.components.mapFooter
import com.example.aquaspot.screens.components.mapNavigationBar
import com.example.aquaspot.viewmodels.AuthViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IndexScreen(
    viewModel: AuthViewModel?,
    navController: NavController?
) {
    //PODACI
    val searchValue = remember {
        mutableStateOf("")
    }



    val context = LocalContext.current
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
        position = CameraPosition.fromLatLngZoom(nis, 100f)
    }
    val uiSettings = remember { mutableStateOf(MapUiSettings()) }
    val properties = remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    val markers = remember { mutableStateListOf<LatLng>() }

    LaunchedEffect(myLocation.value) {
        myLocation.value?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 100f)
            markers.clear()
            markers.add(it)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = properties.value,
            uiSettings = uiSettings.value
        ) {
            markers.forEach { marker ->
                Log.d("Markeri", marker.toString())
                Marker(
                    state = rememberMarkerState(position = marker),
                    title = "Moja Lokacija",
                    icon = remember { BitmapDescriptorFactory.defaultMarker() },
                    snippet = "Marker in Singapore",
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
//                .background(Color.White.copy(alpha = 0.7f))
                .padding(16.dp)
        ) {
            mapNavigationBar(
                searchValue = searchValue,
                profileImage = "https://firebasestorage.googleapis.com/v0/b/rmas-6f7af.appspot.com/o/profile_picture%2FydTlwLyHDgVBycpis5cHdvyMYHE3.jpg?alt=media&token=c12596db-8faa-489b-952d-9e74d4fc7f21"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            mapFooter()
//            Button(onClick = {
//                Intent(context, LocationService::class.java).apply {
//                    action = LocationService.ACTION_STOP
//                    context.startService(this)
//                }
//                viewModel?.logout()
//                navController!!.navigate(Routes.loginScreen)
//            }) {
//                Text(text = "Odjavi se")
//            }
//            Spacer(modifier = Modifier.height(20.dp))
//            Button(onClick = {
//                Intent(context, LocationService::class.java).apply {
//                    action = LocationService.ACTION_STOP
//                    context.startService(this)
//                }
//            }) {
//                Text(text = "Stop")
//            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun showIndex(){
    IndexScreen(null, null)
}