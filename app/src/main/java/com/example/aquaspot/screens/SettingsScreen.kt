package com.example.aquaspot.screens

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aquaspot.Navigation.Routes
import com.example.aquaspot.location.LocationService
import com.example.aquaspot.model.Beach
import com.example.aquaspot.screens.components.CustomTable
import com.example.aquaspot.screens.components.mapFooter
import com.example.aquaspot.ui.theme.greyTextColor
import com.example.aquaspot.ui.theme.lightGreyColor
import com.example.aquaspot.ui.theme.lightMailColor
import com.example.aquaspot.ui.theme.lightMainColor2
import com.example.aquaspot.ui.theme.mainColor
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingScreen(
    navController: NavController
){
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    val isTrackingServiceEnabled = sharedPreferences.getBoolean("tracking_location", true)

    val checked = remember {
        mutableStateOf(isTrackingServiceEnabled)
    }
    val beachMarkers = remember {
        mutableStateListOf<Beach>()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(
                    lightMailColor,
                    RoundedCornerShape(bottomEnd = 50.dp, bottomStart = 50.dp)
                )
                .height(200.dp),
            ) {
                Text(
                    text = "AquaSpot Podešavanja",
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (60).dp) ,
                    style= TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
//            CustomTable(beaches = beaches, navController = navController)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(120.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    lightGreyColor,
                    RoundedCornerShape(10.dp)
                )
                .clip(RoundedCornerShape(10.dp))
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 20.dp, horizontal = 16.dp)
                ){
                    Text(
                        text = "SERVISI",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray,
                            fontSize = 18.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.White,
                                RoundedCornerShape(5.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Najbliža plaža",
                            style = TextStyle(
                                fontSize = 16.sp
                            )
                        )
                        Switch(
                            checked = isTrackingServiceEnabled,
                            onCheckedChange = {
                                checked.value = it
                                if (it){
                                    Intent(context, LocationService::class.java).apply {
                                        action = LocationService.ACTION_FIND_NEARBY
                                        context.startForegroundService(this)
                                    }
                                    with(sharedPreferences.edit()) {
                                        putBoolean("tracking_location", true)
                                        apply()
                                    }
                                }else{
                                    Intent(context, LocationService::class.java).apply {
                                        action = LocationService.ACTION_STOP
                                        context.stopService(this)
                                    }
                                    Intent(context, LocationService::class.java).apply {
                                        action = LocationService.ACTION_START
                                        context.startForegroundService(this)
                                    }
                                    with(sharedPreferences.edit()) {
                                        putBoolean("tracking_location", false)
                                        apply()
                                    }
                                }
                            },
                            thumbContent = if (checked.value) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                    )
                                }
                            } else {
                                null
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = mainColor,
                                checkedTrackColor = lightMainColor2,
                                uncheckedThumbColor = greyTextColor,
                                uncheckedTrackColor = Color.White,
                            )


                        )

                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            mapFooter(
                openAddNewBeach = {},
                active = 3,
                onHomeClick = {
                    navController.navigate(Routes.indexScreen)
                },
                onTableClick = {
//                    navController.navigate(Routes.tableScreen)
                    val beachesJson = Gson().toJson(beachMarkers)
                    val encodedBeachesJson = URLEncoder.encode(beachesJson, StandardCharsets.UTF_8.toString())
                    navController.navigate("tableScreen/$encodedBeachesJson")
                },
                onRankingClick = {},
                onSettingsClick = {}
            )
        }
    }
}
