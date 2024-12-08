package com.example.ejemplo1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ejemplo1.data.dao.UserDao

class CalificacionConductor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calificacion_conductor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val ingresarButton2 = findViewById<Button>(R.id.button11)
        ingresarButton2.setOnClickListener {
            Toast.makeText(this, "Se realizo el reciclaje con exito", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, usuario::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }

        val idOrder = intent.getIntExtra("id_order", -1)
        // Si hay un usuario autenticado, buscar su nombre
        if (idOrder != -1) {
            val nameTrucker = UserDao.obtenerNameTrucker2(idOrder)

            // Actualizar el TextView con el nombre del conductor
            val textViewBienvenido = findViewById<TextView>(R.id.welcomeText)
            textViewBienvenido.text = "Calificar al conductor: $nameTrucker"
        } else {
            // Manejar el caso en que no haya usuario autenticado
            Toast.makeText(this, "No hay un usuario autenticado.", Toast.LENGTH_SHORT).show()
        }



    }
}