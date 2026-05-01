package com.example.mochila

import android.media.MediaPlayer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.animation.core.*
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.RepeatMode
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


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

@Composable
fun PantallaPrincipal(context: Context) {

    var sistemaActivo by remember { mutableStateOf(true) }
    var estado by remember { mutableStateOf("Seguro") }
    var bluetoothConectado by remember { mutableStateOf(false) }
    var historial by remember { mutableStateOf(listOf<String>()) }

    fun horaActual(): String {
        val sdf = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }

    fun agregarEvento(evento: String) {
        historial = listOf("${horaActual()} - $evento") + historial
    }

    fun sonidoAlerta() {
        val mediaPlayer = MediaPlayer.create(
            context,
            android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI
        )
        mediaPlayer.start()
    }


    val colorEstado = when (estado) {
        "Seguro" -> Color(0xFF4DD6A7)
        "Advertencia" -> Color(0xFFE6B450)
        "Alerta" -> Color(0xFFE85D75)
        "Desactivado" -> Color(0xFF8A8F98)
        else -> Color.White
    }


    val textoEstado = when (estado) {
        "Seguro" -> "Sistema monitoreando"
        "Advertencia" -> "Lectura sospechosa"
        "Alerta" -> "Apertura no autorizada"
        "Desactivado" -> "Sistema inactivo"
        else -> "Estado desconocido"
    }

    // 🔥 ANIMACIÓN DE ALERTA
    val infiniteTransition = rememberInfiniteTransition()
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        )
    )

    val alphaFinal = if (estado == "Alerta") alphaAnim else 1f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070A0F))
            .padding(horizontal = 22.dp)
            .padding(top = 65.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("BACK UP", color = Color.White, fontSize = 34.sp)

        Text(
            "Seguridad inteligente",
            color = Color(0xFF8B95A1),
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(25.dp))

        // 🔐 INDICADOR GENERAL
        Text(
            text = if (sistemaActivo) "Sistema activo" else "Sistema desactivado",
            color = if (sistemaActivo) Color(0xFF4DD6A7) else Color(0xFFE85D75),
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 🔵 TARJETA ESTADO (CENTRADA + ANIMADA)
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF111722)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text("ESTADO DEL SISTEMA", color = Color(0xFF7D8793), fontSize = 12.sp)

                Spacer(modifier = Modifier.height(16.dp))

                Icon(
                    imageVector = when (estado) {
                        "Seguro" -> Icons.Default.Lock
                        "Advertencia" -> Icons.Default.Warning
                        "Alerta" -> Icons.Default.Report
                        else -> Icons.Default.Lock
                    },
                    contentDescription = null,
                    tint = colorEstado.copy(alpha = alphaFinal),
                    modifier = Modifier.size(50.dp)
                )

                Text(
                    estado,
                    color = colorEstado,
                    fontSize = 38.sp
                )

                Text(
                    textoEstado,
                    color = Color(0xFFB0B7C3),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 📡 BLUETOOTH PRO
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF111722)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Conectividad", color = Color(0xFF8B95A1), fontSize = 12.sp)

                Text(
                    if (bluetoothConectado) "Conectado a BACKUP-ESP32"
                    else "Sin conexión",
                    color = if (bluetoothConectado) Color(0xFF4DD6A7) else Color(0xFFE6B450),
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                sistemaActivo = !sistemaActivo
                estado = if (!sistemaActivo) "Desactivado" else "Seguro"
                agregarEvento(if (sistemaActivo) "Sistema activado" else "Sistema desactivado")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (sistemaActivo) Color(0xFF1C222B) else Color(0xFF4DD6A7),
                contentColor = Color.White
            )
        ) {
            Text(if (sistemaActivo) "Desactivar sistema" else "Activar sistema")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                bluetoothConectado = !bluetoothConectado
                agregarEvento(if (bluetoothConectado) "Dispositivo conectado" else "Dispositivo desconectado")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1C222B),
                contentColor = Color.White
            )
        ) {
            Text("Simular conexión")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                estado = "Advertencia"
                agregarEvento("Lectura sospechosa detectada")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE6B450),
                contentColor = Color.Black
            )
        ) {
            Text("Simular advertencia")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                estado = "Alerta"
                agregarEvento("ALERTA: apertura confirmada")

                if (sistemaActivo) {
                    sonidoAlerta()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE85D75),
                contentColor = Color.White
            )
        ) {
            Text("Simular alerta")
        }

        Spacer(modifier = Modifier.height(22.dp))

        // 📊 HISTORIAL PRO
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF111722)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {

                Text("Historial de eventos", color = Color.White, fontSize = 18.sp)

                Spacer(modifier = Modifier.height(10.dp))

                if (historial.isEmpty()) {
                    Text("Sin registros", color = Color(0xFF8B95A1))
                } else {
                    historial.take(4).forEach {
                        Text("• $it", color = Color(0xFFB0B7C3), fontSize = 13.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 📚 TEXTO EDUCATIVO
        Text(
            text = "Aplicación desarrollada con fines educativos, orientada a la detección de accesos no autorizados mediante sensores y comunicación inalámbrica.",
            color = Color(0xFF68707C),
            fontSize = 11.sp
        )
    }
}