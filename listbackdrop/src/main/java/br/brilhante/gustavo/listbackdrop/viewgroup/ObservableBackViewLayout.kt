package io.olibra.bonduicomponents.ui.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * TODO: document your custom view class.
 */
class ObservableDragLayout(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    var touchListenerBackView: onBackViewInterceptTouchListener? = null

    lateinit var topActionView: ListActionBar

    lateinit var backListView: RecyclerView

    lateinit var horizontalRecyclerView: RecyclerView

    var horizontalScrollEnabled = true

    init {
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        topActionView = getTopView() as ListActionBar
        backListView = getbackListView() as RecyclerView
        // if there is less than one views, crash the execution
        if (childCount < 1) {
            throw IllegalArgumentException(" ${this.javaClass.simpleName} Must contain at least one child!")
        }
        topActionView.setActionRecyclerViewTouchListener(View.OnTouchListener { _: View, event: MotionEvent ->
            onInterceptTouchEvent(event)
            !horizontalScrollEnabled
        })
        topActionView.setLeftButtonTouchListener(View.OnTouchListener { _: View, _: MotionEvent ->
            true
        })
    }

    fun setListActionBarAdapter(horizontalAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        getListActionBar().setListActionAdapter(horizontalAdapter)
    }

    fun setBackVerticalListAdapter(verticalAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        val linearLayoutManagerVertical = LinearLayoutManager(context)
        linearLayoutManagerVertical.orientation = RecyclerView.VERTICAL
        backListView.layoutManager = linearLayoutManagerVertical
        backListView.adapter = verticalAdapter
    }

    fun getTopTouchableViewPosition(): Float {
        return topActionView.y + topActionView.height
    }

    fun getTopViewHeight(): Float {
        return topActionView.height.toFloat()
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        touchListenerBackView?.let {
            if (event != null) {
                val touch = event.y
                val topLimit = topActionView.y
                val bottomLimit = topActionView.y + topActionView.height
                val insideTopView = (touch in topLimit..bottomLimit)

                it.onInterceptTouch(event, insideTopView)
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    fun touchInsideTopActionView(event: MotionEvent): Boolean {
        val touch = event.y
        val topLimit = topActionView.y
        val bottomLimit = topActionView.y + (2 * topActionView.height)
        return (touch in topLimit..bottomLimit)
    }

    fun smoothHorizontalScrollToPosition(position: Int) {
        topActionView.smoothHorizontalScrollToPosition(position)
    }

    fun getListActionBar(): ListActionBar = topActionView as ListActionBar

    private fun getTopView(): View = getChildAt(0)

    private fun getbackListView(): View = getChildAt(1)
}

interface onBackViewInterceptTouchListener {
    fun onInterceptTouch(event: MotionEvent, insideTopView: Boolean)
}
