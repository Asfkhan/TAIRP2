package com.example.meteorology

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.Locale


class  MainActivity : AppCompatActivity() {
    private var PERMISSION_CODE: Int = 1
//    private lateinit var cityName: String
    private lateinit var searchView: ImageView
    private lateinit var currentWeatherIcon: ImageView
    private lateinit var cityNameTextView: TextView
    private lateinit var currentSeason: TextView
    private lateinit var pbload: ProgressBar
    private lateinit var home: RelativeLayout
    private var forecastListModelClass: ArrayList<forecastListModelClass> = arrayListOf()
    lateinit var locationManager: LocationManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var viewPager2: ViewPager2
    private lateinit var currentTemperature: TextView
    lateinit var backIDObj: ImageView
    lateinit var weatheradapter: MyAdapter
    lateinit var geocoder : Geocoder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this@MainActivity)

        val getcityEditText: EditText = findViewById(R.id.editTextTextID)
        searchView = findViewById(R.id.searchViewID)
        cityNameTextView = findViewById(R.id.currentCityID)
        pbload = findViewById(R.id.pBIDLoading)
        home = findViewById(R.id.homeRLID)
        backIDObj = findViewById(R.id.backID)
        currentSeason = findViewById(R.id.currentSeasonID)
        currentTemperature = findViewById(R.id.currentTemperatureID)
        currentWeatherIcon = findViewById(R.id.currentWeatherIconID)
        geocoder = Geocoder(baseContext, Locale.getDefault())
        viewPager2 = findViewById(R.id.viewpagerID)
        weatheradapter = MyAdapter(this@MainActivity, forecastListModelClass)
        viewPager2.adapter = weatheradapter

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                PERMISSION_CODE
            )
        }
        val location: android.location.Location? =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        if (location != null) {

            val cityName = getCityName(location.longitude, location.latitude)
            println(cityName)
            Toast.makeText(this@MainActivity,"City Name : $cityName",Toast.LENGTH_LONG).show()
            getweatherinfo(cityName)
        }


        searchView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val city: String = getcityEditText.text.toString()
                if (city.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Please Enter City Name! ", Toast.LENGTH_LONG)
                        .show()
                } else {
                    cityNameTextView.setText(city)
                    getweatherinfo(city)
                }
            }

        })

    }


private fun getCityName(longitude: Double, latitude: Double): String {

    var cityName = "Not Found"
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(latitude, longitude, 10) { addresses->
                for (address in addresses) {
                    val city: String = address.locality
                    if (city.isNotEmpty()) {
                        cityName = city
                        break
                    }
                }
                if (cityName == "Not Found") {
                    Log.d("MainActivity", "Unable to find city name for the provided coordinates")
                }
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return cityName
}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Permission Granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@MainActivity, "Please Provide Permission", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }



private fun getweatherinfo(cityName: String){
    val url =
        "http://api.weatherapi.com/v1/forecast.json?key=580eee6f95b64cff81f143704241604&q=$cityName&days=5&aqi=yes&alerts=yes"
    cityNameTextView.setText(cityName)
    val requestQueueRequest : RequestQueue = Volley.newRequestQueue(this@MainActivity)
    val jsonObjectRequest = JsonObjectRequest(
        Request.Method.GET, url, null, { response ->
        pbload.visibility = View.GONE
        home.visibility = View.VISIBLE
        forecastListModelClass.clear()

        try {
            val temprature = response.getJSONObject("current").getString("temp_c")
            currentTemperature.setText(temprature)
            val isday : Int = response.getJSONObject("current").getInt("is_day")
            val weathrconditionText = response.getJSONObject("current").getJSONObject("condition").getString("text")
            val weatherconditioniIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon")
            Picasso.get().load("http:".plus(weatherconditioniIcon)).into(currentWeatherIcon)
            if(isday==1){
                Picasso.get().load("https://img.freepik.com/free-vector/sky-realistic-landscape-with-clouds-grass-vector-illustration_1284-83124.jpg?w=740&t=st=1713523560~exp=1713524160~hmac=b3e825dd417f698d6bc6c48b006785f99fb34f479b81c6989f1ec59e923d4409").into(backIDObj)
            }
            else{
                Picasso.get().load("https://img.freepik.com/free-vector/gradient-starry-night-dark-background_23-2148250148.jpg?w=740&t=st=1713524102~exp=1713524702~hmac=3146a5db4159fa6ed0c145f04a74976e45a34a181aeb968248c6d71e74dae721").into(backIDObj)

            }
            val forecastOBJ : JSONObject = response.getJSONObject("forecast")
            val forecastO : JSONArray = forecastOBJ.getJSONArray("forecastday")
            for(i in 0 until forecastO.length()){
                val forcstdayObj : JSONObject = forecastO.getJSONObject(i)
                val avgTempObj : String = forcstdayObj.getJSONObject("day").getString("avgtemp_c")
                val minTempObj : String = forcstdayObj.getJSONObject("day").getString("mintemp_c")
                val maxTempObj : String = forcstdayObj.getJSONObject("day").getString("maxtemp_c")
                val isdayObj : Int = forcstdayObj.getJSONObject("hour").getInt("is_day")
                val isdayConditionIcon : String = forcstdayObj.getJSONObject("hour").getJSONObject("condition").getString("icon")
                forecastListModelClass.add(forecastListModelClass(isdayObj,isdayConditionIcon,avgTempObj,minTempObj,maxTempObj))
            }

            weatheradapter.notifyDataSetChanged()



        }catch (exception: JSONException){
            exception.printStackTrace()
        }
    },
        { error ->
            Toast.makeText(this@MainActivity,"Please Enter Valid City Name!...(╬▔皿▔)╯",Toast.LENGTH_LONG).show()
        })
    requestQueueRequest.add(jsonObjectRequest)
}
}
