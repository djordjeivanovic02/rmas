package com.example.aquaspot.data

import com.google.firebase.auth.FirebaseUser

interface AuthRepository{
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun register(name: String, email: String, password: String): Resource<FirebaseUser>
    fun logout()
}