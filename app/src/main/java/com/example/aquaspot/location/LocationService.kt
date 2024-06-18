package com.example.aquaspot.location

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.aquaspot.MainActivity
import com.example.aquaspot.R
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class LocationService : Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private val notifiedBeaches = mutableSetOf<String>()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        locationClient = LocationClientImpl(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            ACTION_START -> {
                Log.d("LocationService", "Service started")
                val notification = createNotification()
                startForeground(NOTIFICATION_ID, notification)
                start()
            }
            ACTION_STOP -> {
                Log.d("LocationService", "Service stopped")
                stop()
            }
            ACTION_FIND_NEARBY -> {
                Log.d("NearbyService", "Service started")
                val notification = createNotification()
                startForeground(NOTIFICATION_ID, notification)
                start(nearby = true)
            }
        }
        return START_NOT_STICKY
    }

    private fun start(
        nearby: Boolean = false
    ) {
        locationClient.getLocationUpdates(1000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                Log.d("Lokacija", "${location.latitude} ${location.longitude}")
                val intent = Intent(ACTION_LOCATION_UPDATE).apply {
                    putExtra(EXTRA_LOCATION_LATITUDE, location.latitude)
                    putExtra(EXTRA_LOCATION_LONGITUDE, location.longitude)
                }
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                if(nearby){
                    checkProximityToBeaches(location.latitude, location.longitude)
                }
            }.launchIn(serviceScope)
    }

    private fun stop(){
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private fun createNotificationChannel() {
        val notificationChannelId = "LOCATION_SERVICE_CHANNEL"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Lokacija",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Obaveštavamo vas da se vaša lokacija prati u pozadini kako bi se proverilo da li ste u blizini neke plaže"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): android.app.Notification {
        val notificationChannelId = "LOCATION_SERVICE_CHANNEL"

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Praćenje lokacije")
            .setContentText("Servis praćenja lokacije je pokrenut u pozadini")
            .setSmallIcon(R.drawable.logo)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }
    private fun checkProximityToBeaches(latitude: Double, longitude: Double) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("beaches").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val geoPoint = document.getGeoPoint("location")
                    geoPoint?.let {
                        val distance = calculateDistance(latitude, longitude, it.latitude, it.longitude)
                        if (distance <= 100 && !notifiedBeaches.contains(document.id)) {
                            sendNearbyBeachNotification()
                            notifiedBeaches.add(document.id)
                            Log.d("U blizini", document.toString())
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("LocationService", "Error fetching beaches", e)
            }
    }
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371000.0

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

    private fun sendNearbyBeachNotification() {
        val notificationChannelId = "LOCATION_SERVICE_CHANNEL"

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Plaza u blizini")
            .setContentText("Nalazite se u blizini neke plaže!")
            .setSmallIcon(R.drawable.logo)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NEARBY_BEACH_NOTIFICATION_ID, notification)
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_FIND_NEARBY = "ACTION_FIND_NEARBY"
        const val ACTION_LOCATION_UPDATE = "ACTION_LOCATION_UPDATE"
        const val EXTRA_LOCATION_LATITUDE = "EXTRA_LOCATION_LATITUDE"
        const val EXTRA_LOCATION_LONGITUDE = "EXTRA_LOCATION_LONGITUDE"
        private const val NOTIFICATION_ID = 1
        private const val NEARBY_BEACH_NOTIFICATION_ID = 2
    }
}

