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
        holder.timeItem.text=weatherData[position].time?.subSequence(11,16)
        holder.textItem.text=weatherData[position].condition?.text

    }


}