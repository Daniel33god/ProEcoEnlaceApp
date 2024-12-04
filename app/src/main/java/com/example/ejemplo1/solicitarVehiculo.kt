package com.example.ejemplo1

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ejemplo1.data.dao.UserDao
import com.example.ejemplo1.databinding.ActivityMapaSeguimiento2Binding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale

class solicitarVehiculo : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var editText: EditText
    private var currentMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_solicitar_vehiculo)

        // Recuperar el ID del usuario desde SharedPreferences
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)


        // Si hay un usuario autenticado, buscar su nombre
        if (userId != -1) {
            val nombre = UserDao.obtenerNombrePorId(userId)

            // Actualizar el TextView con el nombre del conductor
            val textViewBienvenido = findViewById<TextView>(R.id.textView2)
            textViewBienvenido.text = "¡Ubicacion de $nombre!"
        } else {
            // Manejar el caso en que no haya usuario autenticado
            Toast.makeText(this, "No hay un usuario autenticado.", Toast.LENGTH_SHORT).show()
        }


        // Configurar el Toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar el botón imagen para retroseder
        val ingresarButton1 = findViewById<ImageButton>(R.id.imageButtonBack)
        ingresarButton1.setOnClickListener {
            val intent = Intent(this, usuario::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }

        // Configurar el botón para tomarFoto
        val ingresarButton = findViewById<Button>(R.id.button5)
        ingresarButton.setOnClickListener {
            if (currentMarker != null) {
                val latLng = currentMarker!!.position
                val address = currentMarker!!.title // Obtener el nombre de la dirección del marcador
                val intent = Intent(this, tomarFoto::class.java)
                intent.putExtra("LATITUDE", latLng.latitude)
                intent.putExtra("LONGITUDE", latLng.longitude)
                intent.putExtra("ADDRESS", address)
                startActivity(intent) // Navegar a la nueva pantalla
            } else {
                Toast.makeText(this, "Por favor selecciona una ubicación antes de continuar", Toast.LENGTH_SHORT).show()
            }
        }

        // Inicializa el EditText y el botón
        editText = findViewById(R.id.editTextTextPostalAddress)
        val searchButton = findViewById<Button>(R.id.button1)

        // Configura el botón para buscar la dirección
        searchButton.setOnClickListener {
            val address = editText.text.toString()
            if (address.isNotEmpty()) {
                geocodeAddress(address)
            } else {
                Toast.makeText(this, "Por favor ingresa una dirección", Toast.LENGTH_SHORT).show()
            }
        }

        // Inicializa el mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun geocodeAddress(address: String) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocationName(address, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val location = addresses[0]
                val latLng = LatLng(location.latitude, location.longitude)

                // Elimina el marcador anterior si existe
                currentMarker?.remove()

                // Agrega un nuevo marcador
                currentMarker = mMap.addMarker(
                    MarkerOptions().position(latLng).title("Ubicación: $address")
                )

                // Mueve la cámara al nuevo marcador
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))

            } else {
                Toast.makeText(this, "Dirección no encontrada", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al geocodificar la dirección", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Configura el mapa si es necesario
        mMap.uiSettings.isZoomControlsEnabled = true
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
