package com.example.aquaspot.Navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aquaspot.data.Resource
import com.example.aquaspot.model.Beach
import com.example.aquaspot.model.CustomUser
import com.example.aquaspot.screens.BeachScreen
import com.example.aquaspot.screens.IndexScreen
import com.example.aquaspot.viewmodels.AuthViewModel
import com.example.aquaspot.screens.LoginScreen
import com.example.aquaspot.screens.RankingScreen
import com.example.aquaspot.screens.RegisterScreen
import com.example.aquaspot.screens.SettingScreen
import com.example.aquaspot.screens.TableScreen
import com.example.aquaspot.screens.UserProfileScreen
import com.example.aquaspot.viewmodels.BeachViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.maps.android.compose.rememberCameraPositionState

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
            val beachesResource = beachViewModel.beaches.collectAsState()
            val beachMarkers = remember {
                mutableListOf<Beach>()
            }
            beachesResource.value.let {
                when(it){
                    is Resource.Success -> {
                        beachMarkers.clear()
                        beachMarkers.addAll(it.result)
                    }
                    is Resource.loading -> {

                    }
                    is Resource.Failure -> {
                        Log.e("Podaci", it.toString())
                    }
                    null -> {}
                }
            }
            IndexScreen(
                viewModel = viewModel,
                navController = navController,
                beachViewModel = beachViewModel,
                beachMarkers = beachMarkers
            )
        }
        composable(
            route = Routes.indexScreenWithParams + "/{isCameraSet}/{latitude}/{longitude}",
            arguments = listOf(
                navArgument("isCameraSet") { type = NavType.BoolType },
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val isCameraSet = backStackEntry.arguments?.getBoolean("isCameraSet")
            val latitude = backStackEntry.arguments?.getFloat("latitude")
            val longitude = backStackEntry.arguments?.getFloat("longitude")

            val beachesResource = beachViewModel.beaches.collectAsState()
            val beachMarkers = remember {
                mutableListOf<Beach>()
            }
            beachesResource.value.let {
                when(it){
                    is Resource.Success -> {
                        beachMarkers.clear()
                        beachMarkers.addAll(it.result)
                    }
                    is Resource.loading -> {

                    }
                    is Resource.Failure -> {
                        Log.e("Podaci", it.toString())
                    }
                    null -> {}
                }
            }

            IndexScreen(
                viewModel = viewModel,
                navController = navController,
                beachViewModel = beachViewModel,
                isCameraSet = remember { mutableStateOf(isCameraSet!!) },
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(LatLng(latitude!!.toDouble(), longitude!!.toDouble()), 17f)
                },
                beachMarkers = beachMarkers
            )
        }
        composable(
            route = Routes.indexScreenWithParams + "/{isCameraSet}/{latitude}/{longitude}/{beaches}",
            arguments = listOf(
                navArgument("isCameraSet") { type = NavType.BoolType },
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType },
                navArgument("beaches") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val isCameraSet = backStackEntry.arguments?.getBoolean("isCameraSet")
            val latitude = backStackEntry.arguments?.getFloat("latitude")
            val longitude = backStackEntry.arguments?.getFloat("longitude")
            val beachesJson = backStackEntry.arguments?.getString("beaches")
            val beaches = Gson().fromJson(beachesJson, Array<Beach>::class.java).toList()

            IndexScreen(
                viewModel = viewModel,
                navController = navController,
                beachViewModel = beachViewModel,
                isCameraSet = remember { mutableStateOf(isCameraSet!!) },
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(LatLng(latitude!!.toDouble(), longitude!!.toDouble()), 17f)
                },
                beachMarkers = beaches.toMutableList()
            )
        }
        composable(
            route = Routes.indexScreenWithParams + "/{beaches}",
            arguments = listOf(
                navArgument("beaches") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val beachesJson = backStackEntry.arguments?.getString("beaches")
            val beaches = Gson().fromJson(beachesJson, Array<Beach>::class.java).toList()
            IndexScreen(
                viewModel = viewModel,
                navController = navController,
                beachViewModel = beachViewModel,
                beachMarkers = beaches.toMutableList()
            )
        }
        composable(Routes.registerScreen){
            RegisterScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(
            route = Routes.beachScreen + "/{beach}",
            arguments = listOf(
                navArgument("beach"){ type = NavType.StringType }
            )
        ){backStackEntry ->
            val beachJson = backStackEntry.arguments?.getString("beach")
            val beach = Gson().fromJson(beachJson, Beach::class.java)
            beachViewModel.getBeachAllRates(beach.id)
            BeachScreen(
                beach = beach,
                navController = navController,
                beachViewModel = beachViewModel,
                viewModel = viewModel,
                beaches = null
            )
        }
        composable(
            route = Routes.beachScreen + "/{beach}/{beaches}",
            arguments = listOf(
                navArgument("beach"){ type = NavType.StringType },
                navArgument("beaches"){ type = NavType.StringType },
            )
        ){backStackEntry ->
            val beachesJson = backStackEntry.arguments?.getString("beaches")
            val beaches = Gson().fromJson(beachesJson, Array<Beach>::class.java).toList()
            val beachJson = backStackEntry.arguments?.getString("beach")
            val beach = Gson().fromJson(beachJson, Beach::class.java)
            beachViewModel.getBeachAllRates(beach.id)

            BeachScreen(
                beach = beach,
                navController = navController,
                beachViewModel = beachViewModel,
                viewModel = viewModel,
                beaches = beaches.toMutableList()
            )
        }
        composable(
            route = Routes.userProfileScreen + "/{userData}",
            arguments = listOf(navArgument("userData"){
                type = NavType.StringType
            })
        ){backStackEntry ->
            val userDataJson = backStackEntry.arguments?.getString("userData")
            val userData = Gson().fromJson(userDataJson, CustomUser::class.java)
            val isMy = FirebaseAuth.getInstance().currentUser?.uid == userData.id
            UserProfileScreen(
                navController = navController,
                viewModel = viewModel,
                beachViewModel = beachViewModel,
                userData = userData,
                isMy = isMy
            )
        }
        composable(
            route = Routes.tableScreen + "/{beaches}",
            arguments = listOf(navArgument("beaches") { type = NavType.StringType })
        ){ backStackEntry ->
            val beachesJson = backStackEntry.arguments?.getString("beaches")
            val beaches = Gson().fromJson(beachesJson, Array<Beach>::class.java).toList()
            TableScreen(beaches = beaches, navController = navController, beachViewModel = beachViewModel)
        }
        
        composable(Routes.settingsScreen){
            SettingScreen(navController = navController)
        }
        composable(Routes.rankingScreen){
            RankingScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}