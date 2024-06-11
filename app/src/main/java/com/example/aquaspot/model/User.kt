package com.example.aquaspot.model

import com.google.firebase.firestore.DocumentId

data class CustomUser(
    @DocumentId val id: String = "",
    val username: String = "",
    val password: String = "",
    val fullName: String = "",
    val phoneNumber: String = ""
)