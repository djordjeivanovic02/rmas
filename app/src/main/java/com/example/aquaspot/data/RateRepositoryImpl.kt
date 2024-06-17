package com.example.aquaspot.data

import com.example.aquaspot.model.Beach
import com.example.aquaspot.model.Rate
import com.example.aquaspot.model.service.DatabaseService
import com.example.aquaspot.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RateRepositoryImpl : RateRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val databaseService = DatabaseService(firestoreInstance)
    override suspend fun getBeachRates(
        bid: String
    ): Resource<List<Rate>> {
        return try {
            val rateDocRef = firestoreInstance.collection("rates")
            val querySnapshot = rateDocRef.get().await()
            val ratesList = mutableListOf<Rate>()
            for (document in querySnapshot.documents) {
                val beachId = document.getString("beachId") ?: ""
                if (beachId == bid) {
                    ratesList.add(
                        Rate(
                            id = document.id,
                            userId = document.getString("userId") ?: "",
                            beachId = bid,
                            rate = document.getLong("rate")?.toInt() ?: 0
                        )
                    )
                }
            }
            Resource.Success(ratesList)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }


    override suspend fun getUserRates(): Resource<List<Rate>> {
        return try{
            val rateDocRef = firestoreInstance.collection("rates")
            val querySnapshot = rateDocRef.get().await()
            val ratesList = mutableListOf<Rate>()
            for(document in querySnapshot.documents){
                val userId = document.getString("userId") ?: ""
                if(userId == firebaseAuth.currentUser?.uid){
                    ratesList.add(Rate(
                        id = document.id,
                        beachId = document.getString("beachId") ?: "",
                        userId = userId,
                        rate = document.getLong("rate")?.toInt() ?: 0
                    ))
                }
            }
            Resource.Success(ratesList)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserAdForBeach(): Resource<List<Rate>> {
        TODO("Not yet implemented")
    }

    override suspend fun addRate(
        bid: String,
        rate: Int,
        beach: Beach
    ): Resource<String> {
        return try{
            val myRate = Rate(
                userId = firebaseAuth.currentUser!!.uid,
                beachId = bid,
                rate = rate
            )
            databaseService.addPoints(beach.userId, rate * 3)
            val result = databaseService.saveRateData(myRate)
            result
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun updateRate(rid: String, rate: Int): Resource<String> {
        return try{
            val result = databaseService.updateRate(rid, rate)
            result
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}