package io.olibra.bondapp.modules.remote.adapter.holder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.brilhante.gustavo.gbackdrop.R
import br.brilhante.gustavo.gbackdrop.extensions.inflate
import br.brilhante.gustavo.gbackdrop.model.WeatherMockModel
import kotlinx.android.synthetic.main.item_vertical.view.*

class WeatherHorizontalHolder(val parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.item_horizontal, false)) {

    fun setupView(weather: WeatherMockModel) {
        itemView.NameTextView.text = weather.name
    }
}