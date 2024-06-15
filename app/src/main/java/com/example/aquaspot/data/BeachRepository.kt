package com.example.aquaspot.data

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint

interface BeachRepository {
    suspend fun saveBeachData(
        description: String,
        crowd: Int,
        mainImage: Uri,
        galleryImages: List<Uri>,
        location: LatLng
    ): Resource<String>
}