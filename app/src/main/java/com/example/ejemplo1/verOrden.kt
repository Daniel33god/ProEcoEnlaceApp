package com.example.ejemplo1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
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
import kotlin.math.log

class verOrden : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_orden)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Configurar el Toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Configurar el botón para cambiar de pantalla
        val ingresarButton1 = findViewById<ImageButton>(R.id.imageButtonBack)
        ingresarButton1.setOnClickListener {
            val intent = Intent(this, usuario::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }


        // Recuperar el ID del usuario desde SharedPreferences
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)

        val ordenes = UserDao.obtenerOrden(userId)
        if (ordenes.isEmpty()) {
            Toast.makeText(this, "No tienes una orden.", Toast.LENGTH_SHORT).show()
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

                val methodTextView = TextView(this).apply {
                    text = "Metodo de Pago: ${order["payment_method_order"]}"
                    gravity = Gravity.CENTER
                    setPadding(0, 0, 0, 4) // Espacio entre los elementos
                }

                val isRecyclerTextView = TextView(this).apply {
                    text = "¿Es reciclaje?: ${if (order["is_recyclable"].equals("t")) "Sí" else "No"}"
                    gravity = Gravity.CENTER
                    setPadding(0, 0, 0, 4) // Espacio entre los elementos
                }


                val addressTextView = TextView(this).apply {
                    text = "Dirección: ${order["address_order_start"]}"
                    gravity = Gravity.CENTER
                    setPadding(0, 0, 0, 4) // Espacio entre los elementos
                }

                val imageView = ImageView(this).apply {
                    setImageResource(R.drawable.basurareciclada)
                    adjustViewBounds = true
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.CENTER
                        setMargins(0, 8, 0, 14)
                    }
                }

                val followButton = Button(this).apply {
                    text = "Ver Ofertas"
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

                        //ERROR DE ENVIO DE ID_ORDER

                        // Recuperar id_order de order
                        val idOrder = order["id_order"]?.toIntOrNull() //tiene k irse al verOfertas

                        // Verificar si idOrder es válido
                        if (idOrder != null) {
                            val intent = Intent(this@verOrden, verOfertas::class.java).apply {
                                // Agregar el id_order al Intent
                                putExtra("id_order", idOrder)
                                // Usando Log.d para debug
                                Log.d("ID_ORDER", "El idOrder es: $idOrder")
                            }
                            startActivity(intent)
                        } else {
                            Log.d("ID_ORDER", "Que es esto: $idOrder")
                            Toast.makeText(this@verOrden, "ID de orden no válido", Toast.LENGTH_SHORT).show()
                        }
                    }
                }


                val followButton2 = Button(this).apply {
                    text = "Cancelar Orden"
                    setBackgroundColor(Color.parseColor("#FF5722")) // Establecer el color de fondo
                    setTextColor(Color.WHITE) // Cambiar el color del texto a blanco para mejor visibilidad

                    setOnClickListener {
                        val idOrder = order["id_order"]?.toString() // Convertir a String si es necesario

                        if (!idOrder.isNullOrEmpty()) {
                            try {
                                UserDao.eliminarOrdenPorId(idOrder)
                                // Mostrar un Toast indicando éxito
                                Toast.makeText(this@verOrden, "Orden eliminada correctamente", Toast.LENGTH_SHORT).show()
                                // Navegar a la pantalla de usuario
                                val intent = Intent(this@verOrden, usuario::class.java)
                                startActivity(intent)
                            } catch (e: Exception) {
                                // Mostrar un Toast indicando error
                                Toast.makeText(this@verOrden, "Error al eliminar la orden: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            // Mostrar un Toast si el ID de la orden es inválido
                            Toast.makeText(this@verOrden, "ID de orden no válido", Toast.LENGTH_SHORT).show()
                        }
                    }
                }






                // Añadir los TextViews al LinearLayout de la orden
                orderLayout.addView(methodTextView)
                orderLayout.addView(isRecyclerTextView)
                orderLayout.addView(addressTextView)
                orderLayout.addView(imageView)
                orderLayout.addView(followButton)
                orderLayout.addView(followButton2)

                // Añadir este LinearLayout al LinearLayout principal (ordersLayout)
                ordersLayout.addView(orderLayout)
            }
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