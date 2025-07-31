package com.example.desas3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    fun sendMessage(message: String) {
        // Añade mensaje del usuario
        _messages.value = _messages.value + ChatMessage(message, isUserMe = true)

        // Simula una respuesta automática después de 1 segundo
        viewModelScope.launch {
            delay(1000)
            _messages.value = _messages.value + ChatMessage("Hola, que deses reportar?", isUserMe = false)
        }
    }
}
