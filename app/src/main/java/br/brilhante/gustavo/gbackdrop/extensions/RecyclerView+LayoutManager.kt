package br.brilhante.gustavo.gbackdrop.extensions

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.verticalLinearLayout(context: Context) {
    val linearLayoutManager = LinearLayoutManager(context)
    linearLayoutManager.orientation = RecyclerView.VERTICAL
    this.layoutManager = linearLayoutManager
}

fun RecyclerView.horizontalLinearLayout(context: Context) {
    val linearLayoutManager = LinearLayoutManager(context)
    linearLayoutManager.orientation = RecyclerView.HORIZONTAL
    this.layoutManager = linearLayoutManager
}

fun RecyclerView.gridViewLayout(context: Context, columnsNumber: Int) {
    val gridViewLayoutManager = GridLayoutManager(context, columnsNumber, RecyclerView.VERTICAL, false)
    this.hasFixedSize()
    this.layoutManager = gridViewLayoutManager
}