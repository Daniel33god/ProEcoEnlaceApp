package com.example.ejemplo1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
        val btnMetodoPago = findViewById<Button>(R.id.btnPayment)
        btnMetodoPago.setOnClickListener{
            val intent = Intent(this, MetodoPago::class.java)
            startActivity(intent)
        }

        btnLoadImage.setOnClickListener {
            // Cargar la imagen desde los recursos drawable
            imageView.setImageResource(R.drawable.basurareciclada)
            imageView.visibility = View.VISIBLE
        }
    }
}