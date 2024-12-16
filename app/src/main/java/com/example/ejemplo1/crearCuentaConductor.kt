package com.example.ejemplo1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ejemplo1.data.dao.UserDao

class crearCuentaConductor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_cuenta_conductor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recibir los datos del Intent
        val name = intent.getStringExtra("name")
        val lastName = intent.getStringExtra("lastName")
        val dni = intent.getStringExtra("dni")
        val email = intent.getStringExtra("email")
        val gender = intent.getStringExtra("gender")
        val phone = intent.getStringExtra("phone")
        val address = intent.getStringExtra("address")
        val password = intent.getStringExtra("password")
        val birthDate = intent.getStringExtra("birthDate")

        //nuevos datos
        val patente = findViewById<EditText>(R.id.editTextPatente)
        val modelo = findViewById<EditText>(R.id.editTextModelo)
        val pesoMax = findViewById<EditText>(R.id.editTextPeso)
        val licencia = findViewById<Spinner>(R.id.tipoLicencia)
        val descripcion = findViewById<EditText>(R.id.editTextDescripcion)

        val btnLoadImage = findViewById<Button>(R.id.btnLoadImage)
        val imageView = findViewById<ImageView>(R.id.imageView)

        btnLoadImage.setOnClickListener {
            // Cargar la imagen desde los recursos drawable
            imageView.setImageResource(R.drawable.revisiontecnica)
            imageView.visibility = View.VISIBLE
        }

        // Configurar el botón "Ingresar" para cambiar de pantalla
        val ingresarButton2 = findViewById<Button>(R.id.button2)
        ingresarButton2.setOnClickListener {
            // Llamar al método insertarCuentaUsuario para insertar el usuario
            val isSuccess = UserDao.cuentaConductor(name.toString(),
                lastName.toString(), dni.toString(), email.toString(), phone.toString(),
                address.toString(), password.toString(), gender.toString(), birthDate.toString(),
                patente.toString(), modelo.toString(), pesoMax, licencia.toString(), descripcion.toString())

            if (isSuccess) {
                Toast.makeText(this, "Cuenta Conductor creada con éxito", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, crearCuenta::class.java)
                startActivity(intent) // Navegar a la nueva pantalla
            }else {
                Toast.makeText(this, "Error al crear la cuenta", Toast.LENGTH_SHORT).show()
            }
        }

    }
}