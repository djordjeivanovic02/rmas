package com.example.aquaspot.data

import android.net.Uri
import com.example.aquaspot.model.Beach
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint

interface BeachRepository {

    suspend fun getAllBeaches(): Resource<List<Beach>>
    suspend fun saveBeachData(
        description: String,
        crowd: Int,
        mainImage: Uri,
        galleryImages: List<Uri>,
        location: LatLng
    ): Resource<String>
}