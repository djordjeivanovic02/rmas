package com.example.aquaspot.screens

import android.graphics.fonts.FontStyle
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aquaspot.Navigation.Routes
import com.example.aquaspot.R
import com.example.aquaspot.data.Resource
import com.example.aquaspot.model.Beach
import com.example.aquaspot.screens.components.CustomTable
import com.example.aquaspot.screens.components.headingText
import com.example.aquaspot.screens.components.mapFooter
import com.example.aquaspot.screens.components.mapNavigationBar
import com.example.aquaspot.ui.theme.lightMailColor
import com.example.aquaspot.viewmodels.BeachViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun TableScreen(
    beaches: List<Beach>?,
    navController: NavController,
    beachViewModel: BeachViewModel
){
    val newBeaches = remember {
        mutableListOf<Beach>()
    }
    if (beaches.isNullOrEmpty()){
        val beachesResource = beachViewModel.beaches.collectAsState()
        beachesResource.value.let {
            when(it){
                is Resource.Success -> {
                    Log.d("Podaci", it.toString())
                    newBeaches.clear()
                    newBeaches.addAll(it.result)
                }
                is Resource.loading -> {

                }
                is Resource.Failure -> {
                    Log.e("Podaci", it.toString())
                }
                null -> {}
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pregled plaža",
                    modifier = Modifier.fillMaxWidth(),
                    style= TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            if(beaches.isNullOrEmpty()){
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(300.dp),
                    contentAlignment = Alignment.Center
                ){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.not_found),
                            contentDescription = "",
                            modifier = Modifier.size(150.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = "Nije pronađena nijedna plaža")
                    }
                }
            }else {
                CustomTable(
                    beaches = beaches,
                    navController = navController
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            mapFooter(
                openAddNewBeach = {},
                active = 1,
                onHomeClick = {
                    val beachesJson = Gson().toJson(beaches)
                    val encodedBeachesJson = URLEncoder.encode(beachesJson, StandardCharsets.UTF_8.toString())
                    navController.navigate(Routes.indexScreenWithParams + "/$encodedBeachesJson")
                },
                onTableClick = {
                },
                onRankingClick = {
                    navController.navigate(Routes.rankingScreen)
                },
                onSettingsClick = {
                    navController.navigate(Routes.settingsScreen)
                }
            )
        }
    }
}