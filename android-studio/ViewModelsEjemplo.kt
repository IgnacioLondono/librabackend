package com.example.uinavegacion.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.remote.dto.LoginRequestDto
import com.example.uinavegacion.data.remote.dto.UserApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel de ejemplo para Login
 * Este es un ejemplo de cómo estructurar ViewModels para poder testearlos
 */
class LoginViewModel(
    private val userApi: UserApi
) : ViewModel() {

    // Estados observables
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _user = MutableStateFlow<com.example.uinavegacion.data.remote.dto.UserDto?>(null)
    val user: StateFlow<com.example.uinavegacion.data.remote.dto.UserDto?> = _user

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val response = userApi.login(LoginRequestDto(email, password))

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    _token.value = loginResponse?.token
                    _user.value = loginResponse?.user
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}


