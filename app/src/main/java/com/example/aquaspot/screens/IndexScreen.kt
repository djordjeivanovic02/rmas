package com.example.aquaspot.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.PointF
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Popup
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import com.example.aquaspot.Navigation.Routes
import com.example.aquaspot.R
import com.example.aquaspot.location.LocationService
import com.example.aquaspot.screens.beachs.AddNewBeachBottomSheet
import com.example.aquaspot.screens.components.mapFooter
import com.example.aquaspot.screens.components.mapNavigationBar
import com.example.aquaspot.viewmodels.AuthViewModel
import com.example.aquaspot.viewmodels.BeachViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IndexScreen(
    viewModel: AuthViewModel?,
    navController: NavController?,
    beachViewModel: BeachViewModel?
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
    val cameraSet = remember {
        mutableStateOf(false)
    }
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
            Log.d("Nova lokacija gore", myLocation.toString())
            if(!cameraSet.value) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 100f)
                cameraSet.value = true
            }
            markers.clear()
            markers.add(it)
        }
    }
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            AddNewBeachBottomSheet(beachViewModel!!, myLocation)
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = properties.value,
                uiSettings = uiSettings.value
            ) {
                markers.forEach { marker ->
                    val icon = bitmapDescriptorFromVector(
                        context, R.drawable.currentlocation
                    )
                    Marker(
                        state = rememberMarkerState(position = marker),
                        title = "Moja Lokacija",
                        icon = icon,
                        snippet = "Marker in Singapore",
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
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
                mapFooter(
                    openAddNewBeach = {
                        scope.launch {
                            sheetState.show()
                        }
                    }
                )
            }
        }
    }

}



fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun showIndex(){
    IndexScreen(null, null, null)
}


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