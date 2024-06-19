package com.example.aquaspot.data

import android.net.Uri
import com.example.aquaspot.model.CustomUser
import com.google.firebase.auth.FirebaseUser

interface AuthRepository{
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun register(fullName: String, phoneNumber: String, profileImage: Uri, email: String, password: String): Resource<FirebaseUser>
    suspend fun getUserData(): Resource<CustomUser>
    suspend fun getAllUserData(): Resource<List<CustomUser>>
    fun logout()
}