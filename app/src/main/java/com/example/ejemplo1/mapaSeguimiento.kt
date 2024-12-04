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
import android.view.animation.LinearInterpolator
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
        val idOrder = intent.getIntExtra("id_order", -1)
        val start_lng = UserDao.buscarDoubleOrden(idOrder, "coordenates_x_order_start")
        val start_lat = UserDao.buscarDoubleOrden(idOrder, "coordenates_y_order_start")
        val end_lat = UserDao.buscarDoubleOrden(idOrder, "coordenates_y_order_end")
        val end_lng = UserDao.buscarDoubleOrden(idOrder, "coordenates_x_order_end")
        val start = "${start_lng},${start_lat}"
        val end = "${end_lng},${end_lat}"
        val inicio = LatLng(start_lat!!, start_lng!!) // Punto de inicio
        val destino = LatLng(end_lat!!, end_lng!!) // Punto de destino

        // Agrega el marcador inicial y el destino en el mapa
        mMap.addMarker(MarkerOptions().position(inicio).title("Recolector"))
        mMap.addMarker(MarkerOptions().position(destino).title("Reciclador"))

        // Dibuja la Polyline entre los dos puntos
        /*val polylineOptions = PolylineOptions()
            .add(inicio)
            .add(destino)
            .width(5f)
            .color(Color.BLUE)*/

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
        val pathPoints = coordlist?.map{
            LatLng(it[1], it[0])
        }
        polylineOptions.addAll(pathPoints!!)
        val polyline = mMap.addPolyline(polylineOptions)

        // Mueve la cámara al inicio
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inicio, 15f))

        // Llama a la función para animar el marcador
        if (pathPoints.isNotEmpty()) {
            animateMarkerAlongPath(pathPoints)
        }
    }

    private fun animateMarkerAlongPath(pathPoints: List<LatLng>) {
        if (pathPoints.isEmpty()) return

        val marker = mMap.addMarker(MarkerOptions().position(pathPoints.first()).title("Recolector en movimiento"))

        val animator = ValueAnimator.ofInt(0, pathPoints.size - 1)
        animator.duration = 20000 // Total duration of the animation
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->
            val index = animation.animatedValue as Int

            if (index < pathPoints.size - 1) {
                val start = pathPoints[index]
                val end = pathPoints[index + 1]

                // Interpolate between the current segment
                val fraction = (animation.currentPlayTime % (20000 / pathPoints.size)) / (20000.0 / pathPoints.size)
                val lat = fraction * end.latitude + (1 - fraction) * start.latitude
                val lng = fraction * end.longitude + (1 - fraction) * start.longitude
                val newPosition = LatLng(lat, lng)

                // Move the marker and adjust the camera
                marker?.position = newPosition
                mMap.moveCamera(CameraUpdateFactory.newLatLng(newPosition))
            }
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