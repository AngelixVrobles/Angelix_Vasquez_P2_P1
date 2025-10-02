package com.example.angelix_vasquez_p2_p1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.angelix_vasquez_p2_p1.data.local.AppDatabase
import com.example.angelix_vasquez_p2_p1.ui.EntradasFormScreen
import com.example.angelix_vasquez_p2_p1.ui.EntradasScreen
import com.example.angelix_vasquez_p2_p1.viewmodel.EntradasHuacalesViewModel
import com.example.angelix_vasquez_p2_p1.viewmodel.EntradasHuacalesViewModelFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState

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
                val navController = rememberNavController()
                val viewModel: EntradasHuacalesViewModel = viewModel(factory = factory)
                val state by viewModel.uiState.collectAsState()

                NavHost(
                    navController = navController,
                    startDestination = "lista"
                ) {
                    composable("lista") {
                        EntradasScreen(
                            viewModel = viewModel,
                            onNuevaEntrada = { navController.navigate("formulario/0") },
                            onEdit = { entrada ->
                                navController.navigate("formulario/${entrada.idEntrada}")
                            }
                        )
                    }
                    composable("formulario/{idEntrada}") { backStackEntry ->
                        val idEntrada = backStackEntry.arguments?.getString("idEntrada")?.toIntOrNull()
                        val entrada = state.entradas.find { it.idEntrada == idEntrada }

                        EntradasFormScreen(
                            viewModel = viewModel,
                            entradaExistente = entrada,
                            onSave = { navController.popBackStack() },
                            onCancel = { navController.popBackStack() },
                            onDelete = {
                                entrada?.let { viewModel.eliminar(it) }
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
