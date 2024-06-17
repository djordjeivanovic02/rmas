package com.example.aquaspot.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aquaspot.model.Beach
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
import com.google.android.gms.maps.model.LatLng

@Composable
fun BeachScreen(
    navController: NavController,
    beach: Beach
){
    Box(modifier = Modifier.fillMaxSize()) {
        BeachMainImage(beach.mainImage)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            item{ CustomBackButton {
                navController.popBackStack()
            }}
            item{Spacer(modifier = Modifier.height(220.dp))}
            item{ CustomCrowdIndicator(crowd = beach.crowd)}
            item{Spacer(modifier = Modifier.height(20.dp))}
            item{headingText(textValue = "Plaža u blizini")}
            item{Spacer(modifier = Modifier.height(10.dp))}
            item{CustomBeachLocation(location = LatLng(beach.location.latitude, beach.location.longitude))}
            item{Spacer(modifier = Modifier.height(10.dp))}
            item{CustomBeachRate(average = 4.3)}
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
            CustomRateButton {}
        }
    }
}