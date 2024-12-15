package com.example.ejemplo1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.os.StrictMode
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.ejemplo1.api.ApiService
import com.example.ejemplo1.data.dao.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.typeOf

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar el botón "Ingresar" para ingresar como Usuario
        val ingresarButton = findViewById<Button>(R.id.button)
        ingresarButton.setOnClickListener {
            var editEmail = findViewById(R.id.editTextText) as EditText
            var editPass = findViewById(R.id.editTextTextPassword) as EditText


            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

            val userId = procesar_login_usuario(editEmail.text.toString(), editPass.text.toString())
            if (userId != null) {
                val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("user_id", userId) // Guarda el ID en SharedPreferences
                editor.apply()

                val intent = Intent(this, usuario::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Error en los datos!!!", Toast.LENGTH_SHORT).show()
            }
        }
        /*
        val ingresarButton4= findViewById<Button>(R.id.button10)
        ingresarButton4.setOnClickListener {
            val intent = Intent(this, Conductor::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }*/

        // Configurar el botón "Ingresar" para ingresar como Conductor
        val ingresarButton4= findViewById<Button>(R.id.button10)
        ingresarButton4.setOnClickListener {
            var editEmail = findViewById(R.id.editTextText) as EditText
            var editPass = findViewById(R.id.editTextTextPassword) as EditText

            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

            val userId = procesar_login_conductor(editEmail.text.toString(), editPass.text.toString())
            if (userId != null) {
                val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("user_id", userId) // Guarda el ID en SharedPreferences
                editor.apply()

                val intent = Intent(this, Conductor::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Error en los datos!!!", Toast.LENGTH_SHORT).show()
            }
        }

        // Buscar el TextView por su ID
        val textView = findViewById<TextView>(R.id.textView)

        // Configurar el click listener
        textView.setOnClickListener {
            // Crear un Intent para iniciar la nueva actividad
            val intent = Intent(this, crearCuenta::class.java)
            startActivity(intent) // Navegar a la segunda actividad
        }

        /*val ingresarButton5= findViewById<Button>(R.id.button13)
        ingresarButton5.setOnClickListener {
            val intent = Intent(this, solicitarVehiculo::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }*/

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        println("Hola")
        val threadWithRunnable = Thread(SimpleRunnable())
        threadWithRunnable.start()

    }


    fun procesar_login_usuario(email:String,password:String): Int? {
        return UserDao.buscar_usuario(email,password)
    }

    fun procesar_login_conductor(email:String,password:String): Int? {
        return UserDao.buscar_conductor(email, password) // Devuelve el ID si las credenciales son válidas
    }



}
class SimpleRunnable: Runnable {
    public override fun run() {
        println("${Thread.currentThread()} has run.")
        var users=UserDao.listar_usuario()
        println(users)
    }
}
