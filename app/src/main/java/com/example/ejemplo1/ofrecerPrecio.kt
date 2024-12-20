package com.example.ejemplo1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ejemplo1.data.dao.UserDao
import kotlin.math.roundToInt

class ofrecerPrecio : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ofrecer_precio)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val idOrder = intent.getIntExtra("id_order", -1)
        val idTrucker = intent.getIntExtra("is_trucker", -1)
        val reciclable = intent.getBooleanExtra("is_recyclable", false)
        Log.d("id_order", "$idOrder")
        val monto = findViewById<EditText>(R.id.editTextMonto)
        val ingresarButton = findViewById<Button>(R.id.button6)

        ingresarButton.setOnClickListener {
            val montofinal = monto.text.toString().toInt()
            var total = montofinal

            if (!reciclable) {
                total = (montofinal + (montofinal * 0.05)).roundToInt()
            }

            UserDao.enviarOferta(idOrder, idTrucker, total)

            // Periodically check the status of the order
            val checkStatusRunnable = object : Runnable {
                override fun run() {
                    if (isRunning) {
                        val confirmacion = UserDao.buscarStatusOrden(idOrder)
                        if (confirmacion == "Aceptado") {
                            val intent = Intent(this@ofrecerPrecio, mapaSeguimiento::class.java).apply {
                                putExtra("id_order", idOrder)
                                putExtra("is_trucker", true)
                            }
                            startActivity(intent)
                            finish()
                        } else {
                            // Re-run the check after 10 seconds
                            Log.d("id_order", "aaaa: $idOrder")
                            handler.postDelayed(this, 1000)
                        }
                    }
                }
            }

            // Start checking the status
            handler.postDelayed(checkStatusRunnable, 1000)

            // Launch the Conductor activity immediately
            val intent = Intent(this, Conductor::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        handler.removeCallbacksAndMessages(null) // Cancel any pending callbacks
    }
}
