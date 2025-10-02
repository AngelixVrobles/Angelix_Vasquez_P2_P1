package com.example.angelix_vasquez_p2_p1.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.angelix_vasquez_p2_p1.data.local.entities.EntradasHuacalesEntity
import com.example.angelix_vasquez_p2_p1.viewmodel.EntradasHuacalesViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntradasFormScreen(
    viewModel: EntradasHuacalesViewModel,
    entradaExistente: EntradasHuacalesEntity? = null,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    val azul = Color(0xFF1976D2)

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val fechaHoy = sdf.format(Date())

    var fecha by remember { mutableStateOf(entradaExistente?.fecha ?: fechaHoy) }
    var nombreCliente by remember { mutableStateOf(entradaExistente?.nombreCliente ?: "") }
    var cantidad by remember { mutableStateOf(entradaExistente?.cantidad?.toString() ?: "") }
    var precio by remember { mutableStateOf(entradaExistente?.precio?.toString() ?: "") }

    val importe = (cantidad.toIntOrNull() ?: 0) * (precio.toDoubleOrNull() ?: 0.0)

    val context = LocalContext.current
    val calendario = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val mes = (month + 1).toString().padStart(2, '0')
            val dia = dayOfMonth.toString().padStart(2, '0')
            fecha = "$year-$mes-$dia"
        },
        calendario.get(Calendar.YEAR),
        calendario.get(Calendar.MONTH),
        calendario.get(Calendar.DAY_OF_MONTH)
    )

    val camposValidos = fecha.isNotBlank() &&
            nombreCliente.isNotBlank() &&
            cantidad.toIntOrNull() != null &&
            precio.toDoubleOrNull() != null

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (entradaExistente == null) "Registrar Entrada" else "Editar Entrada",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = fecha,
                onValueChange = { },
                label = { Text("Fecha (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Seleccionar Fecha")
                    }
                }
            )

            OutlinedTextField(
                value = nombreCliente,
                onValueChange = { nombreCliente = it },
                label = { Text("Nombre del Cliente") },
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
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = importe.toString(),
                onValueChange = {},
                label = { Text("Importe") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if (camposValidos) {
                            if (entradaExistente == null) {
                                viewModel.insertar(
                                    EntradasHuacalesEntity(
                                        fecha = fecha,
                                        nombreCliente = nombreCliente,
                                        cantidad = cantidad.toInt(),
                                        precio = precio.toDouble()
                                    )
                                )
                            } else {
                                viewModel.actualizar(
                                    entradaExistente.copy(
                                        fecha = fecha,
                                        nombreCliente = nombreCliente,
                                        cantidad = cantidad.toInt(),
                                        precio = precio.toDouble()
                                    )
                                )
                            }
                            onSave()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50),
                    enabled = camposValidos,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = azul,
                        contentColor = Color.White,
                        disabledContainerColor = azul.copy(alpha = 0.4f),
                        disabledContentColor = Color.White.copy(alpha = 0.8f)
                    )
                ) {
                    Text("Guardar")
                }

                if (entradaExistente != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = { onDelete?.invoke() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = azul
                        ),
                        border = BorderStroke(1.dp, azul)
                    ) {
                        Text("Eliminar")
                    }
                }
            }

            OutlinedButton(
                onClick = { onCancel() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = azul
                ),
                border = BorderStroke(1.dp, azul)
            ) {
                Text("Cancelar")
            }
        }
    }
}
