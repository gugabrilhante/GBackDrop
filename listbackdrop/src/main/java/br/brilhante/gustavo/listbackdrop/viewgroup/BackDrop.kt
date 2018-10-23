package io.olibra.bonduicomponents.ui.viewgroup

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import br.brilhante.gustavo.listbackdrop.viewgroup.enums.BackDropScroll
import br.brilhante.gustavo.listbackdrop.viewgroup.gesture.BackDropGestureRecognizer

class BackDrop(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet),
    onBackViewInterceptTouchListener, GestureDetector.OnGestureListener {
    private lateinit var backView: ObservableDragLayout
    private lateinit var sheet: SheetView
    private lateinit var backDropGestureRecognizer: BackDropGestureRecognizer
    private lateinit var mDetector: GestureDetectorCompat
    private lateinit var horizontalRecyclerView: RecyclerView
    private lateinit var titleTextView: TextView
    private lateinit var verticalAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>

    private var interpolator = LinearInterpolator()

    private var movementStarted = false
    private var backdropOpen = false
    private var sheetShown = false

    private var backDropScrolling: BackDropScroll = BackDropScroll.NOT_SCROLLING

    companion object {
        const val ANIM_DURATION = 200L
        private var BACKDROP_SPACE = 100
    }

    init {
    }

    fun setup(
        horizontalAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        verticalAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    ) {
        this.verticalAdapter = verticalAdapter
        backView.setListActionBarAdapter(horizontalAdapter)
        backView.setBackVerticalListAdapter(verticalAdapter)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setupListeners() {
        backView.getListActionBar().setLeftActionButtonClickListener(object : ActionButtonClickListener {
            override fun onActionButtonClick(actionButtonState: ActionButtonState) {
                when (actionButtonState) {
                    ActionButtonState.BACK_STATE -> (context as? Activity)?.finish()
                    ActionButtonState.CLOSE_STATE -> animateClose()
                }
            }
        })
        sheet.setOnTouchListener { _: View, _: MotionEvent ->
            if (backdropOpen) {
                animateClose()
            }
            true
        }
    }

    /**
     * Here we check if there is more than two child views.
     * If true, we throw an exception in runtime.
     * @throws IllegalArgumentException if there is more than two child views.
     *
     * And change the front view background color.
     */
    override fun onFinishInflate() {
        super.onFinishInflate()

        // if there is more than two views, crash the execution
        if (childCount > 2) {
            throw IllegalArgumentException(" ${this.javaClass.simpleName} Must contain only two child!")
        }

        // if the back view is not ObservableDragLayout the backdrop wont work properly and the app should break
        if (getBackView() !is ObservableDragLayout) {
            throw IllegalArgumentException(" ${this.javaClass.simpleName} The fist view must be of type ObservableDragLayout")
        }

        // if the sheet view is not SheetView the backdrop wont work properly and the app should break
        if (getFrontView() !is SheetView) {
            throw IllegalArgumentException(" ${this.javaClass.simpleName} The fist view must be of type SheeView")
        }
        backView = getBackView() as ObservableDragLayout
        sheet = getFrontView() as SheetView
        interpolator = LinearInterpolator()

        horizontalRecyclerView = backView.getListActionBar().getRecyclerView()
        titleTextView = backView.getListActionBar().getTitleTextView()

        backView.touchListenerBackView = this
        backDropGestureRecognizer = BackDropGestureRecognizer(backView, sheet)
        mDetector = GestureDetectorCompat(context, this)

        setupListeners()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        sheet.animateY(backView.getTopTouchableViewPosition())
        backDropGestureRecognizer.toleranceDistance = backView.getTopViewHeight()
        if (backdropOpen) animateOpen(100)
    }

    override fun onInterceptTouch(event: MotionEvent, insideTopView: Boolean) {
        if (this.mDetector.onTouchEvent(event)) {
            return
        }
        if (movementStarted && event.action != MotionEvent.ACTION_DOWN && event.action != MotionEvent.ACTION_MOVE) {
            val sheetPosition = sheet.y
            val lineDivider = (backView.y + backView.height) / 2
            if (sheetPosition < lineDivider) {
                animateClose()
            } else {
                animateOpen()
            }
            backDropScrolling = BackDropScroll.NOT_SCROLLING
            backView.horizontalScrollEnabled = true
            movementStarted = false
        }
    }

    override fun onShowPress(event: MotionEvent?) {
    }

    override fun onSingleTapUp(event: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(event: MotionEvent?): Boolean {
        event?.let {
            backDropGestureRecognizer.setFirstActionDown(it)
            if (backView.touchInsideTopActionView(it)) {
                movementStarted = true
            }
            backDropScrolling = BackDropScroll.NOT_SCROLLING
            backView.horizontalScrollEnabled = true
            return true
        }
        return false
    }

    override fun onScroll(eventBefore: MotionEvent?, event: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        event?.let {
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    if (movementStarted) {
                        val topLimit = backView.y + backView.topActionView.height
                        val bottomLimit = backView.height + backView.y

                        val movePosition = backDropGestureRecognizer.getMovePointPosition(it)
                        val topActionAlpha = backDropGestureRecognizer.getTopActionAlpha()
                        val backListAlpha = backDropGestureRecognizer.getBackListAlpha()

                        backDropGestureRecognizer.LastMoveEventY = event.y

                        if (backDropScrolling == BackDropScroll.NOT_SCROLLING || backDropScrolling == BackDropScroll.HORIZONTAL_SCROLLING) {
                            eventBefore?.let {
                                backDropScrolling = backDropGestureRecognizer.getDirectionEvents(eventBefore, event)
                            }
                        }

                        if (movePosition > topLimit && movePosition < bottomLimit && backDropScrolling == BackDropScroll.VERTICAL_SCROLLING) {
                            /*
                            * if backdrop stuck for some reason when the last event is equal the current event, this condition cancel the gesture recognition
                            * */
                            if (sheet.y == movePosition) {
                                return false
                            }
                            backView.horizontalScrollEnabled = false
                            sheet.animateY(movePosition)
                            backView.getListActionBar().animateAlphaList(topActionAlpha)
                            backView.backListView.animateAlpha(backListAlpha).start()
                            backView.getListActionBar().setLeftButtonPercentAnimation(backListAlpha)
                            sheet.animateContentAlpha(topActionAlpha)

                            return true
                        }
                    }
                }
                else -> {
                    if (movementStarted) {
                        val sheetPosition = sheet.y
                        val lineDivider = (backView.y + backView.height) / 2
                        if (sheetPosition < lineDivider) {
                            animateClose()
                        } else {
                            animateOpen()
                        }
                        backDropScrolling = BackDropScroll.NOT_SCROLLING
                        backView.horizontalScrollEnabled = true
                        movementStarted = false
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun onLongPress(event: MotionEvent?) {
    }

    override fun onFling(event: MotionEvent?, eventBefore: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        if (movementStarted) {
            val sheetPosition = sheet.y
            val lineDivider = (backView.y + backView.height) / 2
            if (velocityY < 0 && backDropScrolling == BackDropScroll.VERTICAL_SCROLLING) {
                animateClose()
            } else if (velocityY > 0 && backDropScrolling == BackDropScroll.VERTICAL_SCROLLING) {
                animateOpen()
            } else if (sheetPosition < lineDivider) {
                animateClose()
            } else {
                animateOpen()
            }
            movementStarted = false
        }
        return true
    }

    fun animateClose(duration: Long = ANIM_DURATION) {
        clearAnimations()

        sheet.animateY(backView.getTopTouchableViewPosition(), duration).start()
        titleTextView.animateAlpha(0f, duration).start()
        horizontalRecyclerView.animateAlpha(1f, duration).start()
        backView.backListView.animateAlpha(0f, duration).start()

        backView.getListActionBar().playReversedLeftButtonPercentAnimation()
        sheet.fadeInContentView(duration)
        backdropOpen = false
    }

    fun animateOpen(duration: Long = ANIM_DURATION) {
        clearAnimations()

        sheet.animateY(getTranslationSheetY(), duration).start()
        titleTextView.animateAlpha(1f, duration).start()
        horizontalRecyclerView.animateAlpha(0f, duration).start()
        backView.backListView.animateAlpha(1f, duration).start()

        backView.getListActionBar().playLeftButtonPercentAnimation()
        sheet.fadeOutContentView(duration)
        backdropOpen = true
    }

    fun clearAnimations() {
        sheet.clearAnimation()
        titleTextView.clearAnimation()
        horizontalRecyclerView.clearAnimation()
        backView.clearAnimation()
    }

    fun smoothHorizontalScrollToPosition(position: Int) {
        backView.smoothHorizontalScrollToPosition(position)
    }

    fun getTranslationSheetY(): Float {
        return if (backView.height > sheet.height) {
            backView.y
        } else {
            BACKDROP_SPACE = (verticalAdapter as? BackDropAdapterSelection)?.viewList?.last()?.height ?: 0
            (backView.y + backView.height) - BACKDROP_SPACE
        }
    }

    /**
     * Function to return the back view.
     * @return the first view in this layout.
     */
    private fun getBackView(): View = getChildAt(0)

    /**
     * Function to return the backdrop view.
     * @return the second view in this layout.
     */
    private fun getFrontView(): View = getChildAt(1)
}