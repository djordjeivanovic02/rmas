package com.example.aquaspot

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.aquaspot.Navigation.Router
import com.example.aquaspot.viewmodels.AddNewUserViewModel
import com.example.aquaspot.viewmodels.AuthViewModel

@Composable
fun AquaSpot(
    viewModel: AuthViewModel
){
    Surface(modifier = Modifier.fillMaxSize()) {
        Router(viewModel)
    }
}