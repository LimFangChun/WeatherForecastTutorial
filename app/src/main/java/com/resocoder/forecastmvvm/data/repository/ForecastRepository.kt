package com.resocoder.forecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.resocoder.forecastmvvm.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry

interface ForecastRepository {
    suspend fun getCurrentWeather(isMetric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>
}