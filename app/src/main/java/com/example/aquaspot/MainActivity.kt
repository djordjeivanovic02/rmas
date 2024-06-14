package com.example.aquaspot

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.example.aquaspot.model.service.DatabaseService
import com.example.aquaspot.model.service.StorageService
import com.example.aquaspot.viewmodels.AddNewUserViewModel
import com.example.aquaspot.viewmodels.AddNewUserViewModelFactory
import com.example.aquaspot.viewmodels.AuthViewModel
import com.example.aquaspot.viewmodels.AuthViewModelFactory

class MainActivity : ComponentActivity() {
    private val userViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(DatabaseService((application as AquaSpotApp).db))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AquaSpot(userViewModel)
        }
    }
}
