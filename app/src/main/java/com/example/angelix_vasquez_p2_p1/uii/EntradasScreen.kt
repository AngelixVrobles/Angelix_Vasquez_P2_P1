package com.example.angelix_vasquez_p2_p1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.angelix_vasquez_p2_p1.data.local.entities.EntradasHuacalesEntity
import com.example.angelix_vasquez_p2_p1.viewmodel.EntradasHuacalesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntradasScreen(
    viewModel: EntradasHuacalesViewModel,
    onNuevaEntrada: () -> Unit,
    onEdit: (EntradasHuacalesEntity) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Entradas de Huacales",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Filtros", style = MaterialTheme.typography.bodyMedium)
                    Icon(Icons.Default.FilterList, contentDescription = "Filtros")
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    items(state.entradas) { entrada ->
                        EntradaItem(
                            entrada = entrada,
                            onClick = { onEdit(entrada) }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                FloatingActionButton(
                    onClick = { onNuevaEntrada() },
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 8.dp),
                    containerColor = Color(0xFF3F51B5),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Nueva entrada")
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.entradas.sumOf { it.cantidad }.toString(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "$${"%,.2f".format(state.entradas.sumOf { it.cantidad * it.precio })}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun EntradaItem(
    entrada: EntradasHuacalesEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F0F0) // gris claro
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = entrada.nombreCliente, fontWeight = FontWeight.Medium)
                Text(text = "${entrada.cantidad} x $${"%,.2f".format(entrada.precio)}")
            }
            Text(
                text = "$${"%,.2f".format(entrada.cantidad * entrada.precio)}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
