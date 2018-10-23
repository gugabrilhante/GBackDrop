package br.brilhante.gustavo.listbackdrop.extensions

import android.view.View

fun View.topY(): Float {
    return this.y
}

fun View.bottomY(): Float {
    return this.y + this.height
}

fun View.middleY(): Float {
    return this.bottomY() / 2
}

fun View.startX(): Float {
    return this.x
}

fun View.endX(): Float {
    return this.x + this.width
}

fun View.middleX(): Float {
    return endX() / 2
}