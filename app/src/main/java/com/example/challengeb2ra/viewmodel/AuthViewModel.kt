package com.example.challengeb2ra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> get() = _isAuthenticated

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun login(email: String, password: String, onLoginSuccess: () -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _isAuthenticated.value = true
                _error.value = null
                onLoginSuccess()
            } catch (e: Exception) {
                _isAuthenticated.value = false
                _error.value = "Usuario o contraseña son incorrectos"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(email: String, firstName: String, lastName: String, password: String, onRegisterSuccess: () -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                when {
                    !isValidEmail(email) -> throw IllegalArgumentException("Correo electrónico inválido")
                    !isValidPassword(password) -> throw IllegalArgumentException("La contraseña debe tener al menos 6 caracteres")
                    else -> {
                        auth.createUserWithEmailAndPassword(email, password).await()
                        // Aquí podrías guardar los detalles adicionales del usuario (nombre, apellido) en una base de datos.
                        _isAuthenticated.value = true
                        _error.value = null
                        onRegisterSuccess()
                    }
                }
            } catch (e: IllegalArgumentException) {
                _isAuthenticated.value = false
                _error.value = e.message
            } catch (e: FirebaseAuthException) {
                _isAuthenticated.value = false
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        _isAuthenticated.value = false
        onLogoutSuccess()
    }
}


