package com.example.aquaspot.viewmodels

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aquaspot.data.BeachRepositoryImpl
import com.example.aquaspot.data.Resource
import com.example.aquaspot.model.Beach
import com.example.aquaspot.model.service.DatabaseService
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BeachViewModel: ViewModel() {
    val repository = BeachRepositoryImpl()

    private val _beachFlow = MutableStateFlow<Resource<String>?>(null)
    val beachFlow: StateFlow<Resource<String>?> = _beachFlow

    private val _beaches = MutableStateFlow<Resource<List<Beach>>>(Resource.Success(emptyList()))
    val beaches: StateFlow<Resource<List<Beach>>> get() = _beaches

    init {
        getAllBeaches()
    }

    fun getAllBeaches() = viewModelScope.launch {
        _beaches.value = repository.getAllBeaches()
    }

    fun saveBeachData(
        description: String,
        crowd: Int,
        mainImage: Uri,
        galleryImages: List<Uri>,
        location: MutableState<LatLng?>
    ) = viewModelScope.launch{
        _beachFlow.value = Resource.loading
        repository.saveBeachData(
            description = description,
            crowd = crowd,
            mainImage = mainImage,
            galleryImages = galleryImages,
            location = location.value!!
        )
        _beachFlow.value = Resource.Success("Uspešno dodata plaža")
    }
}

class BeachViewModelFactory:ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BeachViewModel::class.java)){
            return BeachViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}