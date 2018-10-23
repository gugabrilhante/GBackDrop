package br.brilhante.gustavo.listbackdrop.viewgroup.interfaces

import android.view.View
import br.brilhante.gustavo.listbackdrop.extensions.animateAlpha

interface BackDropAdapterSelection {
    var viewList: List<View>
    var indexSelected: Int

    /*this is the limit that backdrop goes down, set the value of an empty holder on the bottom of the
    *recyclerview, this way the card wont disappear and will not cover any cell
    */
    var emptyHolderHeight: Int

    fun setBackDropSelection(clickedView: View) {
        for ((index, view) in viewList.withIndex()) {
            if (view == clickedView) {
                view.animateAlpha(1f)
                indexSelected = index
            } else {
                view.animateAlpha(0.7f)
            }
        }
    }

    fun selectItemByIndex(selectIndex: Int) {
        indexSelected = selectIndex
        for ((index, view) in viewList.withIndex()) {
            if (index == selectIndex) {
                view.animateAlpha(1f)
            } else {
                view.animateAlpha(0.7f)
            }
        }
    }
}