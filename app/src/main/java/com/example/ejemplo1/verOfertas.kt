package com.example.ejemplo1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ejemplo1.data.dao.UserDao

class verOfertas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_ofertas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar el botón para cambiar de pantalla
        val ingresarButton1 = findViewById<ImageButton>(R.id.imageButtonBack)
        ingresarButton1.setOnClickListener {
            val intent = Intent(this, verOrden::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }


        // Recuperar el ID del usuario desde SharedPreferences
        val idOrder = intent.getIntExtra("id_order", -1)

        if(idOrder != -1) {
            val ordenes = UserDao.obtenerOfertasOrden(idOrder)


            Log.d("ID_ORDER", "holaaaaaaaaaaaaaaaaaaaaaaa: $idOrder")
            if (ordenes.isEmpty()) {
                Toast.makeText(this, "No hay ofertas.", Toast.LENGTH_SHORT).show()
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

                    val nameTextView = TextView(this).apply {
                        text = "Nombre Conductor: ${order["name_user"]}"
                        gravity = Gravity.CENTER
                        setPadding(0, 0, 0, 4) // Espacio entre los elementos
                    }

                    val matriculaTextView = TextView(this).apply {
                        text = "Matricula del Vehiculo: ${order["matricula_truck"]}"
                        gravity = Gravity.CENTER
                        setPadding(0, 0, 0, 4) // Espacio entre los elementos
                    }

                    val modelTextView = TextView(this).apply {
                        text = "Modelo del Vehiculo: ${order["model_truck"]}"
                        gravity = Gravity.CENTER
                        setPadding(0, 0, 0, 4) // Espacio entre los elementos
                    }

                    val descriptionTextView = TextView(this).apply {
                        text = "Descripción del Vehiculo: ${order["description_trucker"]}"
                        gravity = Gravity.CENTER
                        setPadding(0, 0, 0, 4) // Espacio entre los elementos
                    }

                    val offerTextView = TextView(this).apply {
                        text = "Oferta Propuesta: ${order["offer_value"]}"
                        gravity = Gravity.CENTER
                        setPadding(0, 0, 0, 4) // Espacio entre los elementos
                    }

                    val followButton = Button(this).apply {
                        text = "Aceptar Oferta"
                        setBackgroundColor(Color.parseColor("#673AB7")) // Establecer el color de fondo
                        setTextColor(Color.WHITE) // Cambiar el color del texto a blanco para mejor visibilidad
                        // Configurar el margen inferior
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(0, 0, 0, 14) // Margen inferior de 12dp
                            gravity = Gravity.BOTTOM // Alinear el botón al fondo
                        }
                        setOnClickListener {
                            val idOrder = order["id_order"] as? String
                            val userId = getSharedPreferences("user_session", MODE_PRIVATE).getInt(
                                "user_id",
                                -1
                            )
                            val idTrucker = UserDao.obtenerIdTrucker(userId)

                            if (idTrucker != null && idOrder != null) {
                                UserDao.aceptarSolicitud(idTrucker, idOrder.toInt())
                                val intent = Intent(
                                    this@verOfertas,
                                    mapaSeguimientoEspera::class.java
                                ).apply {
                                    putExtra("id_order", idOrder.toInt())
                                    putExtra("is_trucker", true)
                                }
                                startActivity(intent)
                            }
                        }
                    }

                    // Añadir los TextViews al LinearLayout de la orden
                    orderLayout.addView(nameTextView)
                    orderLayout.addView(matriculaTextView)
                    orderLayout.addView(modelTextView)
                    orderLayout.addView(descriptionTextView)
                    orderLayout.addView(offerTextView)
                    orderLayout.addView(followButton)

                    // Añadir este LinearLayout al LinearLayout principal (ordersLayout)
                    ordersLayout.addView(orderLayout)
                }
            }
        }else{
            Toast.makeText(this, "Uy un error traumatico.", Toast.LENGTH_SHORT).show()
        }

    }
}