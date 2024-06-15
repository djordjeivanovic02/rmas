package com.example.aquaspot

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.aquaspot.model.service.DatabaseService
import com.example.aquaspot.viewmodels.AuthViewModel
import com.example.aquaspot.viewmodels.AuthViewModelFactory

class MainActivity : ComponentActivity() {
    private val userViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(DatabaseService((application as AquaSpotApp).db))
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AquaSpot(userViewModel)
        }
    }
}
