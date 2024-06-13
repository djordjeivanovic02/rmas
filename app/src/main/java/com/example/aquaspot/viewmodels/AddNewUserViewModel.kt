package com.example.aquaspot.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aquaspot.model.service.StorageService

class AddNewUserViewModel(
    private val storageService: StorageService
): ViewModel(){

}

class AddNewUserViewModelFactory(private val storageService: StorageService): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthViewModel::class.java)){
            return AddNewUserViewModel(storageService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}