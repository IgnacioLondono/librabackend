package com.library.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.library.app.data.repository.BookSyncRepository
import com.library.app.data.remote.dto.BookResponseDTO
import com.library.app.data.remote.dto.BookSyncDTO
import com.library.app.data.remote.dto.BookSyncResponseDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para sincronización de libros entre Android y el servidor
 */
@HiltViewModel
class BookSyncViewModel @Inject constructor(
    private val bookSyncRepository: BookSyncRepository
) : ViewModel() {

    private val _syncState = MutableStateFlow<BookSyncUiState>(BookSyncUiState.Idle)
    val syncState: StateFlow<BookSyncUiState> = _syncState.asStateFlow()

    private val _serverBooks = MutableStateFlow<List<BookResponseDTO>>(emptyList())
    val serverBooks: StateFlow<List<BookResponseDTO>> = _serverBooks.asStateFlow()

    /**
     * Obtener todos los libros del servidor
     */
    fun loadAllBooksFromServer() {
        viewModelScope.launch {
            _syncState.value = BookSyncUiState.Loading
            try {
                val response = bookSyncRepository.getAllBooksFromServer()
                if (response.isSuccessful) {
                    _serverBooks.value = response.body() ?: emptyList()
                    _syncState.value = BookSyncUiState.Success("Cargados ${_serverBooks.value.size} libros")
                } else {
                    _syncState.value = BookSyncUiState.Error("Error al cargar libros: ${response.code()}")
                }
            } catch (e: Exception) {
                _syncState.value = BookSyncUiState.Error("Error de conexión: ${e.message}")
            }
        }
    }

    /**
     * Sincronizar libros locales al servidor
     * @param localBooks Lista de libros desde la base de datos local de Android
     */
    fun syncBooksToServer(localBooks: List<BookSyncDTO>) {
        viewModelScope.launch {
            _syncState.value = BookSyncUiState.Loading
            try {
                val response = bookSyncRepository.syncBooksToServer(localBooks)
                if (response.isSuccessful) {
                    response.body()?.let { syncResponse ->
                        val message = "Sincronización completada: " +
                                "${syncResponse.created} creados, " +
                                "${syncResponse.updated} actualizados, " +
                                "${syncResponse.errors} errores"
                        _syncState.value = BookSyncUiState.SyncSuccess(syncResponse, message)
                    } ?: run {
                        _syncState.value = BookSyncUiState.Error("Respuesta vacía del servidor")
                    }
                } else {
                    _syncState.value = BookSyncUiState.Error("Error en sincronización: ${response.code()}")
                }
            } catch (e: Exception) {
                _syncState.value = BookSyncUiState.Error("Error de conexión: ${e.message}")
            }
        }
    }

    /**
     * Eliminar libros del servidor
     */
    fun deleteBooksFromServer(bookIds: List<Long>) {
        viewModelScope.launch {
            _syncState.value = BookSyncUiState.Loading
            try {
                val response = bookSyncRepository.deleteBooksBulk(bookIds)
                if (response.isSuccessful) {
                    _syncState.value = BookSyncUiState.Success("${bookIds.size} libros eliminados")
                    // Recargar lista
                    loadAllBooksFromServer()
                } else {
                    _syncState.value = BookSyncUiState.Error("Error al eliminar: ${response.code()}")
                }
            } catch (e: Exception) {
                _syncState.value = BookSyncUiState.Error("Error de conexión: ${e.message}")
            }
        }
    }

    fun resetState() {
        _syncState.value = BookSyncUiState.Idle
    }
}

sealed class BookSyncUiState {
    object Idle : BookSyncUiState()
    object Loading : BookSyncUiState()
    data class Success(val message: String) : BookSyncUiState()
    data class SyncSuccess(val syncResponse: BookSyncResponseDTO, val message: String) : BookSyncUiState()
    data class Error(val message: String) : BookSyncUiState()
}


