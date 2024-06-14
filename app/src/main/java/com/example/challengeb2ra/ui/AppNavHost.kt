package com.example.challengeb2ra.ui


import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.challengeb2ra.data.Earthquake
import com.example.challengeb2ra.ui.views.EarthquakeDetailScreen
import com.example.challengeb2ra.ui.views.EarthquakeListScreen
import com.example.challengeb2ra.ui.views.LoginScreen
import com.example.challengeb2ra.ui.views.RegisterScreen
import com.example.challengeb2ra.viewmodel.AuthViewModel
import com.example.challengeb2ra.viewmodel.EarthquakeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    authViewModel: AuthViewModel = viewModel(),
    earthquakeViewModel: EarthquakeViewModel = viewModel()
) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(authViewModel, onLoginSuccess = { navController.navigate("earthquakeList") }, navController = navController)
        }
        composable("register") {
            RegisterScreen(authViewModel, onRegisterSuccess = { navController.navigate("earthquakeList") }, navController = navController)
        }
        composable("earthquakeList") {
            EarthquakeListScreen(earthquakeViewModel, authViewModel, navController) { earthquakeId ->
                navController.navigate("earthquakeDetail/$earthquakeId")
            }
        }
        composable("earthquakeDetail/{earthquakeId}") { backStackEntry ->
            val earthquakeId = backStackEntry.arguments?.getString("earthquakeId")
            if (earthquakeId != null) {
                LaunchedEffect(earthquakeId) {
                    earthquakeViewModel.fetchEarthquakes()
                }
                val earthquake = earthquakeViewModel.earthquakes.collectAsState().value.find { it.id == earthquakeId }
                if (earthquake != null) {
                    EarthquakeDetailScreen(earthquake)
                } else {
                    Log.e("AppNavHost", "Earthquake not found for id: $earthquakeId")
                }
            }
        }
    }
}






