package com.example.demoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.demoapp.ui.recordingScreen.RecordingScreen
import com.example.demoapp.ui.homeScreen.HomeScreen

object Routes {
    const val HOME = "home"
    const val RECORDING = "recording"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    println("AppGraph")
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            println("Home")
            HomeScreen(onNavigateToRecording = { navController.navigate(Routes.RECORDING) })
        }
        composable(Routes.RECORDING) {
            println("Recording")
            RecordingScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}