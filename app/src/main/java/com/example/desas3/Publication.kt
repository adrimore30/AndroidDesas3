package com.example.desas3

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

// Paleta de colores mejorada
val PrimaryBlue = Color(0xFF000000)
val SecondaryBlue = Color(0xFF42A5F5)
val DangerRed = Color(0xFFD32F2F)
val SuccessGreen = Color(0xFF388E3C)
val WarningAmber = Color(0xFFFFA000)
val LightBackground = Color(0xFFF8F9FA)
val CardBackground = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF212121)
val TextSecondary = Color(0xFF757575)

// Enums para mejor tipado
enum class TipoEmergencia(val displayName: String, val color: Color) {
    INCENDIO("Incendio", DangerRed),
    TORMENTA("Tormenta", PrimaryBlue),
    INUNDACION("Inundación", SecondaryBlue),
    DESLIZAMIENTO("Deslizamiento", Color(0xFF8D6E63)),
    AVALANCHA("Avalancha", Color(0xFF607D8B)),
    TERREMOTO("Terremoto", Color(0xFF795548)),
    TSUNAMI("Tsunami", Color(0xFF00838F))
}

enum class Severidad(val displayName: String, val color: Color, val priority: Int) {
    BAJA("Baja", SuccessGreen, 1),
    MEDIA("Media", WarningAmber, 2),
    ALTA("Alta", DangerRed, 3),
    CRITICA("Crítica", Color(0xFF8B0000), 4)
}

enum class EstadoEmergencia(val displayName: String, val color: Color) {
    ACTIVA("Activa", DangerRed),
    EN_PROCESO("En Proceso", WarningAmber),
    RESUELTA("Resuelta", SuccessGreen),
    CERRADA("Cerrada", TextSecondary)
}

// Modelo de publicación mejorado
data class Publicacion(
    val id: String = UUID.randomUUID().toString(),
    val titulo: String,
    val descripcion: String,
    val ubicacion: String,
    val coordenadas: String = "",
    val fecha: String,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val tipo: TipoEmergencia,
    val severidad: Severidad,
    val estado: EstadoEmergencia = EstadoEmergencia.ACTIVA,
    val rol: String,
    val contacto: String = "",
    val afectados: Int = 0,
    val imagenResId: Int,
    val tags: List<String> = emptyList()
)

// Pantalla principal mejorada
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicationScreen(navController: NavHostController) {
    val publicaciones = remember { mutableStateListOf<Publicacion>() }
    var showMenu by remember { mutableStateOf(false) }
    var showFilters by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showNewEmergencyDialog by remember { mutableStateOf(false) }
    var publicacionAEditar by remember { mutableStateOf<Publicacion?>(null) }
    var ordenamiento by remember { mutableStateOf("fecha_desc") }
    var textoBusqueda by remember { mutableStateOf("") }
    var showSuccessMessage by remember { mutableStateOf("") }
    var filtroTipo by remember { mutableStateOf<TipoEmergencia?>(null) }
    var filtroSeveridad by remember { mutableStateOf<Severidad?>(null) }
    var filtroEstado by remember { mutableStateOf<EstadoEmergencia?>(null) }

    // Inicializar con datos de ejemplo
    LaunchedEffect(Unit) {
        if (publicaciones.isEmpty()) {
            publicaciones.addAll(obtenerPublicacionesEjemplo())
        }
    }

    // Filtrar y ordenar publicaciones
    val publicacionesFiltradas = remember(publicaciones.toList(), ordenamiento, textoBusqueda, filtroTipo, filtroSeveridad, filtroEstado) {
        var resultado = publicaciones.toList()

        // Filtro por texto
        if (textoBusqueda.isNotEmpty()) {
            resultado = resultado.filter {
                it.titulo.contains(textoBusqueda, ignoreCase = true) ||
                        it.descripcion.contains(textoBusqueda, ignoreCase = true) ||
                        it.ubicacion.contains(textoBusqueda, ignoreCase = true)
            }
        }

        // Filtros específicos
        filtroTipo?.let { tipo ->
            resultado = resultado.filter { it.tipo == tipo }
        }
        filtroSeveridad?.let { severidad ->
            resultado = resultado.filter { it.severidad == severidad }
        }
        filtroEstado?.let { estado ->
            resultado = resultado.filter { it.estado == estado }
        }

        // Ordenamiento
        when (ordenamiento) {
            "fecha_desc" -> resultado.sortedByDescending { it.fechaCreacion }
            "fecha_asc" -> resultado.sortedBy { it.fechaCreacion }
            "severidad_desc" -> resultado.sortedByDescending { it.severidad.priority }
            "severidad_asc" -> resultado.sortedBy { it.severidad.priority }
            "tipo" -> resultado.sortedBy { it.tipo.displayName }
            else -> resultado
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Sistema de Emergencias",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "${publicacionesFiltradas.size} emergencias",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue
                ),
                actions = {
                    IconButton(onClick = { showFilters = true }) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_search_category_default),
                            contentDescription = "Filtros",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_sort_by_size),
                            contentDescription = "Menú",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showNewEmergencyDialog = true },
                containerColor = DangerRed,
                contentColor = Color.White
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_add),
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text("Nueva Emergencia")
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightBackground)
                    .padding(innerPadding)
            ) {
                // Barra de búsqueda
                BarraBusqueda(
                    texto = textoBusqueda,
                    onTextoChange = { textoBusqueda = it },
                    modifier = Modifier.padding(16.dp)
                )

                // Chips de filtros activos
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    filtroTipo?.let { tipo ->
                        item {
                            FilterChip(
                                selected = true,
                                onClick = { filtroTipo = null },
                                label = { Text(tipo.displayName) }
                            )
                        }
                    }
                    filtroSeveridad?.let { severidad ->
                        item {
                            FilterChip(
                                selected = true,
                                onClick = { filtroSeveridad = null },
                                label = { Text(severidad.displayName) }
                            )
                        }
                    }
                    filtroEstado?.let { estado ->
                        item {
                            FilterChip(
                                selected = true,
                                onClick = { filtroEstado = null },
                                label = { Text(estado.displayName) }
                            )
                        }
                    }
                }

                // Barra de ordenamiento
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Ordenar por:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    var showOrdenDropdown by remember { mutableStateOf(false) }
                    Box {
                        TextButton(onClick = { showOrdenDropdown = true }) {
                            Text(
                                when (ordenamiento) {
                                    "fecha_desc" -> "Más recientes"
                                    "fecha_asc" -> "Más antiguos"
                                    "severidad_desc" -> "Mayor severidad"
                                    "severidad_asc" -> "Menor severidad"
                                    "tipo" -> "Tipo"
                                    else -> "Fecha"
                                },
                                color = PrimaryBlue
                            )
                            Icon(
                                painter = painterResource(id = android.R.drawable.arrow_down_float),
                                contentDescription = null
                            )
                        }
                        DropdownMenu(
                            expanded = showOrdenDropdown,
                            onDismissRequest = { showOrdenDropdown = false }
                        ) {
                            listOf(
                                "fecha_desc" to "Más recientes",
                                "fecha_asc" to "Más antiguos",
                                "severidad_desc" to "Mayor severidad",
                                "severidad_asc" to "Menor severidad",
                                "tipo" to "Por tipo"
                            ).forEach { (value, label) ->
                                DropdownMenuItem(
                                    text = { Text(label) },
                                    onClick = {
                                        ordenamiento = value
                                        showOrdenDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Lista de publicaciones
                if (publicacionesFiltradas.isEmpty()) {
                    EmptyState(
                        mensaje = if (textoBusqueda.isNotEmpty() || filtroTipo != null || filtroSeveridad != null || filtroEstado != null)
                            "No se encontraron emergencias con los filtros aplicados"
                        else "No hay emergencias registradas",
                        onAction = { showNewEmergencyDialog = true }
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(publicacionesFiltradas, key = { it.id }) { publicacion ->
                            PublicacionCardMejorada(
                                publicacion = publicacion,
                                onEdit = {
                                    publicacionAEditar = it
                                    showEditDialog = true
                                },
                                onDelete = {
                                    publicaciones.remove(it)
                                    showSuccessMessage = "Emergencia eliminada"
                                },
                                onEstadoChange = { pub, estado ->
                                    val index = publicaciones.indexOfFirst { it.id == pub.id }
                                    if (index != -1) {
                                        publicaciones[index] = pub.copy(estado = estado)
                                        showSuccessMessage = "Estado actualizado a ${estado.displayName}"
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    )

    // Mensaje de éxito
    if (showSuccessMessage.isNotEmpty()) {
        LaunchedEffect(showSuccessMessage) {
            delay(3000)
            showSuccessMessage = ""
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SuccessGreen)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        showSuccessMessage,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                    TextButton(onClick = { showSuccessMessage = "" }) {
                        Text("OK", color = Color.White)
                    }
                }
            }
        }
    }

    // Diálogos
    if (showEditDialog && publicacionAEditar != null) {
        EditarPublicacionDialogMejorado(
            publicacion = publicacionAEditar!!,
            onDismiss = {
                showEditDialog = false
                publicacionAEditar = null
            },
            onConfirm = { publicacionEditada ->
                val index = publicaciones.indexOfFirst { it.id == publicacionEditada.id }
                if (index != -1) {
                    publicaciones[index] = publicacionEditada
                    showSuccessMessage = "Emergencia actualizada"
                }
                showEditDialog = false
                publicacionAEditar = null
            }
        )
    }

    if (showNewEmergencyDialog) {
        NuevaEmergenciaDialog(
            onDismiss = { showNewEmergencyDialog = false },
            onConfirm = { nuevaPublicacion ->
                publicaciones.add(0, nuevaPublicacion)
                showNewEmergencyDialog = false
                showSuccessMessage = "Nueva emergencia creada"
            }
        )
    }

    if (showFilters) {
        FiltrosDialog(
            filtroTipo = filtroTipo,
            filtroSeveridad = filtroSeveridad,
            filtroEstado = filtroEstado,
            onTipoChange = { filtroTipo = it },
            onSeveridadChange = { filtroSeveridad = it },
            onEstadoChange = { filtroEstado = it },
            onDismiss = { showFilters = false }
        )
    }

    // Menú principal
    if (showMenu) {
        MenuPrincipal(
            expanded = showMenu,
            onDismiss = { showMenu = false },
            navController = navController
        )
    }
}

// Barra de búsqueda
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraBusqueda(
    texto: String,
    onTextoChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = texto,
        onValueChange = onTextoChange,
        placeholder = { Text("Buscar emergencias...") },
        leadingIcon = {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_search),
                contentDescription = "Buscar"
            )
        },
        trailingIcon = {
            if (texto.isNotEmpty()) {
                IconButton(onClick = { onTextoChange("") }) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                        contentDescription = "Limpiar"
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryBlue,
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f)
        ),
        modifier = modifier.fillMaxWidth()
    )
}

// Tarjeta mejorada de publicación
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicacionCardMejorada(
    publicacion: Publicacion,
    onEdit: (Publicacion) -> Unit,
    onDelete: (Publicacion) -> Unit,
    onEstadoChange: (Publicacion, EstadoEmergencia) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEstadoMenu by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = CardBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header con título y acciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Indicador de tipo
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(publicacion.tipo.color, CircleShape)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            publicacion.titulo,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = TextPrimary
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    // Badges de estado y severidad
                    Row {
                        Surface(
                            color = publicacion.estado.color,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(
                                publicacion.estado.displayName,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            color = publicacion.severidad.color,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                publicacion.severidad.displayName,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Menú de acciones
                Box {
                    IconButton(onClick = { showEstadoMenu = true }) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_more),
                            contentDescription = "Acciones"
                        )
                    }
                    DropdownMenu(
                        expanded = showEstadoMenu,
                        onDismissRequest = { showEstadoMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar") },
                            onClick = {
                                showEstadoMenu = false
                                onEdit(publicacion)
                            }
                        )

                        EstadoEmergencia.values().forEach { estado ->
                            if (estado != publicacion.estado) {
                                DropdownMenuItem(
                                    text = { Text("Marcar como ${estado.displayName}") },
                                    onClick = {
                                        showEstadoMenu = false
                                        onEstadoChange(publicacion, estado)
                                    }
                                )
                            }
                        }

                        Divider()

                        DropdownMenuItem(
                            text = { Text("Eliminar", color = DangerRed) },
                            onClick = {
                                showEstadoMenu = false
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Imagen
            Image(
                painter = painterResource(publicacion.imagenResId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(12.dp))

            // Información detallada
            InfoRow("📍", publicacion.ubicacion)
            if (publicacion.coordenadas.isNotEmpty()) {
                InfoRow("🎯", publicacion.coordenadas)
            }
            InfoRow("📅", publicacion.fecha)
            if (publicacion.afectados > 0) {
                InfoRow("👥", "${publicacion.afectados} personas afectadas")
            }
            if (publicacion.contacto.isNotEmpty()) {
                InfoRow("📞", publicacion.contacto)
            }

            Spacer(Modifier.height(8.dp))

            Text(
                publicacion.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Reportado por: ${publicacion.rol}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Text(
                    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        .format(Date(publicacion.fechaCreacion)),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }

    // Diálogo de confirmación para eliminar
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar eliminación") },
            text = {
                Text("¿Estás seguro de que deseas eliminar esta emergencia?\n\n\"${publicacion.titulo}\"")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(publicacion)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// Componente para mostrar información en fila
@Composable
fun InfoRow(emoji: String, texto: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text(
            emoji,
            fontSize = 14.sp
        )
        Spacer(Modifier.width(8.dp))
        Text(
            texto,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

// Estado vacío
@Composable
fun EmptyState(mensaje: String, onAction: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_search),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = TextSecondary
        )
        Spacer(Modifier.height(16.dp))
        Text(
            mensaje,
            style = MaterialTheme.typography.titleMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onAction,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Text("Crear Nueva Emergencia")
        }
    }
}

// Diálogo para nueva emergencia
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaEmergenciaDialog(
    onDismiss: () -> Unit,
    onConfirm: (Publicacion) -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var coordenadas by remember { mutableStateOf("") }
    var contacto by remember { mutableStateOf("") }
    var afectados by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf(TipoEmergencia.INCENDIO) }
    var severidad by remember { mutableStateOf(Severidad.MEDIA) }
    var rol by remember { mutableStateOf("Ciudadano") }

    var showTipoDropdown by remember { mutableStateOf(false) }
    var showSeveridadDropdown by remember { mutableStateOf(false) }
    var showRolDropdown by remember { mutableStateOf(false) }

    val roles = listOf("Ciudadano", "Bombero", "Policía", "Defensa Civil", "Cruz Roja", "Alcaldía", "Administrador")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Text(
                    "Nueva Emergencia",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título de la emergencia") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = titulo.isEmpty()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción detallada") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    isError = descripcion.isEmpty()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = ubicacion,
                    onValueChange = { ubicacion = it },
                    label = { Text("Ubicación") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = ubicacion.isEmpty()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = coordenadas,
                    onValueChange = { coordenadas = it },
                    label = { Text("Coordenadas (opcional)") },
                    placeholder = { Text("Ej: -12.0464, -77.0428") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = contacto,
                    onValueChange = { contacto = it },
                    label = { Text("Teléfono de contacto (opcional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = afectados,
                    onValueChange = { if (it.all { char -> char.isDigit() }) afectados = it },
                    label = { Text("Personas afectadas (opcional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // Tipo de emergencia
                Box {
                    OutlinedTextField(
                        value = tipo.displayName,
                        onValueChange = { },
                        label = { Text("Tipo de emergencia") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showTipoDropdown = true },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = android.R.drawable.arrow_down_float),
                                contentDescription = "Dropdown"
                            )
                        }
                    )

                    DropdownMenu(
                        expanded = showTipoDropdown,
                        onDismissRequest = { showTipoDropdown = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TipoEmergencia.values().forEach { tipoOption ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .background(tipoOption.color, CircleShape)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(tipoOption.displayName)
                                    }
                                },
                                onClick = {
                                    tipo = tipoOption
                                    showTipoDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Severidad
                Box {
                    OutlinedTextField(
                        value = severidad.displayName,
                        onValueChange = { },
                        label = { Text("Severidad") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showSeveridadDropdown = true },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = android.R.drawable.arrow_down_float),
                                contentDescription = "Dropdown"
                            )
                        }
                    )

                    DropdownMenu(
                        expanded = showSeveridadDropdown,
                        onDismissRequest = { showSeveridadDropdown = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Severidad.values().forEach { severidadOption ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .background(severidadOption.color, CircleShape)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            severidadOption.displayName,
                                            color = severidadOption.color,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                },
                                onClick = {
                                    severidad = severidadOption
                                    showSeveridadDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Rol
                Box {
                    OutlinedTextField(
                        value = rol,
                        onValueChange = { },
                        label = { Text("Tu rol/cargo") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showRolDropdown = true },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = android.R.drawable.arrow_down_float),
                                contentDescription = "Dropdown"
                            )
                        }
                    )

                    DropdownMenu(
                        expanded = showRolDropdown,
                        onDismissRequest = { showRolDropdown = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        roles.forEach { rolOption ->
                            DropdownMenuItem(
                                text = { Text(rolOption) },
                                onClick = {
                                    rol = rolOption
                                    showRolDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar", color = TextSecondary)
                    }

                    Button(
                        onClick = {
                            if (titulo.isNotEmpty() && descripcion.isNotEmpty() && ubicacion.isNotEmpty()) {
                                val nuevaPublicacion = Publicacion(
                                    titulo = titulo,
                                    descripcion = descripcion,
                                    ubicacion = ubicacion,
                                    coordenadas = coordenadas,
                                    fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
                                    tipo = tipo,
                                    severidad = severidad,
                                    rol = rol,
                                    contacto = contacto,
                                    afectados = afectados.toIntOrNull() ?: 0,
                                    imagenResId = obtenerImagenPorTipo(tipo)
                                )
                                onConfirm(nuevaPublicacion)
                            }
                        },
                        enabled = titulo.isNotEmpty() && descripcion.isNotEmpty() && ubicacion.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Crear Emergencia")
                    }
                }
            }
        }
    }
}

// Diálogo de filtros
@Composable
fun FiltrosDialog(
    filtroTipo: TipoEmergencia?,
    filtroSeveridad: Severidad?,
    filtroEstado: EstadoEmergencia?,
    onTipoChange: (TipoEmergencia?) -> Unit,
    onSeveridadChange: (Severidad?) -> Unit,
    onEstadoChange: (EstadoEmergencia?) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    "Filtros",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary
                )

                Spacer(Modifier.height(16.dp))

                // Filtro por tipo
                Text(
                    "Tipo de Emergencia",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(TipoEmergencia.values()) { tipo ->
                        FilterChip(
                            selected = filtroTipo == tipo,
                            onClick = {
                                onTipoChange(if (filtroTipo == tipo) null else tipo)
                            },
                            label = { Text(tipo.displayName) },
                            leadingIcon = if (filtroTipo == tipo) {
                                {
                                    Icon(
                                        painter = painterResource(id = android.R.drawable.checkbox_on_background),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            } else null
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Filtro por severidad
                Text(
                    "Severidad",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Severidad.values().forEach { sev ->
                        FilterChip(
                            selected = filtroSeveridad == sev,
                            onClick = {
                                onSeveridadChange(if (filtroSeveridad == sev) null else sev)
                            },
                            label = {
                                Text(
                                    sev.displayName,
                                    color = if (filtroSeveridad == sev) Color.White else sev.color
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = sev.color
                            )
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Filtro por estado
                Text(
                    "Estado",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EstadoEmergencia.values().forEach { estado ->
                        FilterChip(
                            selected = filtroEstado == estado,
                            onClick = {
                                onEstadoChange(if (filtroEstado == estado) null else estado)
                            },
                            label = {
                                Text(
                                    estado.displayName,
                                    color = if (filtroEstado == estado) Color.White else estado.color
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = estado.color
                            )
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(
                        onClick = {
                            onTipoChange(null)
                            onSeveridadChange(null)
                            onEstadoChange(null)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Limpiar Filtros")
                    }

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Aplicar")
                    }
                }
            }
        }
    }
}

// Diálogo de edición mejorado
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPublicacionDialogMejorado(
    publicacion: Publicacion,
    onDismiss: () -> Unit,
    onConfirm: (Publicacion) -> Unit
) {
    var titulo by remember { mutableStateOf(publicacion.titulo) }
    var descripcion by remember { mutableStateOf(publicacion.descripcion) }
    var ubicacion by remember { mutableStateOf(publicacion.ubicacion) }
    var coordenadas by remember { mutableStateOf(publicacion.coordenadas) }
    var contacto by remember { mutableStateOf(publicacion.contacto) }
    var afectados by remember { mutableStateOf(publicacion.afectados.toString()) }
    var tipo by remember { mutableStateOf(publicacion.tipo) }
    var severidad by remember { mutableStateOf(publicacion.severidad) }
    var estado by remember { mutableStateOf(publicacion.estado) }

    var showTipoDropdown by remember { mutableStateOf(false) }
    var showSeveridadDropdown by remember { mutableStateOf(false) }
    var showEstadoDropdown by remember { mutableStateOf(false) }

    Dialog(onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Text(
                    "Editar Emergencia",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = ubicacion,
                    onValueChange = { ubicacion = it },
                    label = { Text("Ubicación") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = coordenadas,
                    onValueChange = { coordenadas = it },
                    label = { Text("Coordenadas") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = contacto,
                    onValueChange = { contacto = it },
                    label = { Text("Contacto") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = afectados,
                    onValueChange = { if (it.all { char -> char.isDigit() || char == ' ' }) afectados = it },
                    label = { Text("Personas afectadas") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // Dropdowns para tipo, severidad y estado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Tipo
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = tipo.displayName,
                            onValueChange = { },
                            label = { Text("Tipo") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showTipoDropdown = true },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(id = android.R.drawable.arrow_down_float),
                                    contentDescription = "Dropdown"
                                )
                            }
                        )

                        DropdownMenu(
                            expanded = showTipoDropdown,
                            onDismissRequest = { showTipoDropdown = false }
                        ) {
                            TipoEmergencia.values().forEach { tipoOption ->
                                DropdownMenuItem(
                                    text = { Text(tipoOption.displayName) },
                                    onClick = {
                                        tipo = tipoOption
                                        showTipoDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    // Severidad
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = severidad.displayName,
                            onValueChange = { },
                            label = { Text("Severidad") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showSeveridadDropdown = true },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(id = android.R.drawable.arrow_down_float),
                                    contentDescription = "Dropdown"
                                )
                            }
                        )

                        DropdownMenu(
                            expanded = showSeveridadDropdown,
                            onDismissRequest = { showSeveridadDropdown = false }
                        ) {
                            Severidad.values().forEach { severidadOption ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            severidadOption.displayName,
                                            color = severidadOption.color,
                                            fontWeight = FontWeight.Medium
                                        )
                                    },
                                    onClick = {
                                        severidad = severidadOption
                                        showSeveridadDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Estado
                Box {
                    OutlinedTextField(
                        value = estado.displayName,
                        onValueChange = { },
                        label = { Text("Estado de la emergencia") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showEstadoDropdown = true },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = android.R.drawable.arrow_down_float),
                                contentDescription = "Dropdown"
                            )
                        }
                    )

                    DropdownMenu(
                        expanded = showEstadoDropdown,
                        onDismissRequest = { showEstadoDropdown = false }
                    ) {
                        EstadoEmergencia.values().forEach { estadoOption ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        estadoOption.displayName,
                                        color = estadoOption.color,
                                        fontWeight = FontWeight.Medium
                                    )
                                },
                                onClick = {
                                    estado = estadoOption
                                    showEstadoDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            val publicacionEditada = publicacion.copy(
                                titulo = titulo,
                                descripcion = descripcion,
                                ubicacion = ubicacion,
                                coordenadas = coordenadas,
                                contacto = contacto,
                                afectados = afectados.toIntOrNull() ?: 0,
                                tipo = tipo,
                                severidad = severidad,
                                estado = estado
                            )
                            onConfirm(publicacionEditada)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Guardar Cambios")
                    }
                }
            }
        }
    }
}

// Menú principal
@Composable
fun MenuPrincipal(
    expanded: Boolean,
    onDismiss: () -> Unit,
    navController: NavHostController
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier
            .background(Color.White)
            .width(280.dp)
    ) {
        Text(
            "NAVEGACIÓN",
            color = PrimaryBlue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        DropdownMenuItem(
            text = { Text("🏠 Inicio", color = TextPrimary) },
            onClick = {
                onDismiss()
                navController.navigate("home")
            }
        )

        DropdownMenuItem(
            text = { Text("💬 Chat de Emergencias", color = TextPrimary) },
            onClick = {
                onDismiss()
                navController.navigate("chat")
            }
        )

        DropdownMenuItem(
            text = { Text("📋 Reportar Emergencia", color = DangerRed, fontWeight = FontWeight.Bold) },
            onClick = {
                onDismiss()
                navController.navigate("reportar")
            }
        )

        Divider(color = PrimaryBlue.copy(alpha = 0.2f))

        Text(
            "ORGANISMOS",
            color = PrimaryBlue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        listOf(
            "🚒 Bomberos",
            "🚑 Defensa Civil",
            "⛑️ Cruz Roja",
            "🏛️ ONEMI",
            "🏢 Alcaldía"
        ).forEach { organismo ->
            DropdownMenuItem(
                text = { Text(organismo, color = TextPrimary) },
                onClick = { onDismiss() }
            )
        }

        Divider(color = PrimaryBlue.copy(alpha = 0.2f))

        Text(
            "CUENTA",
            color = PrimaryBlue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        DropdownMenuItem(
            text = { Text("👤 Ver Perfil", color = TextPrimary) },
            onClick = {
                onDismiss()
                navController.navigate("perfil")
            }
        )

        DropdownMenuItem(
            text = { Text("⚙️ Configuración", color = TextPrimary) },
            onClick = { onDismiss() }
        )

        DropdownMenuItem(
            text = { Text("🚪 Cerrar Sesión", color = DangerRed, fontWeight = FontWeight.Bold) },
            onClick = {
                onDismiss()
                navController.navigate("login")
            }
        )
    }
}

// Funciones auxiliares
fun obtenerPublicacionesEjemplo(): List<Publicacion> {
    return listOf(
        Publicacion(
            titulo = "Incendio Forestal Activo",
            descripcion = "Gran incendio forestal en expansión. Evacuación inmediata requerida en sectores aledaños.",
            ubicacion = "Cerro San Cristóbal, Santiago",
            coordenadas = "-33.4269, -70.6344",
            fecha = "14/08/2025",
            tipo = TipoEmergencia.INCENDIO,
            severidad = Severidad.CRITICA,
            estado = EstadoEmergencia.ACTIVA,
            rol = "Bomberos",
            contacto = "+56 9 1234 5678",
            afectados = 150,
            imagenResId = R.drawable.incendio,
            tags = listOf("evacuación", "humo", "viento-fuerte")
        ),
        Publicacion(
            titulo = "Inundación Urbana",
            descripcion = "Calles anegadas por desborde del río. Tráfico interrumpido en varios sectores.",
            ubicacion = "Avenida Providencia con Manuel Montt",
            coordenadas = "-33.4372, -70.6054",
            fecha = "14/08/2025",
            tipo = TipoEmergencia.INUNDACION,
            severidad = Severidad.ALTA,
            estado = EstadoEmergencia.EN_PROCESO,
            rol = "Defensa Civil",
            contacto = "+56 9 8765 4321",
            afectados = 80,
            imagenResId = R.drawable.inundacion,
            tags = listOf("lluvia", "desborde", "transito")
        ),
        Publicacion(
            titulo = "Deslizamiento de Tierra",
            descripcion = "Deslizamiento menor en ladera de cerro. Ruta cortada preventivamente.",
            ubicacion = "Camino a Farellones, Km 12",
            coordenadas = "-33.3506, -70.3436",
            fecha = "13/08/2025",
            tipo = TipoEmergencia.DESLIZAMIENTO,
            severidad = Severidad.MEDIA,
            estado = EstadoEmergencia.RESUELTA,
            rol = "Ciudadano",
            contacto = "+56 9 5555 1234",
            afectados = 5,
            imagenResId = R.drawable.deslizamiento,
            tags = listOf("lluvia", "preventivo")
        )
    )
}

fun obtenerImagenPorTipo(tipo: TipoEmergencia): Int {
    return when (tipo) {
        TipoEmergencia.INCENDIO -> R.drawable.incendio
        TipoEmergencia.TORMENTA -> R.drawable.tormenta
        TipoEmergencia.INUNDACION -> R.drawable.inundacion
        TipoEmergencia.DESLIZAMIENTO -> R.drawable.deslizamiento
        TipoEmergencia.AVALANCHA -> R.drawable.deslizamiento // Usar la misma imagen como placeholder
        TipoEmergencia.TERREMOTO -> R.drawable.deslizamiento // Usar la misma imagen como placeholder
        TipoEmergencia.TSUNAMI -> R.drawable.inundacion // Usar la misma imagen como placeholder
    }
}