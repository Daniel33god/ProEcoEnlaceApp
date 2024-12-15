package com.example.ejemplo1

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.example.ejemplo1.data.dao.UserDao
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class tomarFoto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tomar_foto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnLoadImage = findViewById<Button>(R.id.btnLoadImage)
        val imageView = findViewById<ImageView>(R.id.imageView)

        btnLoadImage.setOnClickListener {
            // Cargar la imagen desde los recursos drawable
            imageView.setImageResource(R.drawable.basurareciclada)
            imageView.visibility = View.VISIBLE
        }

        val fab: FloatingActionButton = findViewById(R.id.fab)
        val fab_2: FloatingActionButton = findViewById(R.id.fab_2)
        val longTooltip : String = """Para pedidos donde se superen los 1,500 Kg se aplicara un monto adicional de 8.000 pesos, por pedidos sobre 5,000 Kg se aplicara un monto adicional de 10.000 pesos, y si supera los 10,000 Kg se aplicara un monto adicional de 50.000 pesos.""".trimIndent()
        fab.setOnClickListener{
            AlertDialog.Builder(this).setTitle("Información Adicional")
                .setMessage(longTooltip)
                .setPositiveButton("Entendido") {dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        val longTooltip2 : String = "Si no desea reciclar se le aplicara una tarifa adicional de un 5% para el tratamiento de sus desechos caso contrario debe organizar sus desechos por tipo (ej. plasticos, papel, organicos, latas, entre otros)."
        fab_2.setOnClickListener{
            AlertDialog.Builder(this).setTitle("Información Adicional")
                .setMessage(longTooltip2)
                .setPositiveButton("Entendido") {dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        // Configurar el botón para cambiar de pantalla
        val ingresarButton1 = findViewById<ImageButton>(R.id.imageButtonBack)
        ingresarButton1.setOnClickListener {
            val intent = Intent(this, solicitarVehiculo::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }

        // Recuperar el ID del usuario desde SharedPreferences
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)

        // Configurar el botón "Ingresar" para cambiar de pantalla
        val ingresarButton = findViewById<Button>(R.id.button6)
        val editTextPeso = findViewById<EditText>(R.id.editTextPeso)
        val spinnerMetodo = findViewById<Spinner>(R.id.spinnerMetodo)
        val editTextMonto = findViewById<EditText>(R.id.editTextMonto)
        val reciclable = findViewById<CheckBox>(R.id.reciclable)
        // Recuperar datos pasados desde la actividad anterior
        val latitude = intent.getDoubleExtra("LATITUDE", 0.0)
        val longitude = intent.getDoubleExtra("LONGITUDE", 0.0)
        val address = intent.getStringExtra("ADDRESS") ?: "Dirección desconocida"

        val textViewMontoTotal = findViewById<TextView>(R.id.textView3)
        val textWatcher = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val monto = editTextMonto.text.toString().toDoubleOrNull() ?: 0.0
                val peso = editTextPeso.text.toString().toDoubleOrNull() ?: 0.0
                if (monto != null)
                {
                    var impuesto = 0.0
                    var tarifa_peso = 0.0
                    if(!reciclable.isChecked) {
                        impuesto = monto * 0.05
                    }
                    if(peso!! >= 1501 && peso!! <= 5000)
                    {
                        tarifa_peso = 8000.0
                    }
                    else if(peso!! >= 5001 && peso!! <= 10000)
                    {
                        tarifa_peso = 10000.0
                    }
                    else if(peso!! >= 10001)
                    {
                        tarifa_peso = 50000.0
                    }
                    val montoTotal = monto + impuesto + tarifa_peso
                    textViewMontoTotal.text = "Monto total con tarifa incluida: $%.2f".format(montoTotal)
                    textViewMontoTotal.visibility = View.VISIBLE
                }
                else
                {
                    textViewMontoTotal.visibility = View.GONE
                }
            }
        }
        editTextMonto.addTextChangedListener(textWatcher)
        editTextPeso.addTextChangedListener(textWatcher)
        reciclable.addTextChangedListener(textWatcher)

        ingresarButton.setOnClickListener {
            val peso = editTextPeso.text.toString().toDoubleOrNull()
            val metodoPago = spinnerMetodo.selectedItem.toString()
            var monto = editTextMonto.text.toString().toDoubleOrNull()
            /*if (peso == null || monto == null) {
                Toast.makeText(this, "Por favor ingresa valores válidos para Peso y Monto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }*/
            var tajo_monto : Double = 0.0
            var tarifa_peso : Double = 0.0
            if(!reciclable.isChecked && monto != null) {
                tajo_monto = monto * 0.05
            }
            if(peso!! >= 1501 && peso!! <= 5000)
            {
                tarifa_peso = 8000.0
            }
            else if(peso!! >= 5001 && peso!! <= 10000)
            {
                tarifa_peso = 10000.0
            }
            else if(peso!! >= 10001)
            {
                tarifa_peso = 50000.0
            }
            monto = monto!! + tajo_monto + tarifa_peso


            if (peso != null && monto != null) {
                // Llamamos al DAO para insertar la orden y obtener el id_order
                val idOrder = UserDao.insertarOrden(userId, latitude, longitude, peso, metodoPago, monto, address, tajo_monto, reciclable.isChecked)

                if (idOrder != null) {
                    if (idOrder > -1) {
                        Toast.makeText(this, "Orden creada exitosamente", Toast.LENGTH_SHORT).show()
                        // Pasamos el id_order a la siguiente pantalla
                        val intent = Intent(this, mapaSeguimientoEspera::class.java).apply {
                            putExtra("id_order", idOrder) // Pasamos el id_order
                        }

                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Error al crear la orden", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }


    }
}