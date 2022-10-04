package com.example.weatherapp.View

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.Controller.HomeRecycleAdapter
import com.example.weatherapp.Controller.WeatherController
import com.example.weatherapp.Moudle.WeatherApiInterface
import com.example.weatherapp.Moudle.WeatherData
import com.example.weatherapp.R
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeFragment : Fragment() {
    private lateinit var recycleView: RecyclerView
    private lateinit var recyclerAdapter: HomeRecycleAdapter
    private lateinit var locationText: TextView
    private lateinit var temp_Status: TextView
    private lateinit var dateText: TextView
    private lateinit var textStatus: TextView
    private lateinit var windSpeedText: TextView
    private lateinit var maxTempText: TextView
    private lateinit var minTempText: TextView
    private lateinit var imageHome: ImageView
    private lateinit var linearLayoutRecycleView: LinearLayout
    private lateinit var windImage: ImageView
    private lateinit var arrowUpImage: ImageView
    private lateinit var arrowDownImage: ImageView
    private lateinit var requestPermissiom: Button
    private lateinit var locationIcon: ImageButton

    companion object {
        var waitForEverythingToFinish = false
        lateinit var weatherobj:WeatherData
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        intiPage(view)
        if (WeatherController.permissionDeniedOrNot) {
            WeatherController.getData(onDone = {
                if (it != null) {
                    getEveryThingTogether(it)
                }
            })
        } else {
            PermissionDenied(WeatherController.permissionDeniedOrNot)
        }
        requestPermissiom.setOnClickListener() {
            val intent = Intent(view.context, MainPage::class.java)
            startActivity(intent)
            activity?.finish()
//            val mainPage = MainPage()
//            mainPage.requestPermission()
//    PermissionDenied(WeatherController.permissionDeniedOrNot)

        }
        locationIcon.setOnClickListener() {
            val intent = Intent(activity, GoogleMapActivity::class.java)
            startActivity(intent)
        }



        return view
    }

    private fun PermissionDenied(permission: Boolean) {
        if (!permission) {
            locationText.text = "Need Permission To Continue"
            requestPermissiom.visibility = View.VISIBLE
            temp_Status.visibility = View.INVISIBLE
            dateText.visibility = View.INVISIBLE
            imageHome.visibility = View.INVISIBLE
            textStatus.visibility = View.INVISIBLE
            windSpeedText.visibility = View.INVISIBLE
            maxTempText.visibility = View.INVISIBLE
            minTempText.visibility = View.INVISIBLE
            linearLayoutRecycleView.visibility = View.INVISIBLE
            windImage.visibility = View.INVISIBLE
            arrowUpImage.visibility = View.INVISIBLE
            arrowDownImage.visibility = View.INVISIBLE

        } else {
            WeatherController.getData(onDone = {
                if (it != null) {

                    getEveryThingTogether(it)
                }
            })
            locationText.text = "Need Permission To Continue"
            requestPermissiom.visibility = View.INVISIBLE
            temp_Status.visibility = View.VISIBLE
            dateText.visibility = View.VISIBLE
            imageHome.visibility = View.VISIBLE
            textStatus.visibility = View.VISIBLE
            windSpeedText.visibility = View.VISIBLE
            maxTempText.visibility = View.VISIBLE
            minTempText.visibility = View.VISIBLE
            linearLayoutRecycleView.visibility = View.VISIBLE
            windImage.visibility = View.VISIBLE
            arrowUpImage.visibility = View.VISIBLE
            arrowDownImage.visibility = View.VISIBLE
        }

    }

    private fun getEveryThingTogether(weatherData: WeatherData) {
weatherobj=weatherData
        weatherData.location?.let {
            locationText.text = it.region + "-" + it.name
            val localTime = it.localtime ?: return@let
            if (localTime.length >= 9) {
                dateText.text = localTime.subSequence(0, 10)
            }
        }
        Picasso.get().load("https:" + weatherData.current?.condition?.icon).into(imageHome)

        weatherData.current?.let {
            temp_Status.text = it.tempC.toString()
            windSpeedText.text = it.windKph.toString() + "kph"
            textStatus.text = it.condition?.text
        }

        weatherData.forecast?.let {
            if (it.forecastday.isNotEmpty()) {
                minTempText.text = it.forecastday[0].day?.mintempC.toString() + "c"
                maxTempText.text = it.forecastday[0].day?.maxtempC.toString() + "c"
            }
            recyclerAdapter = HomeRecycleAdapter(it.forecastday[0].hour)
            recycleView.adapter = recyclerAdapter
        }
        waitForEverythingToFinish = true

    }


    private fun intiPage(view: View) {
        println("odeh was here")
        recycleView = view.findViewById(R.id.RecycleViewHomePage)
        recycleView.layoutManager = LinearLayoutManager(context)

        recycleView.setHasFixedSize(true)
        recycleView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        locationIcon = view.findViewById(R.id.locationIconHomePageFragment)
        requestPermissiom = view.findViewById(R.id.buttonRequestPermission)
        linearLayoutRecycleView = view.findViewById(R.id.linearForRecycleHomePage)
        windImage = view.findViewById(R.id.windIconHomeFragment)
        arrowUpImage = view.findViewById(R.id.arrowUpIconHomeFragment)
        arrowDownImage = view.findViewById(R.id.arrowDownIconHomeFragment)
        textStatus = view.findViewById(R.id.textStatusHomeFragment)
        locationText = view.findViewById(R.id.locationTextHomeFragment)
        temp_Status = view.findViewById(R.id.temp_statusTextHomeFragment)
        dateText = view.findViewById(R.id.dateTextHomeFragment)
        windSpeedText = view.findViewById(R.id.windSpeedTextHomeFragment)
        maxTempText = view.findViewById(R.id.maxTempTextHomeFragment)
        minTempText = view.findViewById(R.id.minTempTextHomeFragment)
        imageHome = view.findViewById(R.id.ImageHomeFragment)

    }


}