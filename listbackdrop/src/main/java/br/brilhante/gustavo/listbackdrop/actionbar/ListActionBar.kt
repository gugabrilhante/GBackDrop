package br.brilhante.gustavo.listbackdrop.actionbar

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.brilhante.gustavo.listbackdrop.R
import br.brilhante.gustavo.listbackdrop.extensions.animateAlpha
import kotlinx.android.synthetic.main.component_list_action_bar.view.*

class ListActionBar : RelativeLayout {

    var actionButtonState: ActionButtonState = ActionButtonState.BACK_STATE
    lateinit var actionButtonClickListener: ActionButtonClickListener

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        LayoutInflater.from(context)
            .inflate(R.layout.component_list_action_bar, this, true)

        val customProperties = context.obtainStyledAttributes(attrs, R.styleable.BackDrop)

        customProperties.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        leftButton.setProgress(1f)
        leftButton.setProgress(0f)
    }

    fun setLeftActionButtonClickListener(actionButtonClickListener: ActionButtonClickListener) {
        this.actionButtonClickListener = actionButtonClickListener
        leftButton.setOnTouchListener { view: View, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_UP)
                this.actionButtonClickListener.onActionButtonClick(this.actionButtonState)
            true
        }
    }

    fun setListActionAdapter(horizontalAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        val linearLayoutManagerhorizontal = LinearLayoutManager(context)
        linearLayoutManagerhorizontal.orientation = RecyclerView.HORIZONTAL
        actionRecyclerView.layoutManager = linearLayoutManagerhorizontal
        actionRecyclerView.adapter = horizontalAdapter
    }

    inline fun setActionRecyclerViewTouchListener(touchListener: OnTouchListener) {
        actionRecyclerView.setOnTouchListener(touchListener)
    }

    inline fun setLeftButtonTouchListener(touchListener: OnTouchListener) {
        leftButton.setOnTouchListener(touchListener)
    }

    fun animateAlphaTitle(alpha: Float = 1f, duration: Long = 150L) {
        titleTextView.animateAlpha(alpha, duration).start()
    }

    fun animateAlphaList(alpha: Float = 1f, duration: Long = 0) {
        actionRecyclerView.animateAlpha(alpha, duration).start()
    }

    fun setLeftButtonPercentAnimation(percent: Float) {
        leftButton.setProgress(percent)
        actionButtonState = ActionButtonState.ANIMATING
    }

    fun playLeftButtonPercentAnimation() {
        val animator = ValueAnimator.ofFloat(leftButton.progress, 1f)
        animator.addUpdateListener { animation ->
            val percent = animation.animatedValue as Float
            leftButton.setProgress(percent)
            if (percent > 0.9f) {
                actionButtonState = ActionButtonState.CLOSE_STATE
            }
        }
        animator.start()
    }

    fun playReversedLeftButtonPercentAnimation() {
        val animator = ValueAnimator.ofFloat(leftButton.progress, 0f)
        animator.addUpdateListener { animation ->
            val percent = animation.animatedValue as Float
            leftButton.setProgress(percent)
            if (percent < 0.1f) {
                actionButtonState = ActionButtonState.BACK_STATE
            }
        }
        animator.start()
    }

    fun smoothHorizontalScrollToPosition(position: Int) {
        actionRecyclerView.smoothScrollToPosition(position)
    }

    fun showTitle(duration: Long = 0) = animateAlphaList(1f, duration)

    fun showHorizontalList(duration: Long = 0) = animateAlphaTitle(1f, duration)

    fun getRecyclerView() = actionRecyclerView
    fun getLeftButton() = leftButton
    fun getTitleTextView() = titleTextView
}
