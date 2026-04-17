package com.sunilbb.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sunilbb.myapplication.di.repository
import com.sunilbb.myapplication.presentation.CountryDetailScreen
import com.sunilbb.myapplication.presentation.CountryScreen
import com.sunilbb.myapplication.presentation.CountryViewModel
import com.sunilbb.myapplication.presentation.NotesScreen
import com.sunilbb.myapplication.presentation.notes.NotesViewModel
import com.sunilbb.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel = CountryViewModel(repository)
                val navController = rememberNavController()
                val notesViewModel = NotesViewModel(savedStateHandle = SavedStateHandle())

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "list",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("list") {
                            CountryScreen(
                                viewModel = viewModel, navController = navController
                            )
                        }
                        composable("detail/{code}") { backStackEntry ->
                            val code = backStackEntry.arguments?.getString("code")
                            CountryDetailScreen(
                                viewModel = viewModel,
                                code = code ?: "", navcontroller = navController
                            )
                        }

                        composable(route = "notes"){
                            NotesScreen(notesViewModel = notesViewModel, navController = navController)
                        }
                    }
                }
            }
        }
    }
}
