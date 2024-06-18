package com.example.aquaspot.data

import android.net.Uri
import com.example.aquaspot.model.Beach
import com.example.aquaspot.model.service.DatabaseService
import com.example.aquaspot.model.service.StorageService
import com.example.aquaspot.utils.await
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage

class BeachRepositoryImpl : BeachRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val storageInstance = FirebaseStorage.getInstance()

    private val databaseService = DatabaseService(firestoreInstance)
    private val storageService = StorageService(storageInstance)


    override suspend fun getAllBeaches(): Resource<List<Beach>> {
        return try{
            val snapshot = firestoreInstance.collection("beaches").get().await()
            val beaches = snapshot.toObjects(Beach::class.java)
            Resource.Success(beaches)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun saveBeachData(
        description: String,
        crowd: Int,
        mainImage: Uri,
        galleryImages: List<Uri>,
        location: LatLng
    ): Resource<String> {
        return try{
            val currentUser = firebaseAuth.currentUser
            if(currentUser!=null){
                val mainImageUrl = storageService.uploadBeachMainImage(mainImage)
                val galleryImagesUrls = storageService.uploadBeachGalleryImages(galleryImages)
                val geoLocation = GeoPoint(
                    location.latitude,
                    location.longitude
                )
                val beach = Beach(
                    userId = currentUser.uid,
                    description = description,
                    crowd = crowd,
                    mainImage = mainImageUrl,
                    galleryImages = galleryImagesUrls,
                    location = geoLocation
                )
                databaseService.saveBeachData(beach)
                databaseService.addPoints(currentUser.uid, 5)
            }
            Resource.Success("Uspesno sačuvani svi podaci o plaži")
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserBeaches(uid: String): Resource<List<Beach>> {
        return try {
            val snapshot = firestoreInstance.collection("beaches")
                .whereEqualTo("userId", uid)
                .get()
                .await()
            val beaches = snapshot.toObjects(Beach::class.java)
            Resource.Success(beaches)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}