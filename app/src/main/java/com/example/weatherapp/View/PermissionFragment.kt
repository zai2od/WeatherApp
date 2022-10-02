package com.example.weatherapp.View

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherapp.Controller.WeatherController
import com.example.weatherapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

// TODO: Rename parameter arguments, choose names that match
class PermissionFragment : Fragment() {
lateinit var locationManager:LocationManager
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val view=inflater.inflate(R.layout.fragment_permission2, container, false)
        locationManager =
           activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.context)

        return view
    }
//
//    override fun onStart() {
//        super.onStart()
//       if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
//           locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
//           if (ActivityCompat.checkSelfPermission(
//                   activity as Activity,
//                   Manifest.permission.ACCESS_FINE_LOCATION
//               ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                   activity as Activity,
//                   Manifest.permission.ACCESS_COARSE_LOCATION
//               ) != PackageManager.PERMISSION_GRANTED
//           ) {
//
//               return
//           }
//           MainPage.fusedLocationProviderClient.lastLocation.addOnCompleteListener {
//
//               val location: Location? = it.result
//               if (location != null) {
//                   WeatherController.permissionDeniedOrNot = true
//                   WeatherController.q = "${location.latitude}" + "," + "${location.longitude}"
//                   getParentFragmentManager().beginTransaction().apply {
//                       replace(R.id.nav_host_fragment, HomeFragment())
//                       commit()
//                   }
//               }
//
//           }
//       }
//    }





}