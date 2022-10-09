package com.example.weatherapp.View

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.Controller.WeatherController
import com.example.weatherapp.Moudle.WeatherData
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityGoogleMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso

class GoogleMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private  lateinit var mCenterMarker: Marker
    var zaid=false
    private lateinit var loc: LatLng
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGoogleMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoogleMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(
            WeatherController.latLocation.toDouble(),
            WeatherController.longLocation.toDouble()
        )
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Jordan"))
        mCenterMarker= mMap.addMarker(MarkerOptions().position(sydney).visible(false).title(""))!!
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18.0f));
        mMap.setOnCameraMoveListener(OnCameraMoveListener {
           // mCenterMarker.isVisible=true
               //     mCenterMarker.position = mMap.cameraPosition.target
        }

        )
        mMap.setOnCameraIdleListener {

            WeatherController.getData2("${mMap.cameraPosition.target.latitude}"+","+ "${mMap.cameraPosition.target.longitude}",onDone = {
                if(it != null){
                    showDialogOnGoogleMap(it)
                }
            })


        }
        mMap.setOnMarkerClickListener(
            object : GoogleMap.OnMarkerClickListener {
                @SuppressLint("SetTextI18n", "InflateParams")
                override fun onMarkerClick(p0: Marker): Boolean {

                    val dialog = BottomSheetDialog(this@GoogleMapActivity)

                    val view = layoutInflater.inflate(R.layout.bottom_dialog_googlemap, null)
                    val locationName = view.findViewById<TextView>(R.id.locationBottomDialog)
                    val TextName = view.findViewById<TextView>(R.id.textStatusBottomDialog)
                    val temp = view.findViewById<TextView>(R.id.temp_statusBottomDialog)
                    val date = view.findViewById<TextView>(R.id.dateTextBottomDialog)
                    val image = view.findViewById<ImageView>(R.id.ImageHomeBottomDialog)
                    val wind = view.findViewById<TextView>(R.id.windSpeedTextBottomDialog)
                    val maxTemp = view.findViewById<TextView>(R.id.maxTempTextBottomDialog)
                    val minTemp = view.findViewById<TextView>(R.id.minTempTextBottomDialog)


                    HomeFragment.weatherobj.location?.let {
                        locationName.text = it.region + "-" + it.name
                        val localTime = it.localtime ?: return@let
                        if (localTime.length >= 9) {
                            date.text = localTime.subSequence(0, 10)
                        }
                    }
                    Picasso.get().load("https:" + HomeFragment.weatherobj.current?.condition?.icon)
                        .into(image)

                    HomeFragment.weatherobj.current?.let {
                        temp.text = it.tempC.toString()
                        wind.text = it.windKph.toString() + "kph"
                        TextName.text = it.condition?.text
                    }

                    HomeFragment.weatherobj.forecast?.let {
                        if (it.forecastday.isNotEmpty()) {
                            maxTemp.text = it.forecastday[0].day?.mintempC.toString() + "c"
                            minTemp.text = it.forecastday[0].day?.maxtempC.toString() + "c"
                        }
                    }

                    dialog.setContentView(view)

                    dialog.show()

                    return false


                }
            }
        )
    }

    private fun showDialogOnGoogleMap(data: WeatherData) {


                    val dialog = BottomSheetDialog(this@GoogleMapActivity)

                    val view = layoutInflater.inflate(R.layout.bottom_dialog_googlemap, null)
                    val locationName = view.findViewById<TextView>(R.id.locationBottomDialog)
                    val TextName = view.findViewById<TextView>(R.id.textStatusBottomDialog)
                    val temp = view.findViewById<TextView>(R.id.temp_statusBottomDialog)
                    val date = view.findViewById<TextView>(R.id.dateTextBottomDialog)
                    val image = view.findViewById<ImageView>(R.id.ImageHomeBottomDialog)
                    val wind = view.findViewById<TextView>(R.id.windSpeedTextBottomDialog)
                    val maxTemp = view.findViewById<TextView>(R.id.maxTempTextBottomDialog)
                    val minTemp = view.findViewById<TextView>(R.id.minTempTextBottomDialog)


                    data.location?.let {
                        locationName.text = it.region + "-" + it.name
                        val localTime = it.localtime ?: return@let
                        if (localTime.length >= 9) {
                            date.text = localTime.subSequence(0, 10)
                        }
                    }
                    Picasso.get().load("https:" + data.current?.condition?.icon)
                        .into(image)

                    data.current?.let {
                        temp.text = it.tempC.toString()
                        wind.text = it.windKph.toString() + "kph"
                        TextName.text = it.condition?.text
                    }

                    data.forecast?.let {
                        if (it.forecastday.isNotEmpty()) {
                            maxTemp.text = it.forecastday[0].day?.mintempC.toString() + "c"
                            minTemp.text = it.forecastday[0].day?.maxtempC.toString() + "c"
                        }
                    }

                    dialog.setContentView(view)

                    dialog.show()
    }

}