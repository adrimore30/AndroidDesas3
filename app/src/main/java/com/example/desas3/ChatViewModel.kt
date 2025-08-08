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

    // Respuestas predefinidas basadas en el tipo de emergencia
    private val emergencyResponses = mapOf(
        "Tormenta" to "Estamos monitoreando la tormenta. ¿Puedes describir la situación actual?",
        "Incendio" to "Reporte de incendio recibido. ¿Hay personas en peligro?",
        "Inundación" to "Alerta de inundación. ¿Qué nivel de agua estás observando?",
        "Avalancha" to "Emergencia por avalancha. ¿Puedes indicar tu ubicación exacta?"
    )

    fun sendMessage(message: String, emergencyType: String? = null) {
        // Añade mensaje del usuario
        _messages.value = _messages.value + ChatMessage(message, isUserMe = true)

        // Simula respuesta basada en el tipo de emergencia
        viewModelScope.launch {
            delay(1000)
            val response = emergencyType?.let {
                emergencyResponses[it] ?: "Hola, ¿en qué puedo ayudarte?"
            } ?: "Hola, ¿qué deseas reportar?"

            _messages.value = _messages.value + ChatMessage(response, isUserMe = false)
        }
    }
}