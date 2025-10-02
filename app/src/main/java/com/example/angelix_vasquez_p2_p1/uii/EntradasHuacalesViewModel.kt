package com.example.angelix_vasquez_p2_p1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.angelix_vasquez_p2_p1.data.local.dao.EntradasHuacalesDao
import com.example.angelix_vasquez_p2_p1.data.local.entities.EntradasHuacalesEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class EntradasUiState(
    val entradas: List<EntradasHuacalesEntity> = emptyList(),
    val filtroCliente: String = "",
    val fechaDesde: String? = null,
    val fechaHasta: String? = null,
    val cantidadDesde: Int? = null,
    val cantidadHasta: Int? = null,
    val precioDesde: Double? = null,
    val precioHasta: Double? = null,
    val totalCantidad: Int = 0,
    val totalPrecio: Double = 0.0,
    val clientes: List<String> = emptyList()
)

class EntradasHuacalesViewModel(
    private val dao: EntradasHuacalesDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(EntradasUiState())
    val uiState: StateFlow<EntradasUiState> = _uiState.asStateFlow()

    init {
        cargarEntradas()
    }

    private fun cargarEntradas() {
        viewModelScope.launch {
            dao.getAll().collect { lista ->
                aplicarFiltros(lista)
            }
        }
    }

    private fun aplicarFiltros(lista: List<EntradasHuacalesEntity>) {
        var filtrada = lista

        _uiState.value.filtroCliente.takeIf { it.isNotBlank() }?.let { cliente ->
            filtrada = filtrada.filter { it.nombreCliente.contains(cliente, ignoreCase = true) }
        }

        val desde = _uiState.value.fechaDesde
        val hasta = _uiState.value.fechaHasta
        if (!desde.isNullOrBlank() && !hasta.isNullOrBlank()) {
            filtrada = filtrada.filter { it.fecha >= desde && it.fecha <= hasta }
        }

        _uiState.value.cantidadDesde?.let { min ->
            filtrada = filtrada.filter { entrada -> entrada.cantidad >= min }
        }
        _uiState.value.cantidadHasta?.let { max ->
            filtrada = filtrada.filter { entrada -> entrada.cantidad <= max }
        }

        _uiState.value.precioDesde?.let { min ->
            filtrada = filtrada.filter { entrada -> entrada.precio >= min }
        }
        _uiState.value.precioHasta?.let { max ->
            filtrada = filtrada.filter { entrada -> entrada.precio <= max }
        }

        val totalCantidad = filtrada.sumOf { it.cantidad }
        val totalPrecio = filtrada.sumOf { it.cantidad * it.precio }

        _uiState.value = _uiState.value.copy(
            entradas = filtrada,
            totalCantidad = totalCantidad,
            totalPrecio = totalPrecio,
            clientes = lista.map { it.nombreCliente }.distinct()
        )
    }


    fun setFiltroCliente(cliente: String) {
        _uiState.value = _uiState.value.copy(filtroCliente = cliente)
        cargarEntradas()
    }

    fun setFiltroFecha(desde: String?, hasta: String?) {
        _uiState.value = _uiState.value.copy(fechaDesde = desde, fechaHasta = hasta)
        cargarEntradas()
    }

    fun setFiltroCantidad(desde: Int?, hasta: Int?) {
        _uiState.value = _uiState.value.copy(cantidadDesde = desde, cantidadHasta = hasta)
        cargarEntradas()
    }

    fun setFiltroPrecio(desde: Double?, hasta: Double?) {
        _uiState.value = _uiState.value.copy(precioDesde = desde, precioHasta = hasta)
        cargarEntradas()
    }

    fun insertar(entrada: EntradasHuacalesEntity) {
        viewModelScope.launch { dao.insert(entrada) }
    }

    fun actualizar(entrada: EntradasHuacalesEntity) {
        viewModelScope.launch { dao.update(entrada) }
    }

    fun deleteEntrada(entrada: EntradasHuacalesEntity) {
        viewModelScope.launch { dao.delete(entrada) }
    }
}

class EntradasHuacalesViewModelFactory(
    private val dao: EntradasHuacalesDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EntradasHuacalesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EntradasHuacalesViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
