package br.brilhante.gustavo.gbackdrop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.brilhante.gustavo.gbackdrop.adapter.WeatherHorizontalAdapter
import br.brilhante.gustavo.gbackdrop.adapter.WeatherHorizontalAdapterListener
import br.brilhante.gustavo.gbackdrop.adapter.WeatherVerticalAdapter
import br.brilhante.gustavo.gbackdrop.adapter.WeatherVerticalAdapterListener
import br.brilhante.gustavo.gbackdrop.model.WeatherMockModel
import kotlinx.android.synthetic.main.activity_back_drop.*

class BackDropActivity : AppCompatActivity(), WeatherHorizontalAdapterListener, WeatherVerticalAdapterListener {
    override fun onWeatherHorizontalItemClick(weatherMockModel: WeatherMockModel, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onWeatherVerticalItemClick(weatherMockModel: WeatherMockModel, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val horizontalAdapter = WeatherHorizontalAdapter()
    val verticalAdapter = WeatherVerticalAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_back_drop)
        setRecyclerView(mockweatherList())
    }

    /*
    * Backdrop cant see the type of recyclerview adapter, so its necessary to force downcast to RecyclerView.Adapter<RecyclerView.ViewHolder>
    * */
    @Suppress("UNCHECKED_CAST")
    fun setRecyclerView(list: List<WeatherMockModel>) {
        horizontalAdapter.weatherCollection = list
        verticalAdapter.weatherCollection = list

        horizontalAdapter.listener = this
        verticalAdapter.listener = this

        backdropView.setup(
            horizontalAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>,
            verticalAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
        )
    }

    fun mockweatherList(): List<WeatherMockModel> {
        var list: List<WeatherMockModel> = emptyList()
        list += (WeatherMockModel(R.drawable.cloud, "Cloud"))
        list += (WeatherMockModel(R.drawable.cloud_moon, "Cloud Night"))
        list += (WeatherMockModel(R.drawable.cloud_sun, "Cloud and Sun"))
        list += (WeatherMockModel(R.drawable.rain, "Rain"))
        list += (WeatherMockModel(R.drawable.snow, "Snow"))
        list += (WeatherMockModel(R.drawable.storm, "Storm"))
        return list
    }
}
