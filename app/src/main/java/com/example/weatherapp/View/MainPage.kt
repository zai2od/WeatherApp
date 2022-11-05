package com.example.weatherapp.View

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weatherapp.Controller.ConnectionLiveData
import com.example.weatherapp.Controller.WeatherController
import com.example.weatherapp.R
import com.google.android.gms.location.*


class MainPage : AppCompatActivity() {
    lateinit var swip: SwipeRefreshLayout
    lateinit var progressBar: ProgressBar
    private lateinit var checker:ConnectionLiveData

    companion object {

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
            Handler().postDelayed({
                getCurrentLocation()
                swip.isRefreshing = false
            }, 2000)
        }
        checkInternetAdvanced()

    }

    private fun checkInternetAdvanced() {
        checker= ConnectionLiveData(application)
        checker.observe(this,{isConnected ->
            if(isConnected){
                getCurrentLocation()
            }else{
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.nav_host_fragment, InternetErrorMessage())
                    commit()
                }
            }

        })
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
                        if (checkNetwork(this)) {
                            supportFragmentManager.beginTransaction().apply {
                                replace(R.id.nav_host_fragment, HomeFragment())
                                commit()
                            }
                        } else {
                            supportFragmentManager.beginTransaction().apply {
                                replace(R.id.nav_host_fragment, InternetErrorMessage())
                                commit()
                            }
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
                            if (checkNetwork(this)) {
                                supportFragmentManager.beginTransaction().apply {
                                    replace(R.id.nav_host_fragment, HomeFragment())
                                    commit()
                                    if (location != null) {
                                        progressBar.visibility = View.INVISIBLE
                                    }
                                }

                            } else {
                                progressBar.visibility = View.INVISIBLE
                                supportFragmentManager.beginTransaction().apply {
                                    replace(R.id.nav_host_fragment, InternetErrorMessage())
                                    commit()
                                    if (location != null) {
                                        progressBar.visibility = View.INVISIBLE
                                    }
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
                if (checkNetwork(this)) {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.nav_host_fragment, HomeFragment())
                        commit()
                    }

                } else {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.nav_host_fragment, InternetErrorMessage())
                        commit()
                    }
                }

                return
            }
        }
    }

    //
    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onstart Called")
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
                    if (checkNetwork(this)) {
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.nav_host_fragment, HomeFragment())
                            commit()

                        }

                    } else {
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.nav_host_fragment, InternetErrorMessage())
                            commit()
                        }
                    }
                }

            }
        } else if (!isLocationEnabled() && checkPermissions()) {
            val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(intent, LOCATION)
        }
    }

    fun checkNetwork(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume Called")

    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause Called")
    }

}


