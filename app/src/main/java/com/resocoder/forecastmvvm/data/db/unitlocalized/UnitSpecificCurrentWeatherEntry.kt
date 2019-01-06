package com.resocoder.forecastmvvm.data.db.unitlocalized

interface UnitSpecificCurrentWeatherEntry {
    val temperature: Double
    val conditionText: String
    val conditionIconUrl: String
    val windSpeed: Double
    val windDirection: Double
    val precipitationVolume: Double
    val feelsLikeTemperature: Double
    val visibilityDistance: Double
}