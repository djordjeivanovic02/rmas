package com.example.aquaspot.exceptions

object AuthExceptionsMessages{
    val invalidCredential = "The supplied auth credential is incorrect, malformed or has expired."
    val emptyFields = "Given String is empty or null"
    val badlyEmailFormat = "The email address is badly formatted."
    val emailUsed = "The email address is already in use by another account."
    val shortPassword = "The given password is invalid. [ Password should be at least 6 characters ]"
}