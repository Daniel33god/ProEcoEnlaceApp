package com.example.ejemplo1

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ejemplo1.data.dao.UserDao

class ordenesRealizadas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ordenes_realizadas)

        val ingresarButton = findViewById<Button>(R.id.button12)
        ingresarButton.setOnClickListener {
            // Navegar a la pantalla de "Conductor"
            val intent = Intent(this, Conductor::class.java)
            startActivity(intent)
        }

        // Recuperar el ID del usuario desde SharedPreferences
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)

        // Llamar a obtenerOrdenes desde UserDao
        try {
            val ordenes = UserDao.obtenerOrdenesRealizadas()
            if (ordenes.isEmpty()) {
                Toast.makeText(this, "No hay órdenes disponibles.", Toast.LENGTH_SHORT).show()
            } else {
                // Obtener la referencia al LinearLayout donde se agregarán las órdenes
                val ordersLayout = findViewById<LinearLayout>(R.id.ordersLayout)

                // Iterar sobre cada orden y agregar un conjunto de TextViews para mostrarla
                for (order in ordenes) {
                    // Crear un LinearLayout para esta orden (vertical)
                    val orderLayout = LinearLayout(this).apply {
                        orientation = LinearLayout.VERTICAL
                        gravity = Gravity.CENTER
                        setPadding(0, 8, 0, 8) // Reducir el padding
                    }

                    // Crear TextViews para mostrar los detalles de la orden
                    val nameTextView = TextView(this).apply {
                        text = "Nombre: ${order["name_user"]}"
                        gravity = Gravity.CENTER
                        setPadding(0, 0, 0, 4) // Espacio entre los elementos
                    }

                    val weightTextView = TextView(this).apply {
                        text = "Peso: ${order["weight_order"]}"
                        gravity = Gravity.CENTER
                        setPadding(0, 0, 0, 4) // Espacio entre los elementos
                    }

                    val valueTextView = TextView(this).apply {
                        text = "Precio: ${order["value_order"]}"
                        gravity = Gravity.CENTER
                        setPadding(0, 0, 0, 4) // Espacio entre los elementos
                    }

                    val addressTextView = TextView(this).apply {
                        text = "Dirección: ${order["address_order_start"]}"
                        gravity = Gravity.CENTER
                        setPadding(0, 0, 0, 4) // Espacio entre los elementos
                    }

                    val rankingTextView = TextView(this).apply {
                        text = "Calificación: ${order["ranking_trucker_order"]} estrellas"
                        gravity = Gravity.CENTER
                        setPadding(0, 0, 0, 4) // Espacio entre los elementos
                    }

                    val commentTextView = TextView(this).apply {
                        text = "Comentario: ${order["comment_trucker_order"]}"
                        gravity = Gravity.CENTER
                        setPadding(0, 0, 0, 4) // Espacio entre los elementos
                    }

                    // Añadir los TextViews al LinearLayout de la orden
                    orderLayout.addView(nameTextView)
                    orderLayout.addView(weightTextView)
                    orderLayout.addView(valueTextView)
                    orderLayout.addView(addressTextView)
                    orderLayout.addView(rankingTextView)
                    orderLayout.addView(commentTextView)

                    // Añadir este LinearLayout al LinearLayout principal (ordersLayout)
                    ordersLayout.addView(orderLayout)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al cargar las órdenes: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
