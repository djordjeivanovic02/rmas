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
import com.example.aquaspot.viewmodels.BeachViewModel
import com.example.aquaspot.viewmodels.BeachViewModelFactory

class MainActivity : ComponentActivity() {
    private val userViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory()
    }
    private val beachViewModel: BeachViewModel by viewModels{
        BeachViewModelFactory()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AquaSpot(userViewModel, beachViewModel)
//            AquaSpot(viewModel = userViewModel)
        }
    }
}
