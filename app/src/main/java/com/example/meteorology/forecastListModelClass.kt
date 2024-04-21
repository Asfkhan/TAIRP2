package com.example.meteorology

class forecastListModelClass ( private var day: Int,
private var icon: String,
private var avgtemp: String,
private var mintemp: String,
private var maxtemp: String
) {
    fun getday (): Int {
        return day
    }
    fun setday(day: Int) {
        this.day = day
    }
    fun geticon (): String {
        return icon
    }
    fun seticon(icon: String) {
        this.icon = icon
    }
    fun getavgtemp (): String {
        return avgtemp
    }
    fun setavgtemp(avgtemp: String) {
        this.avgtemp = avgtemp
    }
    fun getmintemp (): String {
        return mintemp
    }
    fun setmintemp(mintemp: String) {
        this.mintemp = mintemp
    }
    fun getmaxtemp (): String {
        return maxtemp
    }
    fun setmaxtemp(maxtemp: String) {
        this.maxtemp = maxtemp
    }
}