package com.example.angelix_vasquez_p2_p1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import com.example.angelix_vasquez_p2_p1.data.local.entities.EntradasHuacalesEntity
import com.example.angelix_vasquez_p2_p1.viewmodel.EntradasHuacalesViewModel
import java.text.DecimalFormat

@Composable
fun EntradasScreen(
    viewModel: EntradasHuacalesViewModel,
    onEdit: (EntradasHuacalesEntity) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    val totalCantidad = state.entradas.sumOf { it.cantidad }
    val totalDinero = state.entradas.sumOf { (it.cantidad * it.precio).toDouble() }

    val formatoMoneda = DecimalFormat("#,###.00")
    val totalDineroFormateado = "RD$ ${formatoMoneda.format(totalDinero)}"

    Scaffold(
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                FloatingActionButton(
                    onClick = {
                        onEdit(
                            EntradasHuacalesEntity(
                                idEntrada = 0,
                                fecha = "",
                                nombreCliente = "",
                                cantidad = 0,
                                precio = 0.0
                            )
                        )
                    }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Nueva entrada")
                }

                // Franja de totales debajo del botón
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .background(Color.LightGray)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$totalCantidad",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 32.dp) // 👉 más separado del borde
                    )
                    Text(
                        text = totalDineroFormateado,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Entradas de Huacales", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(state.entradas) { entrada ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = { onEdit(entrada) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Cliente: ${entrada.nombreCliente}")
                            Text("Fecha: ${entrada.fecha}")
                            Text("Cantidad: ${entrada.cantidad}")
                            Text("Precio: ${entrada.precio}")
                            Text("Subtotal: ${entrada.cantidad * entrada.precio}")
                        }
                    }
                }
            }
        }
    }
}
