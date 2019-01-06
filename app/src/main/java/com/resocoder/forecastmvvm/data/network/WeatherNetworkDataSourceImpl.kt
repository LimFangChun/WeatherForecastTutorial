package com.resocoder.forecastmvvm.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.resocoder.forecastmvvm.data.network.response.CurrentWeatherResponse
import com.resocoder.forecastmvvm.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
        private val apixuWeatherApiService: ApixuWeatherApiService
) : WeatherNetworkDataSource {

    private val _downloadCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadCurrentWeather


    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
        try {
            val fetchedCurrentWeather = apixuWeatherApiService
                    .getCurrentWeather(location, languageCode)
                    .await()

            _downloadCurrentWeather.postValue(fetchedCurrentWeather)
        } catch (e: NoConnectivityException) {
            e.printStackTrace()
        }
    }
}