package com.example.aquaspot.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aquaspot.data.AuthRepository
import com.example.aquaspot.data.AuthRepositoryImp
import com.example.aquaspot.data.Resource
import com.example.aquaspot.model.CustomUser
import com.example.aquaspot.model.service.StorageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel(private val storageService: StorageService): ViewModel(){
//    val repository = AuthRepositoryImp()
//
//    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
//    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow
//
//    private val _registerFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
//    val registerFlow: StateFlow<Resource<FirebaseUser>?> = _registerFlow
//
//    val currentUser: FirebaseUser?
//        get() = repository.currentUser
//
//    init {
//        if(repository.currentUser != null){
//            _loginFlow.value = Resource.Success(repository.currentUser!!)
//        }
//    }
//
//    fun login(email: String, password: String) = viewModelScope.launch{
//        _loginFlow.value = Resource.loading
//        val result = repository.login(email, password)
//        _loginFlow.value = result
//    }
//    fun register(name: String, email: String, password: String) = viewModelScope.launch{
//        _registerFlow.value = Resource.loading
//        val result = repository.register(name, email, password)
//        _registerFlow.value = result
//    }
//
//    fun logout(){
//        repository.logout()
//        _loginFlow.value = null
//        _registerFlow.value = null
//    }



    fun signUp(email: String, password: String){
        val user = CustomUser(
            username = email,
            password = password
        )
        viewModelScope.launch {
            storageService.register(user)
        }
    }
}

class AuthViewModelFactory(private val storageService: StorageService): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthViewModel::class.java)){
            return AuthViewModel(storageService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}