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

@Composable
fun HomeScreen(navController: NavController) {
    var selectedDisaster by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)) // Fondo claro profesional
            .padding(24.dp)
    ) {
        // Barra superior con nombre de usuario y menú
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Paul S.Harvel",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color(0xFF2C3E50), // Azul oscuro profesional
                    fontWeight = FontWeight.Bold
                )
            )

            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menú",
                tint = Color(0xFF2C3E50),
                modifier = Modifier
                    .size(40.dp) // Ícono más grande
                    .clickable { /* Mostrar menú/drawer */ }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Sección de iconos de desastres
        DisasterIconsRow(selectedDisaster) { disaster ->
            selectedDisaster = disaster
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Botón de informe
        Button(
            onClick = { /* Acción de informe */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp), // Botón más alto
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3498DB), // Azul profesional
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp), // Bordes más redondeados
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp // Sombra para efecto 3D
            )
        ) {
            Text("INFORMAR",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Logos de organismos de respuesta
        ResponseEntitiesRow()

        Spacer(modifier = Modifier.height(32.dp))

        // 🔵 Botón para ir al perfil de usuario
        Button(
            onClick = { navController.navigate("perfil") }, // Asegúrate de que esta ruta exista en tu NavHost
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2C3E50),
                contentColor = Color.White
            ),
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


@Composable
fun DisasterIconsRow(
    selectedDisaster: String?,
    onDisasterSelected: (String) -> Unit
) {
    val disasters = listOf(
        "Tormenta" to R.drawable.ic_storm,
        "Incendio" to R.drawable.ic_fire,
        "Inundación" to R.drawable.ic_flood,
        "Avalancha" to R.drawable.ic_avalanche
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Tipos de Emergencia",
            color = Color(0xFF2C3E50),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            disasters.forEach { (disaster, iconRes) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onDisasterSelected(disaster) }
                        .padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp) // Logos mucho más grandes
                            .background(
                                color = if (selectedDisaster == disaster) Color(0xFFE3F2FD)
                                else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = disaster,
                            tint = if (selectedDisaster == disaster) Color(0xFF3498DB)
                            else Color(0xFF7F8C8D), // Gris azulado
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = disaster,
                        color = if (selectedDisaster == disaster) Color(0xFF3498DB)
                        else Color(0xFF2C3E50),
                        fontSize = 16.sp, // Texto más grande
                        fontWeight = FontWeight.Medium
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Organismos de Respuesta",
            color = Color(0xFF2C3E50),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            entities.forEach { (entity, iconRes) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp) // Logos institucionales más grandes
                            .background(
                                color = Color(0xFFE3F2FD),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = entity,
                            tint = Color(0xFF2C3E50),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = entity,
                        color = Color(0xFF2C3E50),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}