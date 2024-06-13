package com.example.aquaspot.data

import com.example.aquaspot.model.CustomUser
import com.example.aquaspot.viewmodels.AddNewUserViewModel

interface DatabaseRepository {
    suspend fun addUserDetails(uid: String, user: CustomUser): Resource<String>
}