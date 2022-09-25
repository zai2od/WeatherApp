package com.example.weatherapp.View

import android.Manifest
import android.R.attr.x
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weatherapp.Controller.WeatherController
import com.example.weatherapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.NumberFormat
import java.util.*


class MainPage : AppCompatActivity() {


    companion object {
        private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
        private const val PERMISSION_ACCESS_REQUEST = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

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
                        WeatherController.permissionDeniedOrNot = true
                        WeatherController.q = "${location.latitude}" + "," + "${location.longitude}"
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.nav_host_fragment, HomeFragment())
                            commit()
                        }
                    }

                }


            } else {
                //setting open here
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
                finish()
               if(isLocationEnabled()){
                   fusedLocationProviderClient.lastLocation.addOnCompleteListener {

                       val location: Location? = it.result
                       if (location != null) {
                           WeatherController.permissionDeniedOrNot = true
                           WeatherController.q = "${location.latitude}" + "," + "${location.longitude}"
                           supportFragmentManager.beginTransaction().apply {
                               replace(R.id.nav_host_fragment, HomeFragment())
                               commit()
                           }
                       }

                   }
               }

            }
        } else {
            //request permission
            requestPermission()


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

    private fun isLocationEnabled(): Boolean {
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

    override fun onRestart() {
        super.onRestart()
    }

}


