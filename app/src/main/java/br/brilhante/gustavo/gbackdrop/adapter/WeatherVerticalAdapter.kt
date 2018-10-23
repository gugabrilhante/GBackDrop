package br.brilhante.gustavo.gbackdrop.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.brilhante.gustavo.gbackdrop.model.WeatherMockModel
import br.brilhante.gustavo.listbackdrop.extensions.animateAlpha
import br.brilhante.gustavo.listbackdrop.viewgroup.interfaces.BackDropAdapterSelection
import io.olibra.bondapp.modules.remote.adapter.holder.WeatherVerticalHolder

class WeatherVerticalAdapter : RecyclerView.Adapter<WeatherVerticalHolder>(), BackDropAdapterSelection {

    override var viewList: List<View> = emptyList()
    override var indexSelected: Int = 0
    override var emptyHolderHeight: Int = 0

    var listener: WeatherVerticalAdapterListener? = null

    var weatherCollection: List<WeatherMockModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherVerticalHolder {
        val viewHolder = WeatherVerticalHolder(parent)
        viewList += viewHolder.itemView

        if (viewHolder.adapterPosition < weatherCollection.size) {
            viewHolder.itemView.setOnClickListener {
                setBackDropSelection(it)
                listener?.onWeatherVerticalItemClick(
                    weatherCollection[viewHolder.adapterPosition],
                    viewHolder.adapterPosition
                )
            }
        } else {
            emptyHolderHeight = viewHolder.itemView.height
        }

        return viewHolder
    }

    override fun getItemCount(): Int = weatherCollection.size + 1

    override fun onBindViewHolder(holder: WeatherVerticalHolder, position: Int) {
        if (position < weatherCollection.size) {
            val remote = weatherCollection[position]
            holder.setupView(remote)
            if (position == indexSelected) {
                holder.itemView.animateAlpha(1f)
            } else {
                holder.itemView.animateAlpha(0.7f)
            }
        } else {
            holder.setupEmpty()
            emptyHolderHeight = holder.itemView.height
        }
    }

    override fun onViewAttachedToWindow(holder: WeatherVerticalHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder.adapterPosition >= weatherCollection.size) {
            emptyHolderHeight = holder.itemView.height
        }
    }
}