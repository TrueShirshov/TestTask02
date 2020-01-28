package org.shirshov.testtask.ui.component.recycler

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class CoreAdapter<H : ViewDataBinding, D> protected constructor(var items: List<D>) : RecyclerView.Adapter<CoreViewHolder<H>>() {

    abstract fun bindViewHolder(holder: CoreViewHolder<H>, item: D)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CoreViewHolder<H>, position: Int) {
        bindViewHolder(holder, items[position])
        holder.b.executePendingBindings() // Eliminates cell flickering on updates
    }

    fun updateItems(items: List<D>, diffCallback: DiffUtil.Callback? = null) {
        if (diffCallback != null) {
            DiffUtil.calculateDiff(diffCallback, true).also { this.items = items }.dispatchUpdatesTo(this)
        } else {
            this.items = items
            notifyDataSetChanged()
        }
    }

}