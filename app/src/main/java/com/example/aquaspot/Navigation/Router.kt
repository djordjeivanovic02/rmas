package com.example.aquaspot.Navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aquaspot.screens.IndexScreen
import com.example.aquaspot.viewmodels.AuthViewModel
import com.example.aquaspot.screens.LoginScreen
import com.example.aquaspot.screens.RegisterScreen
import com.example.aquaspot.viewmodels.BeachViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Router(
    viewModel: AuthViewModel,
    beachViewModel: BeachViewModel
){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.loginScreen) {
        composable(Routes.loginScreen){
            LoginScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(Routes.indexScreen){
            IndexScreen(
                viewModel = viewModel,
                navController = navController,
                beachViewModel = beachViewModel
            )
        }
        composable(Routes.registerScreen){
            RegisterScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}