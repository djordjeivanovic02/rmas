package com.example.aquaspot.model.service

import com.example.aquaspot.model.CustomUser
import com.example.aquaspot.utils.await
import com.google.firebase.firestore.FirebaseFirestore

class StorageService(
    private val firestore: FirebaseFirestore
) {

    suspend fun register(user: CustomUser): String{
        val updatedUser = user.copy()
        return firestore.collection("users").add(updatedUser).await().id
    }
}