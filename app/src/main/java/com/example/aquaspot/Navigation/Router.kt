package com.example.aquaspot.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aquaspot.screens.IndexScreen
import com.example.aquaspot.viewmodels.AuthViewModel
import com.example.aquaspot.screens.LoginScreen

@Composable
fun Router(
    viewModel: AuthViewModel
){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.loginScreen) {
        composable(Routes.loginScreen){
            LoginScreen(viewModel, navController)
        }
        composable(Routes.indexScreen){
            IndexScreen(viewModel, navController)
        }
    }
}