package com.example.ejemplo1

import android.animation.ValueAnimator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.ejemplo1.databinding.ActivityMapaSeguimientoBinding
import com.google.android.gms.maps.model.PolylineOptions

class mapaSeguimiento : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapaSeguimientoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapaSeguimientoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el Toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val inicio = LatLng(-20.242629798905142, -70.14183085277881) // Punto de inicio
        val destino = LatLng(-20.248132900715532, -70.13719410859989) // Punto de destino

        // Agrega el marcador inicial y el destino en el mapa
        mMap.addMarker(MarkerOptions().position(inicio).title("Recolector"))
        mMap.addMarker(MarkerOptions().position(destino).title("Reciclador"))

        // Dibuja la Polyline entre los dos puntos
        val polylineOptions = PolylineOptions()
            .add(inicio)
            .add(destino)
            .width(5f)
            .color(Color.BLUE)
        val polyline = mMap.addPolyline(polylineOptions)

        // Mueve la cámara al inicio
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inicio, 15f))

        // Llama a la función para animar el marcador
        animateMarkerAlongPath(inicio, destino)
    }

    private fun animateMarkerAlongPath(startPosition: LatLng, endPosition: LatLng) {
        val marker = mMap.addMarker(MarkerOptions().position(startPosition).title("Recolector en movimiento"))

        // Configura el ValueAnimator para interpolar la posición del marcador
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 20000 // Duración de la animación (en milisegundos)
        animator.addUpdateListener { animation ->
            val fraction = animation.animatedFraction
            val lat = fraction * endPosition.latitude + (1 - fraction) * startPosition.latitude
            val lng = fraction * endPosition.longitude + (1 - fraction) * startPosition.longitude
            val newPosition = LatLng(lat, lng)

            // Mueve el marcador a la nueva posición
            marker?.position = newPosition

            // Centra la cámara en la posición del marcador
            mMap.moveCamera(CameraUpdateFactory.newLatLng(newPosition))
        }
        animator.start()
    }

}