package com.example.desas3

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.Image

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C2C2C))
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {

        // Imagen del logo
        Image(
            painter = painterResource(id = R.drawable.logochico), // Asegúrate de que el nombre sea correcto
            contentDescription = null,
            modifier = Modifier
                .size(68.dp) // Cambia el tamaño según sea necesario
                .align(Alignment.TopCenter) // Alinea la imagen en la parte superior
                .padding(bottom = 10.dp) // Espaciado inferior para evitar solapamiento
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Iniciar sesión",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Escribe tu correo y contraseña",
                fontSize = 14.sp,
                color = Color(0xFFCCCCCC),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Correo
            Text(
                text = "Correo",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("tucorreo@gmail.com", color = Color(0xFF888888)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF666666),
                    unfocusedBorderColor = Color(0xFF666666),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )

            // Contraseña
            Text(
                text = "Contraseña",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("********", color = Color(0xFF888888)) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF666666),
                    unfocusedBorderColor = Color(0xFF666666),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            // Recordarme y ¿Olvidaste tu clave?
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFFFFD500),
                            uncheckedColor = Color.White
                        )
                    )
                    Text("Recordarme", color = Color.White, fontSize = 14.sp)
                }

                Text(
                    text = "¿Olvidaste tu clave?",
                    color = Color(0xFFFFD500),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        // Acción de recuperar clave
                    }
                )
            }

            // Botón Entrar
            Button(
                onClick = {
                    navController.navigate("home") // o la vista principal
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD500)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Entrar", color = Color.Black, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón de inicio de sesión con Gmail
            Button(
                onClick = {
                    // Lógica para iniciar sesión con Google
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Usa el ícono de Google que descargaste
                    Image(
                        painter = painterResource(id = R.drawable.google_logo), // Asegúrate de que el nombre sea correcto
                        contentDescription = "Iniciar sesión con Google",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Iniciar sesión con Google", color = Color.Black, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Text("¿No tienes cuenta? ", color = Color.White)
                Text(
                    text = "Regístrate",
                    color = Color(0xFFFFD500),
                    modifier = Modifier.clickable {
                        navController.navigate("register")
                    }
                )
            }
        }
    }
}
