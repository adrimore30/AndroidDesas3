package com.example.desas3

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import com.example.desas3.ui.theme.Desas3Theme

// PALETA DE COLORES
val AzulGrisOscuro = Color(0xFF3B5B8A)
val NegroPuro = Color(0xFF000000)
val AmarilloAlerta = Color(0xFFFFD700)
val FondoClaro = Color(0xFFF9FAFB)

@Composable
fun EditableField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    )
}


@Composable
fun InfoRowWithIcon(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AzulGrisOscuro,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, fontWeight = FontWeight.Bold, color = NegroPuro)
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = value, color = NegroPuro)
    }
}

@Composable
fun PerfilUsuarioCompacto() {
    var editando by rememberSaveable { mutableStateOf(false) }

    var nombre by rememberSaveable { mutableStateOf("Elynn Lee") }
    var correo by rememberSaveable { mutableStateOf("michelle.@sada.exa") }
    var telefono by rememberSaveable { mutableStateOf("30122359878") }
    var ubicacion by rememberSaveable { mutableStateOf("Morales Vereda San Benites") }
    var direccion by rememberSaveable { mutableStateOf("Morales Vereda San Benites") }

    Surface(color = FondoClaro, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Perfil de Usuario",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AzulGrisOscuro
            )

            Spacer(modifier = Modifier.height(20.dp))

            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .border(3.dp, AzulGrisOscuro, CircleShape),
                tint = AzulGrisOscuro
            )

            if (editando) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Editar Foto",
                    color = AmarilloAlerta,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (editando) {
                        EditableField("Nombre", nombre) { nombre = it }
                        EditableField("Correo", correo) { correo = it }
                        EditableField("Teléfono", telefono) { telefono = it }
                        EditableField("Ubicación", ubicacion) { ubicacion = it }
                        EditableField("Dirección", direccion) { direccion = it }

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                editando = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AzulGrisOscuro),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Guardar Cambios", color = Color.White)
                        }
                    } else {
                        InfoRowWithIcon(Icons.Default.Person, "Nombre:", nombre)
                        InfoRowWithIcon(Icons.Filled.Mail, "Correo:", correo)
                        InfoRowWithIcon(Icons.Filled.Phone, "Teléfono:", telefono)
                        InfoRowWithIcon(Icons.Filled.LocationOn, "Ubicación:", ubicacion)
                        InfoRowWithIcon(Icons.Filled.Place, "Dirección:", direccion)

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { editando = true },
                            colors = ButtonDefaults.buttonColors(containerColor = AmarilloAlerta),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Editar Perfil", color = NegroPuro)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilUsuarioCompactoPreview() {
    Desas3Theme {
        PerfilUsuarioCompacto()
    }
}
