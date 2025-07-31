package com.example.desas3

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUserMe: Boolean = true
)


@Composable
fun Desas3Chat() {
    val viewModel: ChatViewModel = viewModel()
    val messages by viewModel.messages.collectAsState()
    var currentMessage by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DESAS3 - Chat", color = Color.White) },
                backgroundColor = Color.Black
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                reverseLayout = true,
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(messages.reversed()) { message ->
                    ChatBubble(message)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = currentMessage,
                    onValueChange = { currentMessage = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribe un mensaje...") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFFF0F0F0)
                    )
                )
                IconButton(
                    onClick = {
                        if (currentMessage.text.isNotBlank()) {
                            viewModel.sendMessage(currentMessage.text)
                            currentMessage = TextFieldValue("")
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Enviar",
                        tint = Color(0xFF1E88E5)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val alignment = if (message.isUserMe) Alignment.End else Alignment.Start
    val backgroundColor = if (message.isUserMe) Color(0xFF1E88E5) else Color(0xFFE0E0E0)
    val textColor = if (message.isUserMe) Color.White else Color.Black
    val shape = if (message.isUserMe) {
        MaterialTheme.shapes.medium.copy(topEnd = androidx.compose.foundation.shape.CornerSize(0.dp))
    } else {
        MaterialTheme.shapes.medium.copy(topStart = androidx.compose.foundation.shape.CornerSize(0.dp))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isUserMe) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = backgroundColor,
            shape = shape,
            elevation = 4.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = message.text,
                color = textColor,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}
