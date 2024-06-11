package com.example.aquaspot

import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.aquaspot.Navigation.Router
import com.example.aquaspot.viewmodels.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AquaSpotApp: Application(){
    val db by lazy { Firebase.firestore }
}