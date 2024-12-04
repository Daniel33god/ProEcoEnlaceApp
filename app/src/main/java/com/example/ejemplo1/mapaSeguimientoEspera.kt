package com.example.ejemplo1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ejemplo1.data.dao.UserDao

class mapaSeguimientoEspera : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mapa_seguimiento_espera)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val idOrder = intent.getIntExtra("id_order", -1)
        // Configurar el botón "Ingresar" para cambiar de pantalla
        val ingresarButton = findViewById<Button>(R.id.button4)
        ingresarButton.setOnClickListener {
            Toast.makeText(this, "Solicitud Cancelada", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, solicitarVehiculo::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }

        var status = UserDao.buscarStatusOrden(idOrder)
        while (status.equals("Espera"))
        {
            status = UserDao.buscarStatusOrden(idOrder)
        }
        val intent = Intent(this, mapaSeguimiento::class.java).apply{
            /*cambiar el codigo para que redireccione al mapa*/
            putExtra("id_order", idOrder)
        }

    }

}