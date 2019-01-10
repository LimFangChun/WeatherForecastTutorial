package com.resocoder.forecastmvvm.ui.weather.current

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import com.resocoder.forecastmvvm.R
import com.resocoder.forecastmvvm.data.network.ApixuWeatherApiService
import com.resocoder.forecastmvvm.data.network.ConnectivityInterceptorImpl
import com.resocoder.forecastmvvm.data.network.WeatherNetworkDataSourceImpl
import com.resocoder.forecastmvvm.internal.glide.GlideApp
import com.resocoder.forecastmvvm.ui.base.ScopeFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopeFragment(), KodeinAware {
    override val kodein by closestKodein()

    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    companion object {
        fun newInstance() = CurrentWeatherFragment()
    }

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentWeatherViewModel::class.java)

        bindUI()
    }

    private fun bindUI()= launch{
        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        //observe from live data view model
        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            if(it == null){
                return@Observer
            }

            //hide progress bar and loading text
            group_loading.visibility = View.GONE

            updateLocation("KL")
            updateDate()
            updateTemperature(it.temperature, it.feelsLikeTemperature)
            updatePrecipitation(it.precipitationVolume)
            updateCondition(it.conditionText)
            updateWind(it.windDirection, it.windSpeed)
            updateVisibility(it.visibilityDistance)

            GlideApp.with(this@CurrentWeatherFragment)
                    .load("http:${it.conditionIconUrl}")
                    .into(imageView_condition_icon)
        })

        weatherLocation.observe(this@CurrentWeatherFragment, Observer {location ->
            if(location == null){
                return@Observer
            }

            updateLocation(location = location.name)
        })
    }

    private fun getLocalizedUnitAbbreviation(metric:String, imperial:String):String{
        return if(viewModel.isMetric) metric else imperial
    }

    private fun updateLocation(location:String){
        (activity as AppCompatActivity).supportActionBar?.title = location
    }

    private fun updateDate(){
        (activity as AppCompatActivity).supportActionBar?.subtitle = "Today"
    }

    @SuppressLint("SetTextI18n")
    private fun updateTemperature(temperature:Double, feelsLike:Double){
        val unitAbbreviation = getLocalizedUnitAbbreviation("°C", "°F")
        textView_temperature.text = "$temperature$unitAbbreviation"
        textView_feels_like_temperature.text = "Feels like $feelsLike$unitAbbreviation"
    }

    private fun updateCondition(condition:String){
        textView_condition.text = condition
    }

    @SuppressLint("SetTextI18n")
    private fun updatePrecipitation(precipitation:Double){
        val unitAbbreviation = getLocalizedUnitAbbreviation("mm", "in")
        textView_precipitation.text = "Precipitation: $precipitation $unitAbbreviation"
    }

    @SuppressLint("SetTextI18n")
    private fun updateWind(windDirection:String, wind:Double){
        val unitAbbreviation = getLocalizedUnitAbbreviation("kph", "mph")
        textView_wind.text = "Wind: $windDirection, $wind $unitAbbreviation"
    }

    @SuppressLint("SetTextI18n")
    private fun updateVisibility(visibility:Double){
        val unitAbbreviation = getLocalizedUnitAbbreviation("km", "mi.")
        textView_visibility.text = "Visibility: $visibility$unitAbbreviation"
    }
}
