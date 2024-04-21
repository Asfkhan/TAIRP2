package com.example.meteorology
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MyAdapter(val context:Activity, val apidataitems: ArrayList<forecastListModelClass>): RecyclerView.Adapter<HolderView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        return HolderView(LayoutInflater.from(context).inflate(R.layout.forecast_list,parent,false))
    }

    override fun getItemCount(): Int {
        return apidataitems.size
    }

    override fun onBindViewHolder(holder: HolderView, position: Int) {
        val apiItems = apidataitems[position]
        holder.daystitle.text = apiItems.getday().toString()
        holder.daysavgtemp.text = apiItems.getavgtemp()
        holder.daysminTemp.text = apiItems.getmintemp()
        holder.daysmaxTemp.text = apiItems.getmaxtemp()
        Picasso.get().load("http:".plus(apiItems.geticon())).into(holder.daysweatherIcon)
    }

}
class HolderView(itemview: View) : RecyclerView.ViewHolder(itemview){
    val daystitle: TextView
    val daysweatherIcon: ImageView
    val daysavgtemp: TextView
    val daysminTemp: TextView
    val daysmaxTemp: TextView

    init {
        daystitle = itemview.findViewById(R.id.dayTextViewID)
        daysweatherIcon = itemview.findViewById(R.id.dayswheatherIconID)
        daysavgtemp = itemview.findViewById(R.id.avgtempID)
        daysminTemp = itemview.findViewById(R.id.minTemp_C)
        daysmaxTemp = itemview.findViewById(R.id.maxTemp_C)
    }
}