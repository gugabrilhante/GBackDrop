package br.brilhante.gustavo.gbackdrop.adapter

import br.brilhante.gustavo.gbackdrop.model.WeatherMockModel

interface WeatherHorizontalAdapterListener {
    fun onWeatherHorizontalItemClick(weatherMockModel: WeatherMockModel, position: Int)
}