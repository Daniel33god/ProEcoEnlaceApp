package com.example.ejemplo1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class usuario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario)

        // Configurar el Toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Configurar el botón "Ingresar" para cambiar de pantalla
        val ingresarButton = findViewById<Button>(R.id.button3)
        ingresarButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }

        // Configurar el botón "Ingresar" para cambiar de pantalla
        val ingresarButton2 = findViewById<Button>(R.id.button7)
        ingresarButton2.setOnClickListener {
            val intent = Intent(this, SolicitarVehiculo::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                Toast.makeText(this, "Se presionó el botón About", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_about_1 -> {
                Toast.makeText(this, "Se presionó el botón About 1", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_about_2 -> {
                Toast.makeText(this, "Se presionó el botón About 2", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_about_3 -> {
                // Configurar el botón "Ingresar" para cambiar de pantalla
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }





}