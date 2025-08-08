package com.example.desas3

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.Image


// Colores del sistema
val YinMinBlue = Color(0xFF385B8A)
val Gold = Color(0xFFFFD700)
val RedCKIYK = Color(0xFFF02020)
val Sessal = Color(0xFFF9FAFB)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var selectedDisaster by remember { mutableStateOf<String?>(null) }
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Paul S.Harvel",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = YinMinBlue,
                    actionIconContentColor = Gold
                ),
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menú"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Sessal)
                    .padding(innerPadding)
                    .padding(24.dp)
            ) {
                // Sección de iconos de desastres
                DisasterIconsRow(navController, selectedDisaster) { disaster ->
                    selectedDisaster = disaster
                    navController.navigate("chat")
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Botón de informe
                Button(
                    onClick = { navController.navigate("reportar") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RedCKIYK,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        "INFORMAR EMERGENCIA",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Logos de organismos de respuesta
                ResponseEntitiesRow()

                Spacer(modifier = Modifier.height(32.dp))

                // Botón para ir al perfil de usuario
                OutlinedButton(
                    onClick = { navController.navigate("perfil") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = YinMinBlue,
                        containerColor = Sessal
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Ver Perfil de Usuario",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    )

    // Menú desplegable
    if (showMenu) {
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            modifier = Modifier
                .background(Color.White)
                .width(280.dp)
        ) {
            DropdownMenuItem(
                text = { Text("Tipo de Emergencia", color = YinMinBlue, fontWeight = FontWeight.Bold) },
                onClick = {}
            )
            listOf("Tormenta", "Incendio", "Inundación", "Avalancha").forEach { emergency ->
                DropdownMenuItem(
                    text = { Text(emergency, color = YinMinBlue) },
                    onClick = {
                        showMenu = false
                        selectedDisaster = emergency
                        navController.navigate("chat")
                    }
                )
            }
            Divider(color = YinMinBlue.copy(alpha = 0.2f))
            DropdownMenuItem(
                text = { Text("INFORMAR EMERGENCIA", color = RedCKIYK, fontWeight = FontWeight.Bold) },
                onClick = {
                    showMenu = false
                    navController.navigate("reportar")
                }
            )
            DropdownMenuItem(
                text = { Text("Publicar Emergencia", color = YinMinBlue, fontWeight = FontWeight.Bold) },
                onClick = {
                    showMenu = false
                    navController.navigate("publicar")
                }
            )
            Divider(color = YinMinBlue.copy(alpha = 0.2f))
            DropdownMenuItem(
                text = { Text("Organismos de Respuesta", color = YinMinBlue, fontWeight = FontWeight.Bold) },
                onClick = {}
            )
            listOf("Defensa Civil", "Bomberos", "ONEMI", "Cruz Roja").forEach { entity ->
                DropdownMenuItem(
                    text = { Text(entity, color = YinMinBlue) },
                    onClick = { showMenu = false }
                )
            }
            Divider(color = YinMinBlue.copy(alpha = 0.2f))
            DropdownMenuItem(
                text = { Text("Inicio", color = YinMinBlue) },
                onClick = {
                    showMenu = false
                    navController.navigate("home")
                }
            )
            DropdownMenuItem(
                text = { Text("Ver Perfil", color = YinMinBlue) },
                onClick = {
                    showMenu = false
                    navController.navigate("perfil")
                }
            )
            Divider(color = YinMinBlue.copy(alpha = 0.2f))
            DropdownMenuItem(
                text = { Text("Cerrar Sesión", color = RedCKIYK, fontWeight = FontWeight.Bold) },
                onClick = {
                    showMenu = false
                    navController.navigate("login")
                }
            )
        }
    }
}

@Composable
fun DisasterIconsRow(
    navController: NavController,
    selectedDisaster: String?,
    onDisasterSelected: (String) -> Unit
) {
    val disasters = listOf(
        "Tormenta" to R.drawable.ic_storm,
        "Incendio" to R.drawable.ic_fire,
        "Inundación" to R.drawable.ic_flood,
        "Avalancha" to R.drawable.ic_avalanche
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Tipos de Emergencia",
            color = YinMinBlue,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            disasters.forEach { (disaster, iconRes) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            onDisasterSelected(disaster)
                            navController.navigate("chat")
                        }
                        .padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(
                                color = if (selectedDisaster == disaster) YinMinBlue.copy(alpha = 0.1f)
                                else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = disaster,
                            tint = if (selectedDisaster == disaster) YinMinBlue
                            else YinMinBlue.copy(alpha = 0.7f),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = disaster,
                        color = if (selectedDisaster == disaster) YinMinBlue
                        else YinMinBlue.copy(alpha = 0.9f),
                        fontSize = 16.sp,
                        fontWeight = if (selectedDisaster == disaster) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun ResponseEntitiesRow() {
    val entities = listOf(
        "Cruz Roja" to R.drawable.ic_red_cross,
        "Defensa Civil" to R.drawable.ic_civil_defense,
        "Bomberos" to R.drawable.ic_firefighters,
        "ONEMI" to R.drawable.ic_onemi
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Organismos de Respuesta",
            color = YinMinBlue,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            entities.forEach { (entity, iconRes) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(12.dp)) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(color = Color.Transparent, shape = RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = entity,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = entity,
                        color = YinMinBlue,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
