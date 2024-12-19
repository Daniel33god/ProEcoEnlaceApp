package com.example.ejemplo1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ejemplo1.data.dao.UserDao


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

        // Recuperar el ID del usuario desde SharedPreferences
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)

        // Si hay un usuario autenticado, buscar su nombre
        if (userId != -1) {
            val nombre = UserDao.obtenerNombrePorId(userId)

            // Actualizar el TextView con el nombre del conductor
            val textViewBienvenido = findViewById<TextView>(R.id.welcomeText)
            textViewBienvenido.text = "¡Bienvenido $nombre!"
        } else {
            // Manejar el caso en que no haya usuario autenticado
            Toast.makeText(this, "No hay un usuario autenticado.", Toast.LENGTH_SHORT).show()
        }

        // Configurar el botón "Ingresar" para cambiar de pantalla
        val ingresarButton2 = findViewById<Button>(R.id.button7)
        ingresarButton2.setOnClickListener {
            // Verificar la existencia de una orden para el usuario actual
            val existe = UserDao.existenciaOrden(userId)

            if (existe) {
                Toast.makeText(this, "Ya tienes una orden activa.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, solicitarVehiculo::class.java)
                startActivity(intent) // Navegar a la nueva pantalla
            }
        }

        // Configurar el botón "Ingresar" para cambiar de pantalla
        val ingresarButton3 = findViewById<Button>(R.id.verOrden)
        ingresarButton3.setOnClickListener {
            val intent = Intent(this, verOrden::class.java)
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