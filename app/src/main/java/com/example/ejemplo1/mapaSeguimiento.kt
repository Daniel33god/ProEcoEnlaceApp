package com.example.ejemplo1

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.example.ejemplo1.api.ApiService
import com.example.ejemplo1.api.RouteResponse
import com.example.ejemplo1.data.dao.UserDao

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.ejemplo1.databinding.ActivityMapaSeguimientoBinding
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

        val ingresarButton2 = findViewById<Button>(R.id.button5)
        ingresarButton2.setOnClickListener {
            val intent = Intent(this, CalificacionConductor::class.java)
            startActivity(intent) // Navegar a la nueva pantalla
        }


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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val inicio = LatLng(-20.242629798905142, -70.14183085277881) // Punto de inicio
        val destino = LatLng(-20.248132900715532, -70.13719410859989) // Punto de destino

        // Agrega el marcador inicial y el destino en el mapa
        mMap.addMarker(MarkerOptions().position(inicio).title("Recolector"))
        mMap.addMarker(MarkerOptions().position(destino).title("Reciclador"))

        // Dibuja la Polyline entre los dos puntos
        /*val polylineOptions = PolylineOptions()
            .add(inicio)
            .add(destino)
            .width(5f)
            .color(Color.BLUE)*/
        val idOrder = intent.getIntExtra("id_order", -1)
        val start_lng = UserDao.buscarDoubleOrden(idOrder, "coordenates_x_order_start")
        val start_lat = UserDao.buscarDoubleOrden(idOrder, "coordenates_y_order_start")
        val end_lat = UserDao.buscarDoubleOrden(idOrder, "coordenates_y_order_end")
        val end_lng = UserDao.buscarDoubleOrden(idOrder, "coordenates_x_order_end")
        val start = "${start_lng},${start_lat}"
        val end = "${end_lng},${end_lat}"
        var coordlist: List<List<Double>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiService::class.java)
                .getRoute("5b3ce3597851110001cf6248a1c19d4960ff45a5b22d6da339c9312d", start, end)
            if (call.isSuccessful) {
                coordlist = getListCoordinates(call.body())
            } else {
                Log.i("aris", "KO")
            }
        }
        val polylineOptions = PolylineOptions()
        coordlist?.forEach{
            polylineOptions.add(LatLng(it[1], it[0]))
        }
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
    /*
    * CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiService::class.java).getRoute("5b3ce3597851110001cf6248a1c19d4960ff45a5b22d6da339c9312d", "-70.13193931907803,-20.251239429965054", "-70.1367567054598, -20.247957309076586")
            call.body()?.features?.first()?.geometry?.coordinates?.forEach(){
                val x = it[1]
                val y = it[0]
                print(x)
                print(", ")
                println(y)
            }
            /* [ [y, x] , [y, x] , [y, x] ] */
        }


     */
    private fun getListCoordinates(routeResponse: RouteResponse?): List<List<Double>>? {
        return routeResponse?.features?.first()?.geometry?.coordinates
    }
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}