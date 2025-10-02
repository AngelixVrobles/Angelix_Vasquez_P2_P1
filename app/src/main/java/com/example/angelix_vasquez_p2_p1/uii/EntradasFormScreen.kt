package com.example.angelix_vasquez_p2_p1.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.angelix_vasquez_p2_p1.data.local.entities.EntradasHuacalesEntity
import com.example.angelix_vasquez_p2_p1.viewmodel.EntradasHuacalesViewModel

@Composable
fun EntradasFormScreen(
    viewModel: EntradasHuacalesViewModel,
    entradaExistente: EntradasHuacalesEntity? = null,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    var fecha by remember { mutableStateOf(entradaExistente?.fecha ?: "") }
    var cliente by remember { mutableStateOf(entradaExistente?.nombreCliente ?: "") }
    var cantidad by remember { mutableStateOf(entradaExistente?.cantidad?.toString() ?: "") }
    var precio by remember { mutableStateOf(entradaExistente?.precio?.toString() ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (entradaExistente == null) "Nueva Entrada" else "Editar Entrada",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = cliente,
            onValueChange = { cliente = it },
            label = { Text("Nombre Cliente") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = cantidad,
            onValueChange = { cantidad = it },
            label = { Text("Cantidad") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio Unitario") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Cancelar")
            }

            Button(
                onClick = {
                    val nuevaEntrada = EntradasHuacalesEntity(
                        idEntrada = entradaExistente?.idEntrada ?: 0,
                        fecha = fecha,
                        nombreCliente = cliente,
                        cantidad = cantidad.toIntOrNull() ?: 0,
                        precio = precio.toDoubleOrNull() ?: 0.0
                    )
                    if (entradaExistente == null) {
                        viewModel.insertar(nuevaEntrada)
                    } else {
                        viewModel.actualizar(nuevaEntrada)
                    }
                    onSave()
                }
            ) {
                Text("Guardar")
            }
        }
    }
}
