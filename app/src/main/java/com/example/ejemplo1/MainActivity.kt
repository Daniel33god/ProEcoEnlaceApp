package com.example.ejemplo1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Configurar el botón "Ingresar" para cambiar de pantalla
        val ingresarButton = findViewById<Button>(R.id.button)
        ingresarButton.setOnClickListener {
            val intent = Intent(this, activity_welcome::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }

        // Configurar el botón "Ingresar" para cambiar de pantalla
        val ingresarButton3 = findViewById<Button>(R.id.button2)
        ingresarButton3.setOnClickListener {
            val intent = Intent(this, tomarFoto::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }

        // Buscar el TextView por su ID
        val textView = findViewById<TextView>(R.id.textView)

        // Configurar el click listener
        textView.setOnClickListener {
            // Crear un Intent para iniciar la nueva actividad
            val intent = Intent(this, crearCuenta::class.java)
            startActivity(intent) // Navegar a la segunda actividad
        }

    }
}