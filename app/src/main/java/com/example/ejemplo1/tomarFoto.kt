package com.example.ejemplo1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ejemplo1.data.dao.UserDao

class tomarFoto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tomar_foto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnLoadImage = findViewById<Button>(R.id.btnLoadImage)
        val imageView = findViewById<ImageView>(R.id.imageView)

        btnLoadImage.setOnClickListener {
            // Cargar la imagen desde los recursos drawable
            imageView.setImageResource(R.drawable.basurareciclada)
            imageView.visibility = View.VISIBLE
        }

        // Configurar el botón para cambiar de pantalla
        val ingresarButton1 = findViewById<ImageButton>(R.id.imageButtonBack)
        ingresarButton1.setOnClickListener {
            val intent = Intent(this, solicitarVehiculo::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }

        // Recuperar el ID del usuario desde SharedPreferences
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)

        // Configurar el botón "Ingresar" para cambiar de pantalla
        val ingresarButton = findViewById<Button>(R.id.button6)
        val editTextPeso = findViewById<EditText>(R.id.editTextPeso)
        val spinnerMetodo = findViewById<Spinner>(R.id.spinnerMetodo)
        val editTextMonto = findViewById<EditText>(R.id.editTextMonto)

        // Recuperar datos pasados desde la actividad anterior
        val latitude = intent.getDoubleExtra("LATITUDE", 0.0)
        val longitude = intent.getDoubleExtra("LONGITUDE", 0.0)
        val address = intent.getStringExtra("ADDRESS") ?: "Dirección desconocida"


        ingresarButton.setOnClickListener {
            val peso = editTextPeso.text.toString().toDoubleOrNull()
            val metodoPago = spinnerMetodo.selectedItem.toString()
            val monto = editTextMonto.text.toString().toDoubleOrNull()

            if (peso != null && monto != null) {
                // Llamamos al DAO para insertar la orden
                val success = UserDao.insertarOrden(userId,latitude, longitude, peso, metodoPago, monto)

                if (success) {
                    Toast.makeText(this, "Orden creada exitosamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, mapaSeguimientoEspera::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Error al crear la orden", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }


    }
}