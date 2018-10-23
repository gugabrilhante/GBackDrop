package br.brilhante.gustavo.gbackdrop.adapter

import br.brilhante.gustavo.gbackdrop.model.WeatherMockModel

interface WeatherVerticalAdapterListener {
    fun onWeatherVerticalItemClick(weatherMockModel: WeatherMockModel, position: Int)
}