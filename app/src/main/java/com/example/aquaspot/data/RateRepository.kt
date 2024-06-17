package com.example.aquaspot.data

import com.example.aquaspot.model.Beach
import com.example.aquaspot.model.Rate

interface RateRepository {
    suspend fun getBeachRates(
        bid: String
    ): Resource<List<Rate>>
    suspend fun getUserRates(): Resource<List<Rate>>
    suspend fun getUserAdForBeach(): Resource<List<Rate>>
    suspend fun addRate(
        bid: String,
        rate: Int,
        beach: Beach
    ): Resource<String>

    suspend fun updateRate(
        rid: String,
        rate: Int,
    ): Resource<String>
}