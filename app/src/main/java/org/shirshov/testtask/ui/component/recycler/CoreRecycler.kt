package org.shirshov.testtask.ui.component.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG
import androidx.recyclerview.widget.RecyclerView
import org.shirshov.testtask.R
import org.shirshov.testtask.util.extension.initAttributes
import org.shirshov.testtask.util.extension.toPixels

class CoreRecycler : RecyclerView {

    private var bottomPadding: Boolean = true
    private var bottomPaddingValueDp: Int = 0
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var reorderEnabled: () -> Boolean
    private lateinit var onReorderBlocked: () -> Unit

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (bottomPadding) {
            clipToPadding = false
            setPadding(0, 0, 0, bottomPaddingValueDp.toPixels())
        }
    }

    fun registerTouchHelper(touchHelper: CoreTouchHelper) {
        itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
                return touchHelper.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                touchHelper.onItemDismiss(viewHolder.adapterPosition)
            }

            override fun isLongPressDragEnabled() = touchHelper.reorderEnabled()

            override fun isItemViewSwipeEnabled() = touchHelper.swipeToDismissEnabled()

            override fun onSelectedChanged(viewHolder: ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (viewHolder != null && actionState == ACTION_STATE_DRAG) touchHelper.highlightOnReorder(viewHolder as CoreViewHolder<out ViewDataBinding>)
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                touchHelper.clearAfterReorder(viewHolder as CoreViewHolder<out ViewDataBinding>)
            }

        })
        itemTouchHelper.attachToRecyclerView(this)
        reorderEnabled = touchHelper::reorderEnabled
        onReorderBlocked = touchHelper::onReorderBlocked
    }

    fun initDragStartItem(holder: CoreViewHolder<out ViewDataBinding>, item: View) {
        item.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                if (reorderEnabled()) itemTouchHelper.startDrag(holder) else onReorderBlocked()
            }
            return@setOnTouchListener true
        }
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        context.initAttributes(attrs, R.styleable.CoreRecycler) {
            bottomPadding = getBoolean(R.styleable.CoreRecycler_recycler_bottom_padding, true)
            bottomPaddingValueDp = getInteger(R.styleable.CoreRecycler_recycler_bottom_padding_value, 16)
        }
        setHasFixedSize(true)
        setItemViewCacheSize(20)
    }

    // Fixes issue with recycler inside coordinator layout that consumes first click after fast scroll to top or bottom
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        val requestCancelDisallowInterceptTouchEvent = scrollState == SCROLL_STATE_SETTLING
        val consumed = super.onInterceptTouchEvent(event)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> if (requestCancelDisallowInterceptTouchEvent) {
                parent.requestDisallowInterceptTouchEvent(false)

                // only if it touched the top or the bottom
                if (!canScrollVertically(-1) || !canScrollVertically(1)) {
                    // stop scroll to enable child view to get the touch event
                    stopScroll()
                    // do not consume the event
                    return false
                }
            }
        }

        return consumed
    }

    fun onScroll(block: () -> Unit) {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                block()
            }
        })
    }
}
