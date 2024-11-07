package com.example.ejemplo1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class vehiculoDisponible : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vehiculo_disponible)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ingresarButton3 = findViewById<Button>(R.id.button2)
        ingresarButton3.setOnClickListener {
            val intent = Intent(this, mapaSeguimiento::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }

        val ingresarButton4 = findViewById<Button>(R.id.button4)
        ingresarButton4.setOnClickListener {
            val intent = Intent(this, mapaSeguimiento::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }

        val ingresarButton5 = findViewById<Button>(R.id.button5)
        ingresarButton5.setOnClickListener {
            val intent = Intent(this, mapaSeguimiento::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }


    }
}