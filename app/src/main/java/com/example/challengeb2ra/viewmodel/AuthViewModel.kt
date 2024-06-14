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
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> get() = _isAuthenticated

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun login(email: String, password: String, onLoginSuccess: () -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            if (email == "user@example.com" && password == "password") {
                _isAuthenticated.value = true
                _error.value = null
                onLoginSuccess()
            } else {
                _isAuthenticated.value = false
                _error.value = "Usuario o contraseña son incorrectos"
            }
            _isLoading.value = false
        }
    }

    fun register(email: String, firstName: String, lastName: String, password: String, onRegisterSuccess: () -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            if (email.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty() && password.length >= 6) {
                _isAuthenticated.value = true
                _error.value = null
                onRegisterSuccess()
            } else {
                _isAuthenticated.value = false
                _error.value = "Todos los campos son obligatorios y la contraseña debe tener al menos 6 caracteres"
            }
            _isLoading.value = false
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        _isAuthenticated.value = false
        onLogoutSuccess()
    }
}


