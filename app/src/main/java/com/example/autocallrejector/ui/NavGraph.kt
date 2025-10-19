package com.example.autocallrejector.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.autocallrejector.AutoCallRejectorApplication
import com.example.autocallrejector.ui.screens.AddNumberScreen
import com.example.autocallrejector.ui.screens.MainScreen

@Composable
fun NavGraph(
    onToggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean
) {
    val navController = rememberNavController()
    val application = navController.context.applicationContext as AutoCallRejectorApplication
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            val mainViewModel: MainViewModel = viewModel(
                factory = MainViewModelFactory(application.database.blockedNumberDao())
            )
            MainScreen(
                viewModel = mainViewModel,
                onAddClick = { navController.navigate("add") },
                onToggleDarkTheme = onToggleDarkTheme,
                isDarkTheme = isDarkTheme
            )
        }
        composable("add") {
            val addNumberViewModel: AddNumberViewModel = viewModel(
                factory = AddNumberViewModelFactory(application.database.blockedNumberDao())
            )
            AddNumberScreen(
                viewModel = addNumberViewModel,
                onSaveClick = { navController.popBackStack() }  // Navigate back after save
            )
        }
    }
}
