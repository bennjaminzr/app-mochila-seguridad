package com.example.mochila

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.animation.core.*
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Paleta de colores centralizada ───────────────────────────────────────────
private object AppColors {
    val background    = Color(0xFF070A0F)
    val surface       = Color(0xFF0E1420)
    val surfaceBorder = Color(0xFF1C2333)
    val textPrimary   = Color(0xFFE8EDF4)
    val textSecondary = Color(0xFF6B7585)
    val textMuted     = Color(0xFF3D4655)
    val green         = Color(0xFF2DD4A0)
    val greenDim      = Color(0xFF1A5C45)
    val amber         = Color(0xFFF0B429)
    val amberDim      = Color(0xFF5C450D)
    val red           = Color(0xFFEF4B6C)
    val redDim        = Color(0xFF5C1525)
    val blue          = Color(0xFF4F8EF7)
    val inactive      = Color(0xFF2A3040)
}

// ─── Activity principal ───────────────────────────────────────────────────────
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crearCanalNotificaciones()
        setContent {
            PantallaPrincipal(this)
        }
    }

    private fun crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "alertas",
                "Alertas de seguridad",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}

// ─── Pantalla Acerca ──────────────────────────────────────────────────────────
@Composable
fun PantallaAcerca(onVolver: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 60.dp, bottom = 40.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onVolver,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AppColors.surface)
                    .border(1.dp, AppColors.surfaceBorder, CircleShape)
            ) {
                // ✅ FIX 1: AutoMirrored reemplaza el deprecated Icons.Filled.ArrowBack
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = AppColors.textPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Text(
                "Acerca del proyecto",
                color = AppColors.textPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(36.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(AppColors.surface)
                .border(1.dp, AppColors.surfaceBorder, RoundedCornerShape(16.dp))
                .padding(30.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(AppColors.greenDim, AppColors.background)
                            )
                        )
                        .border(1.dp, AppColors.green.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Shield,
                        contentDescription = null,
                        tint = AppColors.green,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    "BACK UP",
                    color = AppColors.textPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Sistema de seguridad inteligente",
                    color = AppColors.textSecondary,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        InfoCard(
            titulo = "Descripción",
            contenido = "BACK UP es una aplicación educativa orientada a la detección de accesos no autorizados en mochilas, mediante sensores físicos conectados a un microcontrolador ESP32 vía Bluetooth."
        )

        Spacer(Modifier.height(12.dp))

        InfoCard(
            titulo = "Arquitectura del sistema",
            contenido = "El sistema combina múltiples lecturas de sensores para reducir falsos positivos. La lógica de detección evalúa umbrales antes de escalar el nivel de alerta, priorizando la confiabilidad."
        )

        Spacer(Modifier.height(12.dp))

        SectionCard(titulo = "Stack tecnológico") {
            listOf(
                "Kotlin + Jetpack Compose" to "Interfaz nativa Android",
                "ESP32 (Bluetooth)"        to "Microcontrolador IoT",
                "Sensores de apertura"     to "Detección física",
                "Material Design 3"        to "Sistema de diseño"
            ).forEachIndexed { index, (tech, desc) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(AppColors.green)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(tech, color = AppColors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Text(desc, color = AppColors.textSecondary, fontSize = 12.sp)
                    }
                }
                // ✅ FIX 2: HorizontalDivider reemplaza el deprecated Divider
                if (index < 3) {
                    HorizontalDivider(color = AppColors.surfaceBorder, thickness = 0.5.dp)
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        Text(
            "Proyecto académico — Uso educativo",
            color = AppColors.textMuted,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun InfoCard(titulo: String, contenido: String) {
    SectionCard(titulo = titulo) {
        Text(contenido, color = AppColors.textSecondary, fontSize = 14.sp, lineHeight = 22.sp)
    }
}

@Composable
private fun SectionCard(titulo: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(AppColors.surface)
            .border(1.dp, AppColors.surfaceBorder, RoundedCornerShape(14.dp))
            .padding(18.dp)
    ) {
        Text(
            titulo.uppercase(),
            color = AppColors.textSecondary,
            fontSize = 11.sp,
            letterSpacing = 1.5.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(12.dp))
        content()
    }
}

// ─── Pantalla Principal ───────────────────────────────────────────────────────
@Composable
fun PantallaPrincipal(context: Context) {

    var sistemaActivo      by remember { mutableStateOf(true) }
    var estado             by remember { mutableStateOf("Seguro") }
    var bluetoothConectado by remember { mutableStateOf(false) }
    var historial          by remember { mutableStateOf(listOf<String>()) }
    var mostrarAcerca      by remember { mutableStateOf(false) }

    // ── MediaPlayer como estado para controlarlo globalmente ─────────────────
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    fun detenerSonido() {
        mediaPlayer?.let {
            runCatching { if (it.isPlaying) it.stop() }
            it.release()
        }
        mediaPlayer = null
    }

    fun iniciarSonidoAlerta() {
        detenerSonido()
        runCatching {
            val uri = android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI
            val mp = MediaPlayer().apply {
                // ✅ FIX 3: setAudioAttributes reemplaza el deprecated setAudioStreamType
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                setDataSource(context, uri)
                isLooping = true
                prepare()
                start()
            }
            mediaPlayer = mp
        }
    }

    // Liberar recursos al destruir el composable
    DisposableEffect(Unit) {
        onDispose { detenerSonido() }
    }

    fun horaActual(): String {
        val sdf = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }

    fun agregarEvento(evento: String) {
        historial = listOf("${horaActual()}  $evento") + historial
    }

    fun cambiarEstado(nuevoEstado: String, evento: String) {
        estado = nuevoEstado
        agregarEvento(evento)
        // ✅ FIX BUG SONIDO: se detiene correctamente al salir del estado Alerta
        if (nuevoEstado == "Alerta" && sistemaActivo) {
            iniciarSonidoAlerta()
        } else {
            detenerSonido()
        }
    }

    if (mostrarAcerca) {
        PantallaAcerca(onVolver = { mostrarAcerca = false })
        return
    }

    val colorEstado = when (estado) {
        "Seguro"      -> AppColors.green
        "Advertencia" -> AppColors.amber
        "Alerta"      -> AppColors.red
        else          -> AppColors.textSecondary
    }
    val colorEstadoDim = when (estado) {
        "Seguro"      -> AppColors.greenDim
        "Advertencia" -> AppColors.amberDim
        "Alerta"      -> AppColors.redDim
        else          -> AppColors.inactive
    }

    val infiniteTransition = rememberInfiniteTransition(label = "alerta")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "pulseAlpha"
    )
    val iconAlpha = if (estado == "Alerta") pulseAlpha else 1f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 60.dp, bottom = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ── Top bar ───────────────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "BACK UP",
                    color = AppColors.textPrimary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp
                )
                Text(
                    "Seguridad inteligente",
                    color = AppColors.textSecondary,
                    fontSize = 12.sp
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (sistemaActivo) AppColors.greenDim else AppColors.inactive)
                    .border(
                        1.dp,
                        if (sistemaActivo) AppColors.green.copy(0.4f) else AppColors.surfaceBorder,
                        RoundedCornerShape(50)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (sistemaActivo) AppColors.green else AppColors.textMuted)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        if (sistemaActivo) "Activo" else "Inactivo",
                        color = if (sistemaActivo) AppColors.green else AppColors.textMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        // ── Tarjeta de estado principal ───────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(AppColors.surface)
                .border(
                    1.dp,
                    colorEstado.copy(alpha = if (estado == "Alerta") iconAlpha * 0.6f else 0.25f),
                    RoundedCornerShape(20.dp)
                )
                .padding(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "ESTADO DEL SISTEMA",
                    color = AppColors.textSecondary,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(colorEstadoDim)
                        .border(1.dp, colorEstado.copy(alpha = iconAlpha * 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (estado) {
                            "Seguro"      -> Icons.Default.Lock
                            "Advertencia" -> Icons.Default.Warning
                            "Alerta"      -> Icons.Default.Report
                            else          -> Icons.Default.LockOpen
                        },
                        contentDescription = null,
                        tint = colorEstado.copy(alpha = iconAlpha),
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    estado,
                    color = colorEstado,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    when (estado) {
                        "Seguro"      -> "Todos los sensores en reposo"
                        "Advertencia" -> "Lectura sospechosa detectada"
                        "Alerta"      -> "Apertura no autorizada confirmada"
                        else          -> "Monitoreo detenido"
                    },
                    color = AppColors.textSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        // ── Conectividad Bluetooth ────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(AppColors.surface)
                .border(1.dp, AppColors.surfaceBorder, RoundedCornerShape(14.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Bluetooth,
                    contentDescription = null,
                    tint = if (bluetoothConectado) AppColors.blue else AppColors.textSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        "Dispositivo ESP32",
                        color = AppColors.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        if (bluetoothConectado) "BACKUP-ESP32 · Conectado" else "Sin conexión",
                        color = if (bluetoothConectado) AppColors.green else AppColors.textSecondary,
                        fontSize = 12.sp
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (bluetoothConectado) AppColors.green else AppColors.textMuted)
            )
        }

        Spacer(Modifier.height(20.dp))

        // ── Controles ─────────────────────────────────────────────────────────
        Text(
            "CONTROLES",
            color = AppColors.textSecondary,
            fontSize = 11.sp,
            letterSpacing = 2.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        AppButton(
            label = if (sistemaActivo) "Desactivar sistema" else "Activar sistema",
            icon = Icons.Default.PowerSettingsNew,
            containerColor = if (sistemaActivo) AppColors.surface else AppColors.greenDim,
            contentColor = if (sistemaActivo) AppColors.red else AppColors.green,
            borderColor = if (sistemaActivo) AppColors.red.copy(0.3f) else AppColors.green.copy(0.3f)
        ) {
            sistemaActivo = !sistemaActivo
            if (!sistemaActivo) cambiarEstado("Desactivado", "Sistema desactivado")
            else                cambiarEstado("Seguro", "Sistema activado")
        }

        Spacer(Modifier.height(8.dp))

        AppButton(
            label = if (bluetoothConectado) "Desconectar dispositivo" else "Conectar dispositivo",
            icon = Icons.Default.Bluetooth,
            containerColor = AppColors.surface,
            contentColor = AppColors.blue,
            borderColor = AppColors.blue.copy(0.25f)
        ) {
            bluetoothConectado = !bluetoothConectado
            agregarEvento(if (bluetoothConectado) "Dispositivo conectado" else "Dispositivo desconectado")
        }

        Spacer(Modifier.height(8.dp))

        AppButton(
            label = "Simular advertencia",
            icon = Icons.Default.Warning,
            containerColor = AppColors.amberDim,
            contentColor = AppColors.amber,
            borderColor = AppColors.amber.copy(0.3f)
        ) {
            if (sistemaActivo) cambiarEstado("Advertencia", "Lectura sospechosa detectada")
        }

        Spacer(Modifier.height(8.dp))

        AppButton(
            label = "Simular alerta",
            icon = Icons.Default.Report,
            containerColor = AppColors.redDim,
            contentColor = AppColors.red,
            borderColor = AppColors.red.copy(0.3f)
        ) {
            if (sistemaActivo) cambiarEstado("Alerta", "ALERTA: apertura no autorizada confirmada")
        }

        Spacer(Modifier.height(8.dp))

        AppButton(
            label = "Restablecer estado",
            icon = Icons.Default.Refresh,
            containerColor = AppColors.surface,
            contentColor = AppColors.textSecondary,
            borderColor = AppColors.surfaceBorder
        ) {
            if (sistemaActivo) cambiarEstado("Seguro", "Estado restablecido manualmente")
        }

        Spacer(Modifier.height(22.dp))

        // ── Historial de eventos ──────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(AppColors.surface)
                .border(1.dp, AppColors.surfaceBorder, RoundedCornerShape(14.dp))
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "HISTORIAL DE EVENTOS",
                    color = AppColors.textSecondary,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Medium
                )
                if (historial.isNotEmpty()) {
                    Text(
                        "${historial.size} eventos",
                        color = AppColors.textMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            if (historial.isEmpty()) {
                Text("Sin registros aún", color = AppColors.textMuted, fontSize = 13.sp)
            } else {
                historial.take(5).forEachIndexed { index, evento ->
                    val partes = evento.split("  ", limit = 2)
                    val hora   = if (partes.size == 2) partes[0] else "—"
                    val msg    = if (partes.size == 2) partes[1] else evento

                    val colorEvento = when {
                        "ALERTA"     in msg                       -> AppColors.red
                        "sospechosa" in msg                       -> AppColors.amber
                        "conectado"  in msg || "activado" in msg  -> AppColors.green
                        else                                      -> AppColors.textSecondary
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(colorEvento)
                        )
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(msg, color = AppColors.textPrimary, fontSize = 13.sp)
                            Text(hora, color = AppColors.textMuted, fontSize = 11.sp)
                        }
                    }

                    // ✅ FIX 4: HorizontalDivider reemplaza el deprecated Divider
                    if (index < minOf(historial.size, 5) - 1) {
                        HorizontalDivider(
                            color = AppColors.surfaceBorder,
                            thickness = 0.5.dp,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        TextButton(onClick = { mostrarAcerca = true }) {
            Text("Acerca del proyecto", color = AppColors.textSecondary, fontSize = 13.sp)
        }

        Text(
            "Proyecto educativo · Detección de accesos no autorizados",
            color = AppColors.textMuted,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}

// ─── Componente de botón reutilizable ─────────────────────────────────────────
@Composable
private fun AppButton(
    label: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(10.dp))
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
