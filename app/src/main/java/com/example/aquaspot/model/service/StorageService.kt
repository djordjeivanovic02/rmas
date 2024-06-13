package com.example.aquaspot.model.service

import android.net.Uri
import com.example.aquaspot.data.Resource
import com.example.aquaspot.utils.await
import com.google.firebase.storage.FirebaseStorage

class StorageService(
    private val storage: FirebaseStorage
){
    suspend fun uploadProfilePicture(uid: String, image: Uri): String{
        return try{
            val storageRef = storage.reference.child("profile_picture/$uid.jpg")
            val uploadTask = storageRef.putFile(image).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            downloadUrl.toString()
        }catch (e: Exception){
            e.printStackTrace()
            ""
        }
    }
}