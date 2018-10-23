package br.brilhante.gustavo.gbackdrop.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.brilhante.gustavo.gbackdrop.model.WeatherMockModel
import br.brilhante.gustavo.listbackdrop.extensions.animateAlpha
import br.brilhante.gustavo.listbackdrop.viewgroup.interfaces.BackDropAdapterSelection
import io.olibra.bondapp.modules.remote.adapter.holder.WeatherHorizontalHolder

class WeatherHorizontalAdapter : RecyclerView.Adapter<WeatherHorizontalHolder>(), BackDropAdapterSelection {

    override var viewList: List<View> = emptyList()
    override var indexSelected: Int = 0
    override var emptyHolderHeight: Int = 0

    var listener: WeatherHorizontalAdapterListener? = null

    var weatherCollection: List<WeatherMockModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHorizontalHolder {
        val viewHolder = WeatherHorizontalHolder(parent)
        viewList += viewHolder.itemView

        viewHolder.itemView.setOnClickListener {
            setBackDropSelection(it)
            listener?.onWeatherHorizontalItemClick(
                weatherCollection[viewHolder.adapterPosition],
                viewHolder.adapterPosition
            )
        }

        return viewHolder
    }

    override fun getItemCount(): Int = weatherCollection.size

    override fun onBindViewHolder(holder: WeatherHorizontalHolder, position: Int) {
        val remote = weatherCollection[position]
        holder.setupView(remote)
        if (position == indexSelected) {
            holder.itemView.animateAlpha(1f)
        } else {
            holder.itemView.animateAlpha(0.7f)
        }
    }
}