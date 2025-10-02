package com.example.angelix_vasquez_p2_p1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.angelix_vasquez_p2_p1.data.local.AppDatabase
import com.example.angelix_vasquez_p2_p1.ui.EntradasFormScreen
import com.example.angelix_vasquez_p2_p1.ui.EntradasScreen
import com.example.angelix_vasquez_p2_p1.viewmodel.EntradasHuacalesViewModel
import com.example.angelix_vasquez_p2_p1.viewmodel.EntradasHuacalesViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_db"
        ).build()

        val factory = EntradasHuacalesViewModelFactory(db.entradasDao())

        setContent {
            MaterialTheme {
                AppNavigation(factory)
            }
        }
    }
}

@Composable
fun AppNavigation(factory: EntradasHuacalesViewModelFactory) {
    val navController = rememberNavController()
    val viewModel: EntradasHuacalesViewModel = viewModel(factory = factory)

    NavHost(
        navController = navController,
        startDestination = "lista"
    ) {
        composable("lista") {
            EntradasScreen(
                viewModel = viewModel,
                onEdit = { entrada ->
                    navController.navigate("formulario/${entrada.idEntrada}")
                }
            )
        }
        composable(
            route = "formulario/{idEntrada}",
            arguments = listOf(navArgument("idEntrada") { type = NavType.IntType })
        ) { backStackEntry ->
            val idEntrada = backStackEntry.arguments?.getInt("idEntrada")
            val entrada = viewModel.uiState.value.entradas.find { it.idEntrada == idEntrada }

            EntradasFormScreen(
                viewModel = viewModel,
                entradaExistente = entrada,
                onSave = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}
