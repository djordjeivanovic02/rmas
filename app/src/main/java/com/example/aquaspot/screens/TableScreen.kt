package com.example.aquaspot.screens

import android.graphics.fonts.FontStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aquaspot.Navigation.Routes
import com.example.aquaspot.model.Beach
import com.example.aquaspot.screens.components.CustomTable
import com.example.aquaspot.screens.components.headingText
import com.example.aquaspot.screens.components.mapFooter
import com.example.aquaspot.screens.components.mapNavigationBar
import com.example.aquaspot.ui.theme.lightMailColor
import kotlinx.coroutines.launch

@Composable
fun TableScreen(
    beaches: List<Beach>?,
    navController: NavController
){
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(
                    lightMailColor,
                    RoundedCornerShape(10.dp)
                )
                .height(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pregled svih plaža",
                    modifier = Modifier.fillMaxWidth(),
                    style= TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            CustomTable(beaches = beaches, navController = navController)
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
                    navController.navigate(Routes.indexScreen)
                },
                onTableClick = {
//                navController?.navigate(Routes.tableScreen)
                },
                onRankingClick = {},
                onSettingsClick = {}
            )
        }
    }
}