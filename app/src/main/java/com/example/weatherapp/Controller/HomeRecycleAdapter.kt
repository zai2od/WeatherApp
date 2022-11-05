package com.example.weatherapp.Controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.Moudle.Hour
import com.example.weatherapp.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeRecycleAdapter(var weatherData: ArrayList<Hour>) :
    RecyclerView.Adapter<HomeRecycleAdapter.ViewHolders>() {
    inner class ViewHolders(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageItem: ImageView = itemView.findViewById(R.id.imageInForecastItem)
        val tempItem: TextView = itemView.findViewById(R.id.temp_statusForecastItem)
        val textItem:TextView=itemView.findViewById(R.id.TextStatusForecastItem)
        val timeItem: TextView = itemView.findViewById(R.id.timeInForecastItem)

    }
    override fun getItemCount(): Int {
        return weatherData.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val myView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_in_recycle, parent, false)
        return ViewHolders(myView)
    }
    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        Picasso.get().load("https:"+weatherData[position].condition?.icon).into(holder.imageItem)
        holder.tempItem.text=weatherData[position].tempC.toString()
        val date: Date = SimpleDateFormat("hh:mm").parse(weatherData[position].time?.subSequence(11,16).toString())
        val newDate: String = SimpleDateFormat("h:mm a").format(date)
        holder.timeItem.text=newDate
        holder.textItem.text=weatherData[position].condition?.text
    }


}