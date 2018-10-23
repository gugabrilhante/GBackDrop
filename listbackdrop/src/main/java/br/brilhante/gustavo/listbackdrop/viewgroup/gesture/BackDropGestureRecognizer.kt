package br.brilhante.gustavo.listbackdrop.viewgroup.gesture

import android.view.MotionEvent
import android.view.View
import br.brilhante.gustavo.listbackdrop.viewgroup.enums.BackDropScroll
import io.olibra.bonduicomponents.ui.viewgroup.ObservableDragLayout
import kotlin.math.absoluteValue

class BackDropGestureRecognizer(val backView: ObservableDragLayout, val sheet: View) {
    var differenceY: Float = 0.0f
    var actionDownRawY: Float = 0.0f

    var LastMoveEventY: Float? = null

    var toleranceDistance = 100f

    fun getTopActionAlpha(): Float {
        return 1.0f - ((sheet.y - backView.y) / (backView.height - backView.y))
    }

    fun getBackListAlpha(): Float {
        return (sheet.y - backView.y) / (backView.height - backView.y)
    }

    fun getMovePosition(event: MotionEvent): Float {
        return event.rawY - differenceY
    }

    fun getLastPointDifference(event: MotionEvent): Float {
        this.LastMoveEventY?.let {
            return event.y - it
        }
        return 0.0f
    }

    /*
    * this method calculate the next move distance based on the diference o Y from current and last event
    * */
    fun getMovePointPosition(event: MotionEvent): Float {
        return sheet.y + getLastPointDifference(event)
    }

    fun getDirectionEvents(eventBefore: MotionEvent, currentevent: MotionEvent): BackDropScroll {
        val distanceY = (currentevent.y - eventBefore.y).absoluteValue
        val distanceX = (currentevent.x - eventBefore.x).absoluteValue

        if (distanceX < toleranceDistance && distanceY < toleranceDistance) return BackDropScroll.NOT_SCROLLING

        return when {
            distanceY < distanceX -> BackDropScroll.HORIZONTAL_SCROLLING
            distanceY > distanceX -> BackDropScroll.VERTICAL_SCROLLING
            else -> BackDropScroll.NOT_SCROLLING
        }
    }

    fun updateDiferenceY(event: MotionEvent) {
        differenceY = event.rawY - sheet.y
    }

    fun setFirstActionDown(event: MotionEvent) {
        actionDownRawY = event.rawY
        updateDiferenceY(event)
    }
}