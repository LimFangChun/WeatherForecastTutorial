package com.resocoder.forecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.resocoder.forecastmvvm.data.db.entity.WeatherLocation
import com.resocoder.forecastmvvm.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry

interface ForecastRepository {
    suspend fun getCurrentWeather(isMetric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>

    suspend fun getWeatherLocation():LiveData<WeatherLocation>
}