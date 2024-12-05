package com.example.ejemplo1

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ejemplo1.data.dao.UserDao

class mapaSeguimientoEspera : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var statusCheckRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mapa_seguimiento_espera)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val idOrder = intent.getIntExtra("id_order", -1)

        val ingresarButton = findViewById<Button>(R.id.button4)
        ingresarButton.setOnClickListener {

            if (idOrder != -1) {
                try {
                    UserDao.eliminarOrdenPorId(idOrder)
                    Toast.makeText(this, "Orden eliminada correctamente", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error al eliminar la orden: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "ID de orden no válido", Toast.LENGTH_SHORT).show()
            }

            // Navegar a la pantalla de solicitarVehiculo
            val intent = Intent(this, solicitarVehiculo::class.java)
            startActivity(intent)
        }


        // Iniciar la verificación del estado de la orden
        iniciarVerificacionEstado(idOrder)
    }

    private fun iniciarVerificacionEstado(idOrder: Int) {
        statusCheckRunnable = object : Runnable {
            override fun run() {
                val status = UserDao.buscarStatusOrden(idOrder)
                if (status == "Progreso") {
                    // Cambiar a la pantalla de mapaSeguimiento
                    val intent = Intent(this@mapaSeguimientoEspera, mapaSeguimiento::class.java).apply {
                        putExtra("id_order", idOrder)
                    }
                    startActivity(intent)
                    finish() // Finaliza la pantalla actual
                } else {
                    // Volver a verificar después de un retraso
                    handler.postDelayed(this, 3000) // Consulta cada 3 segundos
                }
            }
        }
        handler.post(statusCheckRunnable) // Iniciar la primera verificación
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(statusCheckRunnable) // Detener verificaciones al destruir la actividad
    }
}