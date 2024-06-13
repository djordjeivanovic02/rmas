package com.example.aquaspot.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aquaspot.data.AuthRepository
import com.example.aquaspot.data.AuthRepositoryImp
import com.example.aquaspot.data.Resource
import com.example.aquaspot.model.CustomUser
import com.example.aquaspot.model.service.DatabaseService
import com.example.aquaspot.model.service.StorageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class AuthViewModel(private val databaseService: DatabaseService): ViewModel(){
    val repository = AuthRepositoryImp()
    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _registerFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val registerFlow: StateFlow<Resource<FirebaseUser>?> = _registerFlow

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        if(repository.currentUser != null){
            _loginFlow.value = Resource.Success(repository.currentUser!!)
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch{
        _loginFlow.value = Resource.loading
        val result = repository.login(email, password)
        _loginFlow.value = result
    }
    fun register(fullName: String, phoneNumber: String, profileImage: Uri, email: String, password: String) = viewModelScope.launch{
        _registerFlow.value = Resource.loading
        val result = repository.register(fullName, phoneNumber, profileImage, email, password)
        _registerFlow.value = result
    }

    fun logout(){
        repository.logout()
        _loginFlow.value = null
        _registerFlow.value = null
    }
}

class AuthViewModelFactory(private val databaseService: DatabaseService): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthViewModel::class.java)){
            return AuthViewModel(databaseService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}