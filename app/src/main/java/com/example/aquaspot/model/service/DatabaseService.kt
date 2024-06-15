package com.example.aquaspot.model.service

import com.example.aquaspot.data.Resource
import com.example.aquaspot.model.Beach
import com.example.aquaspot.model.CustomUser
import com.example.aquaspot.utils.await
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

class DatabaseService(
    private val firestore: FirebaseFirestore
) {
    suspend fun saveUserData(
        uid: String,
        user: CustomUser
    ): Resource<String>{
        return try {
            firestore.collection("users").document(uid).set(user).await()
            Resource.Success("Uspešno dodati podaci o korisniku")
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    suspend fun saveBeachData(
        beach: Beach
    ): Resource<String>{
        return try{
            firestore.collection("beaches").add(beach).await()
            Resource.Success("Uspešno sačuvani podaci o plaži")
        }catch(e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}