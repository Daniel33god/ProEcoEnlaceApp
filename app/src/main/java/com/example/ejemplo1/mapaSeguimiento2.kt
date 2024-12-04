package com.example.ejemplo1

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ejemplo1.CalificacionConductor
import com.example.ejemplo1.R
import com.example.ejemplo1.databinding.ActivityMapaSeguimiento2Binding
import com.example.ejemplo1.databinding.ActivityMapaSeguimientoBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.security.AccessController.getContext

/**
 * A simple [Fragment] subclass.
 * Use the [MapsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class mapaSeguimiento2 : AppCompatActivity() , OnMapReadyCallback{
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val polylineOptions = PolylineOptions()

    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 10000 // Update interval in milliseconds
        fastestInterval = 5000 // Fastest update interval in milliseconds
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapaSeguimiento2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapaSeguimiento2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val ingresarButton2 = findViewById<Button>(R.id.button)
        ingresarButton2.setOnClickListener {
            getCurrentLocation()
            startLocationUpdates()
        }

        requestPermission()
    }

    private fun requestPermission(){
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Do if the permission is granted
            }
            else {
                // Do otherwise
            }
        }
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun addPolylines(){
        val polyline =mMap.addPolyline(polylineOptions)
    }

    private fun startLocationUpdates() {
        if (getContext()?.let {
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && getContext()?.let {
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.lastLocation?.let { location ->
                // Handle the updated location here
                println(location.latitude)
                animateMap(location.latitude, location.longitude)
                polylineOptions.add(LatLng(location.latitude,location.longitude))
                addPolylines()
            }
        }
    }

    private fun getCurrentLocation(){
        fusedLocationClient =  LocationServices.getFusedLocationProviderClient(this)
        if (getContext()?.let {
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && getContext()?.let {
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location->
                if (location != null) {
                    // use your location object
                    // get latitude , longitude and other info from this
                    println(location.latitude)
                }

            }
    }

    private  fun animateMap(latitude: Double, longitude: Double){
        val place = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(place).title("Marcador"))
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(place,18f),
            4000,
            null
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        animateMap(-34.0, 151.0)
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