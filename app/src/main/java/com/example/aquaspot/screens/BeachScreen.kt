package com.example.aquaspot.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aquaspot.Navigation.Routes
import com.example.aquaspot.data.Resource
import com.example.aquaspot.model.Beach
import com.example.aquaspot.model.Rate
import com.example.aquaspot.screens.components.BeachMainImage
import com.example.aquaspot.screens.components.CustomBackButton
import com.example.aquaspot.screens.components.CustomBeachGallery
import com.example.aquaspot.screens.components.CustomBeachLocation
import com.example.aquaspot.screens.components.CustomBeachRate
import com.example.aquaspot.screens.components.CustomCrowdIndicator
import com.example.aquaspot.screens.components.CustomRateButton
import com.example.aquaspot.screens.components.greyText
import com.example.aquaspot.screens.components.greyTextBigger
import com.example.aquaspot.screens.components.headingText
import com.example.aquaspot.screens.dialogs.RateBeachDialog
import com.example.aquaspot.viewmodels.AuthViewModel
import com.example.aquaspot.viewmodels.BeachViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import java.math.RoundingMode
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun BeachScreen(
    navController: NavController,
    beachViewModel: BeachViewModel,
    beach: Beach,
    viewModel: AuthViewModel,
    beaches: MutableList<Beach>?
){
    val ratesResources = beachViewModel.rates.collectAsState()
    val newRateResource = beachViewModel.newRate.collectAsState()

    val rates = remember {
        mutableListOf<Rate>()
    }
    val averageRate = remember {
        mutableStateOf(0.0)
    }
    val showRateDialog = remember {
        mutableStateOf(false)
    }

    val isLoading = remember {
        mutableStateOf(false)
    }

    val myPrice = remember {
        mutableStateOf(0)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BeachMainImage(beach.mainImage)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            item{ CustomBackButton {
                if(beaches == null) {
                    navController.popBackStack()
                }else{
                    val isCameraSet = true
                    val latitude = beach.location.latitude
                    val longitude = beach.location.longitude

                    val beachesJson = Gson().toJson(beaches)
                    val encodedBeachesJson = URLEncoder.encode(beachesJson, StandardCharsets.UTF_8.toString())
                    navController.navigate(Routes.indexScreenWithParams + "/$isCameraSet/$latitude/$longitude/$encodedBeachesJson")
                }
            }}
            item{Spacer(modifier = Modifier.height(220.dp))}
            item{ CustomCrowdIndicator(crowd = beach.crowd)}
            item{Spacer(modifier = Modifier.height(20.dp))}
            item{headingText(textValue = "Plaža u blizini")}
            item{Spacer(modifier = Modifier.height(10.dp))}
            item{CustomBeachLocation(location = LatLng(beach.location.latitude, beach.location.longitude))}
            item{Spacer(modifier = Modifier.height(10.dp))}
            item{CustomBeachRate(average = averageRate.value)}
            item{Spacer(modifier = Modifier.height(10.dp))}
            item{greyTextBigger(textValue = beach.description.replace('+', ' '))}
            item{Spacer(modifier = Modifier.height(20.dp))}
            item{Text(text = "Galerija plaže", style= TextStyle(fontSize = 20.sp))};
//            item{ CustomCrowdIndicator(crowd = 1)}
            item{Spacer(modifier = Modifier.height(10.dp))}
            item { CustomBeachGallery(images = beach.galleryImages)}
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 15.dp, vertical = 20.dp)
        ) {
            CustomRateButton(
                enabled = if(beach.userId == viewModel.currentUser?.uid) false else true,
                onClick = {
                val rateExist = rates.firstOrNull{
                    it.beachId == beach.id && it.userId == viewModel.currentUser!!.uid
                }
                if(rateExist != null)
                    myPrice.value = rateExist.rate
                showRateDialog.value = true
            })
        }


        if(showRateDialog.value){
            RateBeachDialog(
                showRateDialog = showRateDialog,
                rate = myPrice,
                rateBeach = {

                    val rateExist = rates.firstOrNull{
                        it.beachId == beach.id && it.userId == viewModel.currentUser!!.uid
                    }
                    if(rateExist != null){
                        isLoading.value = true
                        beachViewModel.updateRate(
                            rid = rateExist.id,
                            rate = myPrice.value
                        )
                    }else {
                        isLoading.value = true
                        beachViewModel.addRate(
                            bid = beach.id,
                            rate = myPrice.value,
                            beach = beach
                        )
                    }
                },
                isLoading = isLoading
            )
        }
    }

    ratesResources.value.let {
        when(it){
            is Resource.Success -> {
                rates.addAll(it.result)
                var sum = 0.0
                for (rate in it.result){
                    sum += rate.rate.toDouble()
                }
                if(sum != 0.0) {
                    val rawPositive = sum / it.result.count()
                    val rounded = rawPositive.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
                    averageRate.value = rounded
                }  else {}
            }
            is Resource.loading -> {

            }
            is Resource.Failure -> {
                Log.e("Podaci", it.toString())
            }
        }
    }
    newRateResource.value.let {
        when(it){
            is Resource.Success -> {
                isLoading.value = false

                val rateExist = rates.firstOrNull{rate ->
                    rate.id == it.result
                }
                if(rateExist != null){
                    rateExist.rate = myPrice.value
                }
            }
            is Resource.loading -> {
//                isLoading.value = false
            }
            is Resource.Failure -> {
                val context = LocalContext.current
                Toast.makeText(context, "Došlo je do greške prilikom ocenjivanja plaže", Toast.LENGTH_LONG).show()
                isLoading.value = false
            }
            null -> {
                isLoading.value = false
            }
        }
    }
}