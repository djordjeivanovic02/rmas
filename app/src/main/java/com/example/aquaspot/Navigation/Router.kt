package com.example.aquaspot.Navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aquaspot.model.Beach
import com.example.aquaspot.screens.BeachScreen
import com.example.aquaspot.screens.IndexScreen
import com.example.aquaspot.viewmodels.AuthViewModel
import com.example.aquaspot.screens.LoginScreen
import com.example.aquaspot.screens.RegisterScreen
import com.example.aquaspot.screens.TableScreen
import com.example.aquaspot.screens.UserProfileScreen
import com.example.aquaspot.viewmodels.BeachViewModel
import com.google.gson.Gson

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
        composable(
            route = Routes.beachScreen,
            arguments = listOf(navArgument("beach"){
                type = NavType.StringType
            })
        ){backStackEntry ->
            val beachJson = backStackEntry.arguments?.getString("beach")
            val beach = Gson().fromJson(beachJson, Beach::class.java)
            BeachScreen(
                beach = beach,
                navController = navController
            )
        }
        composable(Routes.userProfileScreen){
            UserProfileScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(Routes.tableScreen){
            TableScreen()
        }
    }
}