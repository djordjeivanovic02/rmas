package com.example.aquaspot

import android.Manifest
import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.aquaspot.Navigation.Router
import com.example.aquaspot.location.LocationService
import com.example.aquaspot.viewmodels.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AquaSpotApp : Application() {
    val db by lazy { Firebase.firestore }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

//        val notificationChannel = NotificationChannel(
//            "notification_channel_id",
//            "Notification name",
//            NotificationManager.IMPORTANCE_HIGH
//        )
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(notificationChannel)
    }
}
