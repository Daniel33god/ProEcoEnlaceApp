package com.example.ejemplo1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MetodoPago : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_metodo_pago)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val Cash = findViewById<Button>(R.id.CashButton)
        val Paypal = findViewById<Button>(R.id.PaypalButton)
        val Credit = findViewById<Button>(R.id.CreditButton)
        val Debit = findViewById<Button>(R.id.DebitButton)
        Cash.setOnClickListener{
            Toast.makeText(this, "Metodo de pago efectivo", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, tomarFoto::class.java)
            startActivity(intent)
        }
        Paypal.setOnClickListener{
            Toast.makeText(this, "Metodo de pago paypal", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, tomarFoto::class.java)
            startActivity(intent)
        }
        Credit.setOnClickListener{
            Toast.makeText(this, "Metodo de pago credito", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, tomarFoto::class.java)
            startActivity(intent)
        }
        Debit.setOnClickListener{
            Toast.makeText(this, "Metodo de pago debito", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, tomarFoto::class.java)
            startActivity(intent)
        }
    }
}