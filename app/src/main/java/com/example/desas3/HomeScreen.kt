package com.example.desas3

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // NAV BAR (logo a la izquierda y Menú a la derecha)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo ficticio al lado izquierdo
            Text(
                text = "DESAS3",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            // Icono Menú
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menú",
                modifier = Modifier
                    .size(36.dp)
                    .clickable { /* Aquí puedes mostrar un Drawer o acción */ }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // BOTÓN 3D (amarillo) hacia Chat
        Button(
            onClick = { navController.navigate("chat") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)), // amarillo
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(60.dp)
                .padding(horizontal = 16.dp),
            elevation = ButtonDefaults.buttonElevation(10.dp)
        ) {
            Text("Ir al Chat", fontSize = 20.sp, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Sección de íconos (2x2)
        EmergencyIconGrid()
    }
}

@Composable
fun EmergencyIconGrid() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            EmergencyIconItem(Icons.Default.LocalFireDepartment, "Bomberos")
            EmergencyIconItem(Icons.Default.LocalHospital, "Hospital")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            EmergencyIconItem(Icons.Default.Security, "Defensa Civil")
            EmergencyIconItem(Icons.Default.Warning, "UNGRD")
        }
    }
}

@Composable
fun EmergencyIconItem(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, fontSize = 14.sp)
    }
}

