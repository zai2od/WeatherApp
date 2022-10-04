package com.example.weatherapp.View

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weatherapp.Controller.WeatherController
import com.example.weatherapp.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import java.util.*


class MainPage : AppCompatActivity() {
lateinit var swip:SwipeRefreshLayout
    lateinit var progressBar: ProgressBar

    companion object {
        var getBackToActivity = false
        lateinit var fusedLocationProviderClient: FusedLocationProviderClient
        private const val PERMISSION_ACCESS_REQUEST = 100
        private const val LOCATION = 12
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swip = findViewById(R.id.swipeMainPage)
        progressBar = findViewById(R.id.progressBarMainActivity)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        swip.setOnRefreshListener {
            Handler().postDelayed( {
                getCurrentLocation()
                swip.isRefreshing = false
            }, 2000)
        }
        getCurrentLocation()
    }


    fun getCurrentLocation() {
        if (checkPermissions()) {


            if (isLocationEnabled()) {

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {

                    val location: Location? = it.result
                    if (location != null) {
                        WeatherController.latLocation = location?.latitude.toString()
                        WeatherController.longLocation = location?.longitude.toString()
                        WeatherController.permissionDeniedOrNot = true
                        WeatherController.q = "${location.latitude}" + "," + "${location.longitude}"
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.nav_host_fragment, HomeFragment())
                            commit()
                        }
                    }

                }
            } else {
                val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, LOCATION)

            }


        } else {
            // request permission
            requestPermission()


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION) {

            if (isLocationEnabled()) {
                progressBar.visibility = View.VISIBLE
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                Handler(Looper.getMainLooper()).postDelayed(
                    {

                        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                            println("ALLL GOOOOOOOOOOOOOOOOOOOOOOOOOOOOD")

                            val location: Location? = it.result
                            WeatherController.latLocation = location?.latitude.toString()
                            WeatherController.longLocation = location?.longitude.toString()
                            WeatherController.permissionDeniedOrNot = true
                            WeatherController.q =
                                "${location?.latitude}" + "," + "${location?.longitude}"
                            supportFragmentManager.beginTransaction().apply {
                                replace(R.id.nav_host_fragment, HomeFragment())
                                commit()
                                if (location != null) {
                                    progressBar.visibility = View.INVISIBLE
                                }
                            }

                        }.addOnFailureListener() {
                            Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
                        }
                    },
                    3000 // value in milliseconds
                )

            }
        }
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ACCESS_REQUEST
        )
        return
    }
    fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ACCESS_REQUEST) {

            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            ) {
                getCurrentLocation()
            } else {
                WeatherController.permissionDeniedOrNot = false
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.nav_host_fragment, HomeFragment())
                    commit()
                }
            }
        }
        return
    }
//
    override fun onStart() {
        super.onStart()

        if (isLocationEnabled() && checkPermissions()) {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission()
                return
            }
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {

                val location: Location? = it.result
                if (location != null) {
                    WeatherController.latLocation = location?.latitude.toString()
                    WeatherController.longLocation = location?.longitude.toString()
                    WeatherController.permissionDeniedOrNot = true
                    WeatherController.q = "${location.latitude}" + "," + "${location.longitude}"
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.nav_host_fragment, HomeFragment())
                        commit()
                    }
                }

            }}else if(!isLocationEnabled() && checkPermissions()){
            val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(intent, LOCATION)
        }
    }
//
//    override fun onResume() {
//        super.onResume()
//       this.onCreate(null)
//    }


}


