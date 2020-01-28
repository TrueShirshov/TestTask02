package org.shirshov.testtask.ui.component.recycler

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

class CoreViewHolder<T : ViewDataBinding>(val b: T, lifecycleOwner: LifecycleOwner) : RecyclerView.ViewHolder(b.root) {

    var onClick: ((Int) -> Unit)? = null

    init {
        b.lifecycleOwner = lifecycleOwner
        itemView.setOnClickListener {
            onClick?.invoke(adapterPosition)
        }
    }
}