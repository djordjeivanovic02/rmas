package com.example.aquaspot.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aquaspot.model.CustomUser
import com.example.aquaspot.model.service.StorageService
import com.example.aquaspot.viewmodels.AuthViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DatabaseRepositoryImpl : DatabaseRepository {
    private val dB: FirebaseFirestore = FirebaseFirestore.getInstance()
    override suspend fun addUserDetails(uid: String, user: CustomUser): Resource<String> {
        return try {
            dB.collection("users").document(uid).set(user).await()
            Resource.Success("Uspešno zapamćeni podaci o korisniku")
        }catch(e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}