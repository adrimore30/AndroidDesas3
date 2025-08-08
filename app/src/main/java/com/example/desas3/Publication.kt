package com.example.desas3

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.desas3.R

// Modelo de publicación
data class Publicacion(
    val titulo: String,
    val descripcion: String,
    val ubicacion: String,
    val fecha: String,
    val tipo: String,
    val severidad: String,
    val rol: String,
    val imagenResId: Int
)

// Pantalla principal de publicaciones
@Composable
fun PublicationScreen(navController: NavHostController) {
    val publicaciones = remember { mutableStateListOf<Publicacion>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1C))
            .padding(12.dp)
    ) {
        BotonesAgregarDesastre(
            onAgregarIncendio = {
                publicaciones.add(
                    Publicacion(
                        "Incendio Forestal",
                        "Incendio en la zona rural.",
                        "Zona Sur",
                        "31/07/2025",
                        "Incendio",
                        "Alta",
                        "Bombero",
                        R.drawable.incendio
                    )
                )
            },
            onAgregarTormenta = {
                publicaciones.add(
                    Publicacion(
                        "Tormenta Eléctrica",
                        "Fuertes lluvias con truenos.",
                        "Centro",
                        "31/07/2025",
                        "Tormenta",
                        "Media",
                        "Ciudadano",
                        R.drawable.tormenta
                    )
                )
            },
            onAgregarDeslizamiento = {
                publicaciones.add(
                    Publicacion(
                        "Deslizamiento de Tierra",
                        "Deslizamiento en la montaña cercana.",
                        "Zona Alta",
                        "31/07/2025",
                        "Deslizamiento",
                        "Alta",
                        "Alcaldía",
                        R.drawable.deslizamiento
                    )
                )
            },
            onAgregarInundacion = {
                publicaciones.add(
                    Publicacion(
                        "Inundación Urbana",
                        "Calles anegadas por fuertes lluvias.",
                        "Barrio El Prado",
                        "31/07/2025",
                        "Inundación",
                        "Alta",
                        "Defensa Civil",
                        R.drawable.inundacion
                    )
                )
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(publicaciones) { publicacion ->
                PublicacionCard(publicacion)
            }
        }
    }
}

// Botones para agregar tipos de desastre
@Composable
fun BotonesAgregarDesastre(
    onAgregarIncendio: () -> Unit,
    onAgregarTormenta: () -> Unit,
    onAgregarDeslizamiento: () -> Unit,
    onAgregarInundacion: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        DesastreButton(
            iconRes = R.drawable.ic_incendio,
            backgroundColor = Color(0xFFD32F2F),
            contentDescription = "Agregar Incendio",
            onClick = onAgregarIncendio
        )
        DesastreButton(
            iconRes = R.drawable.ic_tormenta,
            backgroundColor = Color(0xFF1976D2),
            contentDescription = "Agregar Tormenta",
            onClick = onAgregarTormenta
        )
        DesastreButton(
            iconRes = R.drawable.ic_deslizamiento,
            backgroundColor = Color(0xFF388E3C),
            contentDescription = "Agregar Deslizamiento",
            onClick = onAgregarDeslizamiento
        )
        DesastreButton(
            iconRes = R.drawable.ic_inundacion,
            backgroundColor = Color(0xFF0288D1),
            contentDescription = "Agregar Inundación",
            onClick = onAgregarInundacion
        )
    }
}

// Botón individual con ícono
@Composable
fun DesastreButton(
    iconRes: Int,
    backgroundColor: Color,
    contentDescription: String,
    onClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = backgroundColor,
        shadowElevation = 6.dp,
        tonalElevation = 6.dp,
        modifier = Modifier
            .size(72.dp)
            .padding(4.dp)
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = contentDescription,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

// Tarjeta que representa cada publicación
@Composable
fun PublicacionCard(publicacion: Publicacion) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2A2A2A), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(publicacion.titulo, color = Color.White, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Image(
            painter = painterResource(publicacion.imagenResId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text("Tipo: ${publicacion.tipo} - Severidad: ${publicacion.severidad}", color = Color(0xFFFFD500), fontSize = 14.sp)
        Text("Ubicación: ${publicacion.ubicacion}", color = Color.White, fontSize = 14.sp)
        Text("Fecha: ${publicacion.fecha}", color = Color.LightGray, fontSize = 13.sp)
        Text(publicacion.descripcion, color = Color.White, fontSize = 14.sp)
        Text("Reportado por: ${publicacion.rol}", color = Color.Gray, fontSize = 13.sp)
    }
}
