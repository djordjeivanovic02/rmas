package com.example.aquaspot.screens

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.SatelliteAlt
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Popup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.drawToBitmap
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.aquaspot.Navigation.Routes
import com.example.aquaspot.R
import com.example.aquaspot.data.Resource
import com.example.aquaspot.location.LocationService
import com.example.aquaspot.model.Beach
import com.example.aquaspot.model.CustomUser
import com.example.aquaspot.screens.beachs.AddNewBeachBottomSheet
import com.example.aquaspot.screens.beachs.FiltersBottomSheet
import com.example.aquaspot.screens.components.bitmapDescriptorFromUrlWithRoundedCorners
import com.example.aquaspot.screens.components.bitmapDescriptorFromVector
import com.example.aquaspot.screens.components.mapFooter
import com.example.aquaspot.screens.components.mapNavigationBar
import com.example.aquaspot.ui.theme.greyTextColor
import com.example.aquaspot.ui.theme.mainColor
import com.example.aquaspot.viewmodels.AuthViewModel
import com.example.aquaspot.viewmodels.BeachViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.ktx.model.cameraPosition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IndexScreen(
    viewModel: AuthViewModel?,
    navController: NavController?,
    beachViewModel: BeachViewModel?,

    isCameraSet: MutableState<Boolean> = remember {
        mutableStateOf(false)
    },
    cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(43.321445, 21.896104), 17f)
    },
    beachMarkers: MutableList<Beach>,
    isFilteredParam: Boolean = false
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("filters", Context.MODE_PRIVATE)
    val options = sharedPreferences.getString("options", null)
    val crowd = sharedPreferences.getString("crowd", null)
    val range = sharedPreferences.getFloat("range", 1000f)

    val isFiltered = remember {
        mutableStateOf(false)
    }
    val isFilteredIndicator = remember{
        mutableStateOf(false)
    }

    if(isFilteredParam && (options != null || crowd != null || range != 1000f)){
        isFilteredIndicator.value = true
    }

    val beachesResource = beachViewModel?.beaches?.collectAsState()
    val allBeaches = remember {
        mutableListOf<Beach>()
    }
    beachesResource?.value.let {
        when(it){
            is Resource.Success -> {
                allBeaches.clear()
                allBeaches.addAll(it.result)
            }
            is Resource.loading -> {

            }
            is Resource.Failure -> {
                Log.e("Podaci", it.toString())
            }
            null -> {}
        }
    }

    viewModel?.getUserData()

    val userDataResource = viewModel?.currentUserFlow?.collectAsState()

    val filteredBeaches = remember {
        mutableListOf<Beach>()
    }

    val searchValue = remember {
        mutableStateOf("")
    }
    val userData = remember {
        mutableStateOf<CustomUser?>(null)
    }
    val profileImage = remember {
        mutableStateOf("")
    }

    val myLocation = remember {
        mutableStateOf<LatLng?>(null)
    }

    val beachMarkerCopy = beachMarkers

    val showFilterDialog = remember {
        mutableStateOf(false)
    }

    val isAddNewBottomSheet = remember {
        mutableStateOf(true)
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

    val uiSettings = remember { mutableStateOf(MapUiSettings()) }

    val properties = remember {
        mutableStateOf(MapProperties(mapType = MapType.TERRAIN))
    }

    val markers = remember { mutableStateListOf<LatLng>() }
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)


    LaunchedEffect(myLocation.value) {
        myLocation.value?.let {
            Log.d("Nova lokacija gore", myLocation.toString())
            if(!isCameraSet.value) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 17f)
                isCameraSet.value = true
            }
            markers.clear()
            markers.add(it)
        }
    }

    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            if(isAddNewBottomSheet.value)
                AddNewBeachBottomSheet(beachViewModel!!, myLocation, sheetState)
            else
                FiltersBottomSheet(beachViewModel!!, viewModel!!, allBeaches, sheetState, isFiltered,isFilteredIndicator, filteredBeaches, beachMarkers, myLocation.value)
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
                        snippet = "",
                    )
                }
                Log.d("Is Filtered", isFiltered.value.toString())
                if(!isFiltered.value) {
                    beachMarkers.forEach { marker ->
                        val icon = bitmapDescriptorFromUrlWithRoundedCorners(
                            context,
                            marker.mainImage,
                            10f,
                        )
                        Marker(
                            state = rememberMarkerState(
                                position = LatLng(
                                    marker.location.latitude,
                                    marker.location.longitude
                                )
                            ),
                            title = "Moja Lokacija",
                            icon = icon.value ?: BitmapDescriptorFactory.defaultMarker(),
                            snippet = marker.description,
                            onClick = {
                                val beachJson = Gson().toJson(marker)
                                val encodedBeachJson =
                                    URLEncoder.encode(beachJson, StandardCharsets.UTF_8.toString())

                                val beachesJson = Gson().toJson(beachMarkers)
                                val encodedBeachesJson = URLEncoder.encode(beachesJson, StandardCharsets.UTF_8.toString())

                                navController?.navigate(Routes.beachScreen + "/$encodedBeachJson/$encodedBeachesJson")
                                true
                            }
                        )
                    }
                }else{
                    Log.d("Filtered", filteredBeaches.count().toString())
                    filteredBeaches.forEach { marker ->
                        val icon = bitmapDescriptorFromUrlWithRoundedCorners(
                            context,
                            marker.mainImage,
                            10f,
                        )
                        Marker(
                            state = rememberMarkerState(
                                position = LatLng(
                                    marker.location.latitude,
                                    marker.location.longitude
                                )
                            ),
                            title = "Moja Lokacija",
                            icon = icon.value ?: BitmapDescriptorFactory.defaultMarker(),
                            snippet = marker.description,
                            onClick = {
                                val beachJson = Gson().toJson(marker)
                                val encodedBeachJson =
                                    URLEncoder.encode(beachJson, StandardCharsets.UTF_8.toString())

                                val beachesJson = Gson().toJson(filteredBeaches)
                                val encodedBeachesJson = URLEncoder.encode(beachesJson, StandardCharsets.UTF_8.toString())

                                navController?.navigate(Routes.beachScreen + "/$encodedBeachJson/$encodedBeachesJson")
                                true
                            }
                        )
                    }
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
                    profileImage = profileImage.value.ifEmpty { "" },
                    onImageClick = {

                        val userJson = Gson().toJson(userData.value)
                        val encodedUserJson = URLEncoder.encode(userJson, StandardCharsets.UTF_8.toString())
                        navController?.navigate(Routes.userProfileScreen + "/$encodedUserJson")

                    },
                    beaches = beachMarkerCopy,
                    navController = navController,
                    cameraPositionState = cameraPositionState
                )
                Spacer(modifier = Modifier.height(5.dp))
                Box(
                    modifier = Modifier
                        .clickable  {
                            isAddNewBottomSheet.value = false
                            scope.launch {
                                sheetState.show()
                            }
                        }
                        .background(
                            if(isFiltered.value || isFilteredIndicator.value)
                                mainColor
                            else
                                Color.White
                            ,RoundedCornerShape(30.dp)
                        )
                        .padding(horizontal = 15.dp, vertical = 7.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FilterAlt,
                            contentDescription = "",
                            tint =
                                if(isFiltered.value || isFilteredIndicator.value)
                                    Color.White
                                else
                                    mainColor
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Filteri",
                            style = TextStyle(
                                color = if(isFiltered.value || isFilteredIndicator.value)
                                    Color.White
                                else
                                    mainColor
                            )
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(
                        onClick = {
                            properties.value = MapProperties(mapType = MapType.TERRAIN)
                        },
                        modifier = Modifier
                            .background(
                                if(properties.value == MapProperties(mapType = MapType.TERRAIN))
                                    mainColor
                                else
                                    Color.White
                                ,RoundedCornerShape(30.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SatelliteAlt,
                            contentDescription = "",
                            tint =
                            if(properties.value == MapProperties(mapType = MapType.TERRAIN))
                                Color.White
                            else
                                greyTextColor
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    IconButton(
                        onClick = {
                            properties.value = MapProperties(mapType = MapType.SATELLITE)
                        },
                        modifier = Modifier
                            .background(
                                if(properties.value == MapProperties(mapType = MapType.SATELLITE))
                                    mainColor
                                else
                                    Color.White
                                ,RoundedCornerShape(30.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Terrain,
                            contentDescription = "",
                            tint =
                            if(properties.value == MapProperties(mapType = MapType.SATELLITE))
                                Color.White
                            else
                                greyTextColor
                        )
                    }
                }
                mapFooter(
                    openAddNewBeach = {
                        isAddNewBottomSheet.value = true
                        scope.launch {
                            sheetState.show()
                        }
                    },
                    active = 0,
                    onHomeClick = {},
                    onTableClick = {
//                        navController?.navigate(Routes.tableScreen)
                        val beachesJson = Gson().toJson(
                            if(!isFiltered.value)
                                beachMarkers
                            else
                                filteredBeaches
                        )
                        val encodedBeachesJson = URLEncoder.encode(beachesJson, StandardCharsets.UTF_8.toString())
                        navController?.navigate("tableScreen/$encodedBeachesJson")
                    },
                    onRankingClick = {
                        navController?.navigate(Routes.rankingScreen)
                    },
                    onSettingsClick = {
                        navController?.navigate(Routes.settingsScreen)
                    }
                )
            }
        }
    }
    
    if (showFilterDialog.value){
        FilterDialog(onApply = { /*TODO*/ }) {
            
        }
    }

   userDataResource?.value.let {
       when(it){
           is Resource.Success -> {
               userData.value = it.result
               profileImage.value = it.result.profileImage
           }
           null -> {
               userData.value = null
               profileImage.value = ""
           }

           is Resource.Failure -> {}
           Resource.loading -> {}
       }
   }


}

@Composable
fun FilterDialog(
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    // Ovdje definirajte izgled dijaloga s filterima
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Filteri") },
        confirmButton = {
            Button(
                onClick = onApply,
            ) {
                Text(text = "Primijeni")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
            ) {
                Text(text = "Odustani")
            }
        },
        // Dodajte elemente za filtriranje ovdje
    )
}