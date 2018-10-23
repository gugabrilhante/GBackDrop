package br.brilhante.gustavo.listbackdrop.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import br.brilhante.gustavo.listbackdrop.extensions.animateAlpha

class SheetView(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    private lateinit var insideView: View

    val alphaBottomLimit: Float = 0.4f

    override fun onFinishInflate() {
        super.onFinishInflate()
        insideView = getInsideView()
    }

    fun fadeOutContentView(duration: Long = 150L) {
        insideView.animateAlpha(alphaBottomLimit, duration)
    }

    fun fadeInContentView(duration: Long = 150L) {
        insideView.animateAlpha(1f, duration)
    }

    fun animateContentAlpha(alpha: Float = 1f, duration: Long = 0) {
        val alphaTarget = if (alpha < this.alphaBottomLimit) {
            this.alphaBottomLimit
        } else {
            alpha
        }
        insideView.animateAlpha(alphaTarget, duration).start()
    }

    /**
     * Function to return the content view.
     * @return the first view in this layout.
     */
    private fun getInsideView(): View {
        // TODO need to replace this logic later
        var childView = getChildAt(0)
        return childView
    }
}