package com.example.ejemplo1

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ejemplo1.data.dao.UserDao
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
        val ingresarButton2 = findViewById<Button>(R.id.button2)
        ingresarButton2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }

        // Configurar el DatePickerDialog para la fecha de nacimiento
        val birthDate = findViewById<EditText>(R.id.editTextFechaNacimiento)
        birthDate.setOnClickListener {
            // Obtener la fecha actual
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Mostrar el DatePickerDialog
            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                // Formatear y mostrar la fecha seleccionada
                val formattedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                birthDate.setText(formattedDate)
            }, year, month, day)

            datePickerDialog.show()
        }


        val name = findViewById<EditText>(R.id.editTextNombre)
        val lastName = findViewById<EditText>(R.id.editTextApellido)
        val dni = findViewById<EditText>(R.id.editTextRUT)
        val email = findViewById<EditText>(R.id.editTextEmail)
        val gender = findViewById<Spinner>(R.id.spinnerGenero)
        val phone = findViewById<EditText>(R.id.editTextCelular)
        val address = findViewById<EditText>(R.id.editTextDireccion)
        val password = findViewById<EditText>(R.id.editTextContrasena)

        //val birthDate = findViewById<EditText>(R.id.editTextFechaNacimiento)

        // Configurar el botón "Ingresar" para cambiar de pantalla
        val spinnerCuenta = findViewById<Spinner>(R.id.spinnercuenta)
        val ingresarButton = findViewById<Button>(R.id.button)
        // Lógica cuando se presiona el botón "Crear Cuenta"
        ingresarButton.setOnClickListener {
            val selectedItem = spinnerCuenta.selectedItem.toString() // Obtener el valor seleccionado en el Spinner

            when (selectedItem) {
                "Usuario" -> {

                    // Llamar al método insertarCuentaUsuario para insertar el usuario
                    val isSuccess = UserDao.insertarCuentaUsuario(name.toString(),
                        lastName.toString(), dni.toString(), email.toString(), phone.toString(),
                        address.toString(), password.toString(), gender.toString(), birthDate.toString()
                    )

                    if (isSuccess != null) {
                        Toast.makeText(this, "Cuenta Usuario creada con éxito", Toast.LENGTH_SHORT).show()
                        // Si se selecciona "Usuario", redirigir a MainActivity
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Error al crear la cuenta", Toast.LENGTH_SHORT).show()
                    }
                }
                "Conductor" -> {
                    // Si se selecciona "Conductor", redirigir a CrearCuentaConductor
                    val intent = Intent(this, crearCuentaConductor::class.java).apply {
                        putExtra("name", name.text.toString())
                        putExtra("lastName", lastName.text.toString())
                        putExtra("dni", dni.text.toString())
                        putExtra("email", email.text.toString())
                        putExtra("gender", gender.selectedItem.toString())
                        putExtra("phone", phone.text.toString())
                        putExtra("address", address.text.toString())
                        putExtra("password", password.text.toString())
                        putExtra("birthDate", birthDate.text.toString())
                    }
                    startActivity(intent)
                }
                else -> {
                    // Si no se selecciona "Usuario" o "Conductor", mostrar un Toast
                    Toast.makeText(this, "Seleccione qué tipo de cuenta desea tener", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
}