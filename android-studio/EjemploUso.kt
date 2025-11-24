package com.library.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.library.api.RetrofitClient
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Ejemplo: Login
        loginExample()
        
        // Ejemplo: Obtener libros
        getBooksExample()
        
        // Ejemplo: Crear préstamo
        createLoanExample()
    }
    
    private fun loginExample() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.userApiService.login(
                    com.library.api.LoginRequest(
                        email = "juan@example.com",
                        password = "password123"
                    )
                )
                
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val token = loginResponse?.token
                    val user = loginResponse?.user
                    
                    // Guardar token en SharedPreferences o similar
                    // saveToken(token)
                    
                    println("Login exitoso: ${user?.name}")
                } else {
                    println("Error en login: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }
    
    private fun getBooksExample() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.bookApiService.getAllBooks(
                    page = 0,
                    size = 10
                )
                
                if (response.isSuccessful) {
                    val books = response.body()?.content
                    books?.forEach { book ->
                        println("Libro: ${book.title} - ${book.author}")
                    }
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }
    
    private fun createLoanExample() {
        lifecycleScope.launch {
            try {
                val token = "tu_token_aqui" // Obtener de SharedPreferences
                
                val response = RetrofitClient.loanApiService.createLoan(
                    com.library.api.CreateLoanRequest(
                        userId = 1,
                        bookId = 1,
                        loanDays = 14
                    ),
                    token = "Bearer $token"
                )
                
                if (response.isSuccessful) {
                    val loan = response.body()
                    println("Préstamo creado: ${loan?.id}")
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }
}





