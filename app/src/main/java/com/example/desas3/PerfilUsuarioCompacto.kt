package com.example.desas3

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

// Colores actuales
val AzulGrisOscuro = Color(0xFF3B5B8A)
val NegroPuro = Color(0xFF000000)
val AmarilloAlerta = Color(0xFFFFD700)
val FondoClaro = Color(0xFFF9FAFB)
val VerdeExito = Color(0xFF2BD600)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilUsuarioCompacto(navController: NavHostController) {
    val context = LocalContext.current
    var editando by remember { mutableStateOf(false) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var mostrarExito by remember { mutableStateOf(false) }

    var nombre by remember { mutableStateOf("Elynn") }
    var apellido by remember { mutableStateOf("Lee") }
    var correo by remember { mutableStateOf("michelle.@sada.exa") }
    var telefono by remember { mutableStateOf("30122359878") }
    var direccion by remember { mutableStateOf("Morales") }
    var vereda by remember { mutableStateOf("Vereda San Diego") }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    val veredas = listOf(
        "Vereda San Rafael", "Vereda San Diego", "Vereda El Triunfo", "Vereda La Primavera",
        "Vereda El Rosal", "Vereda La Esperanza", "Vereda Los Pinos", "Vereda San Antonio",
        "Vereda El Paraíso", "Vereda El Carmen", "Vereda El Roble", "Vereda La Palma",
        "Vereda Santa Rosa", "Vereda El Placer", "Vereda La Cumbre", "Vereda Las Delicias",
        "Vereda La Floresta", "Vereda Los Ángeles", "Vereda La Unión", "Vereda Monteverde",
        "Vereda Alto Bonito", "Vereda El Edén", "Vereda Campo Hermoso", "Vereda La Loma",
        "Vereda Las Brisas", "Vereda El Jardín", "Vereda Bella Vista", "Vereda El Mirador",
        "Vereda San José", "Vereda La Ceiba", "Vereda El Progreso", "Vereda El Nogal"
    )

    val galeriaLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        fotoUri = uri
    }

    val camaraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        // Aquí puedes convertir el bitmap a URI si lo necesitas
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoClaro)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Perfil de Usuario", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(Modifier.height(20.dp))

            Box {
                AsyncImage(
                    model = fotoUri,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .border(3.dp, AzulGrisOscuro, CircleShape),
                    contentScale = ContentScale.Crop
                )
                if (fotoUri == null) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Foto predeterminada",
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .border(3.dp, AzulGrisOscuro, CircleShape),
                        tint = AzulGrisOscuro
                    )
                }
            }

            if (editando) {
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    IconButton(onClick = { camaraLauncher.launch(null) }) {
                        Icon(Icons.Filled.CameraAlt, contentDescription = "Cámara", tint = AzulGrisOscuro)
                    }
                    IconButton(onClick = { galeriaLauncher.launch("image/*") }) {
                        Icon(Icons.Filled.Photo, contentDescription = "Galería", tint = AzulGrisOscuro)
                    }
                    IconButton(onClick = { fotoUri = null }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = AzulGrisOscuro)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    if (editando) {
                        OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") }, singleLine = true)
                        OutlinedTextField(apellido, { apellido = it }, label = { Text("Apellido") }, singleLine = true)
                        OutlinedTextField(correo, {}, label = { Text("Correo") }, singleLine = true, enabled = false)
                        OutlinedTextField(telefono, { telefono = it }, label = { Text("Teléfono") }, singleLine = true)
                        OutlinedTextField(direccion, { direccion = it }, label = { Text("Dirección") }, singleLine = true)

                        var expanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                            OutlinedTextField(
                                value = vereda,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Vereda") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                veredas.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it) },
                                        onClick = {
                                            vereda = it
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField("", {}, label = { Text("Contraseña") }, singleLine = true)
                    } else {
                        Text("Nombre: $nombre $apellido", color = NegroPuro)
                        Text("Correo: $correo", color = NegroPuro)
                        Text("Teléfono: $telefono", color = NegroPuro)
                        Text("Dirección: $direccion", color = NegroPuro)
                        Text("Vereda: $vereda", color = NegroPuro)
                    }

                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (editando) mostrarDialogo = true else editando = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (editando) AzulGrisOscuro else AmarilloAlerta,
                            contentColor = if (editando) Color.White else NegroPuro
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (editando) "Guardar cambios" else "Editar información")
                    }
                }
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Confirmar") },
            text = { Text("¿Estás seguro de guardar los cambios?") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogo = false
                    editando = false
                    mostrarExito = true
                }) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (mostrarExito) {
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(3000)
            mostrarExito = false
        }
        Box(
            Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = VerdeExito),
                modifier = Modifier.padding(16.dp)
            ) {
                Text("✓ Cambios guardados", color = Color.Black, modifier = Modifier.padding(12.dp))
            }
        }
    }
}