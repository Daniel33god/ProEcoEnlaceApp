package com.example.ejemplo1

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class crearCuenta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_cuenta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Configurar el botón "Ingresar" para cambiar de pantalla
        val ingresarButton = findViewById<Button>(R.id.button)
        ingresarButton.setOnClickListener {
            Toast.makeText(this, "La cuenta fue creada con éxito", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }

        // Configurar el botón "Ingresar" para cambiar de pantalla
        val ingresarButton2 = findViewById<Button>(R.id.button2)
        ingresarButton2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }

        // Configurar el DatePickerDialog para la fecha de nacimiento
        val fechaNacimientoEditText = findViewById<EditText>(R.id.editTextFechaNacimiento)
        fechaNacimientoEditText.setOnClickListener {
            // Obtener la fecha actual
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Mostrar el DatePickerDialog
            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                // Formatear y mostrar la fecha seleccionada
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                fechaNacimientoEditText.setText(formattedDate)
            }, year, month, day)

            datePickerDialog.show()
        }
    }
}