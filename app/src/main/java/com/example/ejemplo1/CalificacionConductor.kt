package com.example.ejemplo1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
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

        // Obtén el id_order del intent
        val idOrder = intent.getIntExtra("id_order", -1)

        // Obtén referencias a los elementos del layout
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val comentarioEditText = findViewById<EditText>(R.id.editTextTextMultiLine)
        val enviarButton = findViewById<Button>(R.id.button11)

        val ingresarButton2 = findViewById<Button>(R.id.button11)
        ingresarButton2.setOnClickListener {
            if (idOrder != -1) {
                // Obtén los valores ingresados
                val rating = ratingBar.rating
                val comentario = comentarioEditText.text.toString()

                // Llama a la función para actualizar en la base de datos
                UserDao.calificarConductor(idOrder, rating, comentario)

                // Muestra un mensaje de confirmación
                Toast.makeText(this, "Calificación enviada con éxito", Toast.LENGTH_SHORT).show()

                // Navega a la pantalla de usuario
                val intent = Intent(this, usuario::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Error: No se encontró la orden", Toast.LENGTH_SHORT).show()
            }
        }

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