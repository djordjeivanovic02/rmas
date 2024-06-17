package com.example.aquaspot.data

import android.net.Uri
import com.example.aquaspot.model.CustomUser
import com.example.aquaspot.model.service.DatabaseService
import com.example.aquaspot.model.service.StorageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class AuthRepositoryImp : AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val storageInstance = FirebaseStorage.getInstance()

    private val databaseService = DatabaseService(firestoreInstance)
    private val storageService = StorageService(storageInstance)
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        }catch(e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun register(
        fullName: String,
        phoneNumber: String,
        profileImage: Uri,
        email: String,
        password: String
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            if(result.user != null){
                val profilePictureUrl = storageService.uploadProfilePicture(result.user!!.uid, profileImage)
                val user = CustomUser(
                    fullName = fullName,
                    phoneNumber = phoneNumber,
                    profileImage = profilePictureUrl
                )
                databaseService.saveUserData(result.user!!.uid, user)
            }
            Resource.Success(result.user!!)
        }catch(e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserData(): Resource<CustomUser> {
        return try {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                val uid = currentUser.uid

                val db = FirebaseFirestore.getInstance()

                val userDocRef = db.collection("users").document(uid)
                val userSnapshot = userDocRef.get().await()

                if (userSnapshot.exists()) {
                    val customUser = userSnapshot.toObject(CustomUser::class.java)
                    if (customUser != null) {
                        Resource.Success(customUser)
                    } else {
                        Resource.Failure(Exception("Neuspešno mapiranje dokumenta na CustomUser"))
                    }
                } else {
                    Resource.Failure(Exception("Korisnikov dokument ne postoji"))
                }
            } else {
                Resource.Failure(Exception("Nema trenutnog korisnika"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}