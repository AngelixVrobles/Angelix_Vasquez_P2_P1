package com.example.angelix_vasquez_p2_p1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.angelix_vasquez_p2_p1.data.local.dao.EntradasDao
import com.example.angelix_vasquez_p2_p1.data.local.entities.EntradasHuacalesEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class EntradasUiState(
    val entradas: List<EntradasHuacalesEntity> = emptyList()
)

class EntradasHuacalesViewModel(private val dao: EntradasDao) : ViewModel() {

    private val _uiState = MutableStateFlow(EntradasUiState())
    val uiState: StateFlow<EntradasUiState> = _uiState

    init {
        cargarEntradas()
    }

    private fun cargarEntradas() {
        viewModelScope.launch {
            dao.getAll().collect { lista ->
                _uiState.value = EntradasUiState(lista)
            }
        }
    }

    fun insertar(entrada: EntradasHuacalesEntity) {
        viewModelScope.launch {
            dao.insert(entrada)
        }
    }

    fun actualizar(entrada: EntradasHuacalesEntity) {
        viewModelScope.launch {
            dao.update(entrada)
        }
    }

    fun eliminar(entrada: EntradasHuacalesEntity) {
        viewModelScope.launch {
            dao.delete(entrada)
        }
    }
}

class EntradasHuacalesViewModelFactory(
    private val dao: EntradasDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EntradasHuacalesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EntradasHuacalesViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
