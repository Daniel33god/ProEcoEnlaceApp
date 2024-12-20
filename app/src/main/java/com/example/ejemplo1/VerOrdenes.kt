package com.example.ejemplo1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.example.ejemplo1.data.dao.UserDao
import kotlin.math.log

class VerOrdenes : AppCompatActivity() {

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_ordenes)

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
            val ordenes = UserDao.obtenerOrdenes()
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

                    val metodTextView = TextView(this).apply {
                        text = "Metodo de Pago: ${order["payment_method_order"]}"
                        gravity = Gravity.CENTER
                        setPadding(0, 0, 0, 4) // Espacio entre los elementos
                    }

                    val addressTextView = TextView(this).apply {
                        text = "Dirección: ${order["address_order_start"]}"
                        gravity = Gravity.CENTER
                        setPadding(0, 0, 0, 4) // Espacio entre los elementos
                    }

                    // Crear un ImageView para mostrar la imagen de "basurareciclada.jpg"
                    val imageView = ImageView(this).apply {
                        setImageResource(R.drawable.basurareciclada) // Establece la imagen desde drawable
                        adjustViewBounds = true // Permite que la imagen se ajuste dentro de los límites
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            gravity = Gravity.CENTER // Centra la imagen en el layout
                            setMargins(0, 8, 0, 8) // Margen para separar la imagen de otros elementos
                        }
                    }

                    // Crear un botón para redirigir al mapa de seguimiento
                    val followButton = Button(this).apply {
                        text = "Ofrecer Precio"
                        setOnClickListener {
                            // Recuperar el id_order de la orden actual
                            val idOrder = order["id_order"] as? String

                            val idTrucker = UserDao.obtenerIdTrucker(userId)

                            if (idTrucker != null) {
                                if (idOrder != null) {
                                    UserDao.aceptarSolicitud(idTrucker, idOrder.toInt())
                                    // Navegar a la pantalla "MapaSeguimiento"
                                    val intent = Intent(this@VerOrdenes, mapaSeguimiento::class.java).apply {
                                        putExtra("id_order", idOrder.toInt()) // Asegúrate de enviarlo como Int
                                        putExtra("is_trucker", true)
                                    }
                                    startActivity(intent)
                                }
                            }
                        }
                    }



                    // Añadir los TextViews al LinearLayout de la orden
                    orderLayout.addView(nameTextView)
                    orderLayout.addView(weightTextView)
                    orderLayout.addView(valueTextView)
                    orderLayout.addView(metodTextView)
                    orderLayout.addView(addressTextView)
                    orderLayout.addView(imageView)
                    orderLayout.addView(followButton)  // Añadir el botón debajo de los detalles de la orden

                    // Añadir este LinearLayout al LinearLayout principal (ordersLayout)
                    ordersLayout.addView(orderLayout)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al cargar las órdenes: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }*/

    private lateinit var ordersLayout: LinearLayout
    private val handler = Handler(Looper.getMainLooper())
    private var updateThread: Thread? = null
    private var isRunning = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_ordenes)

        ordersLayout = findViewById(R.id.ordersLayout)


        val ingresarButton = findViewById<Button>(R.id.button12)
        ingresarButton.setOnClickListener {
            val intent = Intent(this, Conductor::class.java)
            startActivity(intent)
            finish()
        }

        startOrderUpdateThread()

    }

    private fun startOrderUpdateThread() {
        updateThread = Thread {
            while (isRunning) {
                try {
                    // Fetch the latest orders from the database
                    val ordenes = UserDao.obtenerOrdenes()

                    // Update the UI on the main thread
                    handler.post {
                        updateOrders(ordenes)
                    }

                    // Check for new data every 10 seconds
                    Thread.sleep(10000)
                } catch (e: Exception) {
                    handler.post {
                        /*Toast.makeText(this, "Error al cargar las órdenes: ${e.message}", Toast.LENGTH_SHORT).show()
                        Log.d("error", "${e.message}")*/
                    }
                }
            }
        }
        updateThread?.start()
    }

    private fun updateOrders(ordenes: List<Map<String, Any>>) {
        ordersLayout.removeAllViews()

        if (ordenes.isEmpty()) {
            Toast.makeText(this, "No hay órdenes disponibles.", Toast.LENGTH_SHORT).show()
        } else {
            for (order in ordenes) {
                Log.d("is_recyclable", "bool: ${order["is_recyclable"]}")
                val orderLayout = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    gravity = Gravity.CENTER
                    setPadding(0, 8, 0, 8)
                }

                val nameTextView = TextView(this).apply {
                    text = "Nombre: ${order["name_user"]}"
                    gravity = Gravity.CENTER
                    textSize = 18f
                    setPadding(0, 0, 0, 4)
                }

                /*val weightTextView = TextView(this).apply {
                    text = "Peso: ${order["weight_order"]}"
                    gravity = Gravity.CENTER
                    setPadding(0, 0, 0, 4)
                }

                val valueTextView = TextView(this).apply {
                    text = "Precio: ${order["value_order"]}"
                    gravity = Gravity.CENTER
                    setPadding(0, 0, 0, 4)
                }*/
                val reciclajeTextView = TextView(this).apply {

                    if(order["is_recyclable"]!!.equals("t"))
                        text = "Reciclable: Si"

                    else
                        text = "Reciclable: No"
                    gravity = Gravity.CENTER
                    textSize = 18f
                    setPadding(0, 0, 0, 4)
                }
                val metodTextView = TextView(this).apply {
                    text = "Metodo de Pago: ${order["payment_method_order"]}"
                    gravity = Gravity.CENTER
                    textSize = 18f
                    setPadding(0, 0, 0, 4)
                }

                val addressTextView = TextView(this).apply {
                    text = "Dirección: ${order["address_order_start"]}"
                    gravity = Gravity.CENTER
                    textSize = 18f
                    setPadding(0, 0, 0, 4)
                }

                val descriptionTextView = TextView(this).apply {
                    text = "Descripción: ${order["description_order"]}"
                    gravity = Gravity.CENTER
                    textSize = 18f
                    setPadding(0, 0, 0, 4)
                }

                val imageView = ImageView(this).apply {
                    setImageResource(R.drawable.basurareciclada)
                    adjustViewBounds = true
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.CENTER
                        setMargins(0, 8, 0, 8)
                    }
                }

                val followButton = Button(this).apply {
                    text = "Ofrecer Precio"
                    setOnClickListener {
                        val idOrder = order["id_order"] as? String
                        //val reciclable = order["is_recyclable"] as? Boolean
                        var reciclable = false
                        if(order["is_recyclable"]!!.equals("t"))
                            reciclable = true


                        val userId = getSharedPreferences("user_session", MODE_PRIVATE).getInt("user_id", -1)
                        val idTrucker = UserDao.obtenerIdTrucker(userId)

                        if (idTrucker != null && idOrder != null) {
                            val intent = Intent(this@VerOrdenes, ofrecerPrecio::class.java).apply {
                                putExtra("id_order", idOrder.toInt())
                                putExtra("is_trucker", idTrucker.toInt())
                                putExtra("is_recyclable", reciclable)
                            }
                            startActivity(intent)
                            //UserDao.aceptarSolicitud(idTrucker, idOrder.toInt())

                            /*val intent = Intent(this@VerOrdenes, mapaSeguimiento::class.java).apply {
                                putExtra("id_order", idOrder.toInt())
                                putExtra("is_trucker", true)
                            }*/
                            /*startActivity(intent)*/
                            finish()
                        }
                    }
                }

                // Add all views to the order layout
                orderLayout.addView(nameTextView)
                /*orderLayout.addView(weightTextView)
                orderLayout.addView(valueTextView)*/
                orderLayout.addView(reciclajeTextView)
                orderLayout.addView(metodTextView)
                orderLayout.addView(addressTextView)
                orderLayout.addView(descriptionTextView)
                orderLayout.addView(imageView)
                orderLayout.addView(followButton)

                // Add the order layout to the main orders layout
                ordersLayout.addView(orderLayout)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        if (updateThread?.isAlive == true) {
            updateThread?.interrupt()
        }
        updateThread = null
    }


}

