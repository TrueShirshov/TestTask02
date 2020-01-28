package org.shirshov.testtask.ui.component.recycler

import androidx.databinding.ViewDataBinding
import org.shirshov.testtask.R
import org.shirshov.testtask.util.extension.Ui

interface CoreTouchHelper {

    fun onItemMove(from: Int, to: Int): Boolean

    fun onItemDismiss(position: Int)

    fun reorderEnabled(): Boolean = true

    fun swipeToDismissEnabled(): Boolean = true

    fun highlightOnReorder(holder: CoreViewHolder<out ViewDataBinding>) {
        holder.itemView.alpha = 0.5f
    }

    fun clearAfterReorder(holder: CoreViewHolder<out ViewDataBinding>) {
        holder.itemView.alpha = 1.0f
    }

    fun onReorderBlocked() = Ui.showToast(R.string.toast_reorder_disabled)
}