package com.example.mochilaseguridad

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
    var historial by remember { mutableStateOf(listOf<String>()) }

    fun agregarEvento(evento: String) {
        historial = listOf(evento) + historial
    }

    fun vibrar() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    fun notificar() {
        val builder = NotificationCompat.Builder(context, "alertas")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("🚨 Alerta de Robo")
            .setContentText("Se detectó apertura sospechosa en la mochila")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    }



    fun procesarDatoBluetooth(dato: String) {
        when (dato) {
            "0" -> {
                estado = "Seguro"
                agregarEvento("Lectura normal desde sensores")
            }
            "1" -> {
                estado = "Advertencia"
                agregarEvento("Sensor magnético activado")
            }
            "2" -> {
                estado = "Advertencia"
                agregarEvento("Sensor óptico activado")
            }
            "3" -> {
                estado = "Alerta"
                agregarEvento("Apertura confirmada por ambos sensores")

                if (sistemaActivo) {
                    vibrar()
                    notificar()
                }
            }
        }
    }







    val colorEstado = when (estado) {
        "Seguro" -> Color(0xFF4CAF50)
        "Advertencia" -> Color(0xFFFFC107)
        "Alerta" -> Color(0xFFF44336)
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text("Mochila Segura", color = Color.White, fontSize = 26.sp)

            Spacer(modifier = Modifier.height(30.dp))

            Text(estado, color = colorEstado, fontSize = 40.sp)

            Spacer(modifier = Modifier.height(30.dp))

            Button(onClick = {
                sistemaActivo = !sistemaActivo
                estado = if (!sistemaActivo) "Desactivado" else "Seguro"
                agregarEvento("Sistema ${if (sistemaActivo) "Activado" else "Desactivado"}")
            }) {
                Text(if (sistemaActivo) "Desactivar" else "Activar")
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 🔧 SIMULACIÓN

            Button(onClick = {
                estado = "Advertencia"
                agregarEvento("Advertencia: 1 sensor activado")
            }) {
                Text("Simular 1 sensor")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                estado = "Alerta"
                agregarEvento("ALERTA: posible robo")

                if (sistemaActivo) {
                    vibrar()
                    notificar()
                }
            }) {
                Text("Simular robo")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                estado = "alerta"
                agregarEvento("Estado seguro")
            }) {
                Text("Reset")
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text("Historial", color = Color.White)

            historial.take(5).forEach {
                Text(it, color = Color.LightGray, fontSize = 12.sp)
            }
        }
    }
}