package com.library.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.library.app.data.repository.BookRepository
import com.library.app.data.remote.dto.BookResponseDTO
import com.library.app.data.remote.dto.PageResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _books = MutableStateFlow<List<BookResponseDTO>>(emptyList())
    val books: StateFlow<List<BookResponseDTO>> = _books.asStateFlow()

    private val _uiState = MutableStateFlow<BookListUiState>(BookListUiState.Idle)
    val uiState: StateFlow<BookListUiState> = _uiState.asStateFlow()

    private var currentPage = 0
    private var totalPages = 1
    private var isLoading = false

    fun loadBooks(refresh: Boolean = false) {
        if (isLoading) return
        
        viewModelScope.launch {
            isLoading = true
            if (refresh) {
                currentPage = 0
                _books.value = emptyList()
            }
            
            _uiState.value = BookListUiState.Loading
            
            try {
                val response = bookRepository.getAllBooks(page = currentPage, size = 20)
                if (response.isSuccessful) {
                    response.body()?.let { pageResponse ->
                        if (refresh) {
                            _books.value = pageResponse.content
                        } else {
                            _books.value = _books.value + pageResponse.content
                        }
                        currentPage = pageResponse.number
                        totalPages = pageResponse.totalPages
                        _uiState.value = BookListUiState.Success
                    } ?: run {
                        _uiState.value = BookListUiState.Error("Respuesta vacía")
                    }
                } else {
                    _uiState.value = BookListUiState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = BookListUiState.Error("Error de conexión: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun loadMore() {
        if (currentPage < totalPages - 1 && !isLoading) {
            currentPage++
            loadBooks()
        }
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            _uiState.value = BookListUiState.Loading
            try {
                val response = bookRepository.searchBooks(query, page = 0, size = 20)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _books.value = it.content
                        _uiState.value = BookListUiState.Success
                    }
                } else {
                    _uiState.value = BookListUiState.Error("Error en búsqueda")
                }
            } catch (e: Exception) {
                _uiState.value = BookListUiState.Error("Error: ${e.message}")
            }
        }
    }
}

sealed class BookListUiState {
    object Idle : BookListUiState()
    object Loading : BookListUiState()
    object Success : BookListUiState()
    data class Error(val message: String) : BookListUiState()
}


