package com.library.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.library.app.data.repository.UserRepository
import com.library.app.data.remote.dto.LoginResponseDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val response = userRepository.login(email, password)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _uiState.value = LoginUiState.Success(it)
                    } ?: run {
                        _uiState.value = LoginUiState.Error("Respuesta vacía del servidor")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = LoginUiState.Error(
                        errorBody ?: "Error al iniciar sesión: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("Error de conexión: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val loginResponse: LoginResponseDTO) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}


